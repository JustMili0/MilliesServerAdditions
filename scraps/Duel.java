package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.justmili.servertweaks.core.variables.PlayerAttachments;
import net.justmili.servertweaks.mechanics.logic.WhileDuel;
import net.justmili.servertweaks.core.util.CommandUtil;
import net.justmili.servertweaks.core.util.FdaApiUtil;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public class Duel {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection environment) {
        dispatcher.register(
            Commands.literal("duel")
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(context -> {
                        CommandUtil.checkIfPlayerExecuted(context);

                        ServerPlayer recipient = EntityArgument.getPlayer(context, "player");
                        ServerPlayer sender = context.getSource().getPlayer();

                        //Prevent dueling yourself
                        if (sender == recipient) {
                            CommandUtil.sendTo(sender, "You can not duel yourself");
                            return 0;
                        }

                        // Prevent new duels while in duel
                        if (FdaApiUtil.getBoolValue(sender, PlayerAttachments.IN_DUEL)) {
                            CommandUtil.sendTo(sender, "You are already in a duel");
                            return 0;
                        }
                        if (FdaApiUtil.getBoolValue(recipient, PlayerAttachments.IN_DUEL)) {
                            CommandUtil.sendTo(sender, recipient.getName().getString() + " is already in a duel");
                            return 0;
                        }

                        // Prevent overwriting invites
                        String senderPendingRecipient = FdaApiUtil.getStringValue(sender, PlayerAttachments.AWAITING_DUEL_RECIPIENT);
                        if (!senderPendingRecipient.equals("none")) {
                            CommandUtil.sendTo(sender, "You've already sent a duel request to " + recipient.getName().getString());
                            return 0;
                        }
                        String recipientPendingSender = FdaApiUtil.getStringValue(recipient, PlayerAttachments.AWAITING_DUEL_SENDER);
                        if (!recipientPendingSender.equals("none")) {
                            CommandUtil.sendTo(sender, recipient.getName().getString() + " already has a pending duel request");
                            CommandUtil.sendTo(recipient, sender.getName().getString() + " tried to send you a duel request but you already have one pending");
                            return 0;
                        }

                        // Send request
                        CommandUtil.sendTo(sender, "You've sent a duel request to " + recipient.getName().getString());
                        CommandUtil.sendTo(recipient, sender.getName().getString() + " has sent you a duel request");

                        FdaApiUtil.setStringValue(sender, PlayerAttachments.AWAITING_DUEL_RECIPIENT, recipient.getStringUUID());
                        FdaApiUtil.setStringValue(recipient, PlayerAttachments.AWAITING_DUEL_SENDER, sender.getStringUUID());

                        return 1;
                    })
                )
                .then(Commands.literal("accept")
                    .executes(context -> {
                        CommandUtil.checkIfPlayerExecuted(context);

                        ServerPlayer recipient = context.getSource().getPlayer();
                        String senderUUID = FdaApiUtil.getStringValue(recipient, PlayerAttachments.AWAITING_DUEL_SENDER);

                        if (!senderUUID.equals("none")) {
                            ServerPlayer sender = context.getSource().getServer().getPlayerList().getPlayer(java.util.UUID.fromString(senderUUID));

                            if (sender == null) {
                                CommandUtil.sendTo(recipient, "That player is no longer online");
                                FdaApiUtil.setStringValue(recipient, PlayerAttachments.AWAITING_DUEL_SENDER, "none");
                                return 0;
                            }

                            FdaApiUtil.setStringValue(recipient, PlayerAttachments.DUELING_WITH, sender.getStringUUID());
                            FdaApiUtil.setStringValue(sender, PlayerAttachments.DUELING_WITH, recipient.getStringUUID());

                            FdaApiUtil.setBoolValue(recipient, PlayerAttachments.IN_DUEL, true);
                            FdaApiUtil.setBoolValue(sender, PlayerAttachments.IN_DUEL, true);

                            FdaApiUtil.setStringValue(recipient, PlayerAttachments.AWAITING_DUEL_SENDER, "none");
                            FdaApiUtil.setStringValue(sender, PlayerAttachments.AWAITING_DUEL_RECIPIENT, "none");

                            CommandUtil.sendTo(recipient, "You are now in a duel with " + sender.getName().getString());
                            CommandUtil.sendTo(sender, "You are now in a duel with " + recipient.getName().getString());

                            return 1;
                        }

                        CommandUtil.sendTo(recipient, "You don't have a pending duel request");
                        return 0;
                    })
                )
                .then(Commands.literal("decline")
                    .executes(context -> {
                        CommandUtil.checkIfPlayerExecuted(context);

                        ServerPlayer recipient = context.getSource().getPlayer();
                        String senderUUID = FdaApiUtil.getStringValue(recipient, PlayerAttachments.AWAITING_DUEL_SENDER);

                        if (!senderUUID.equals("none")) {
                            ServerPlayer sender = context.getSource().getServer().getPlayerList().getPlayer(java.util.UUID.fromString(senderUUID));

                            FdaApiUtil.setStringValue(recipient, PlayerAttachments.AWAITING_DUEL_SENDER, "none");

                            if (sender == null) {
                                CommandUtil.sendTo(recipient, "That player is no longer online");
                                return 0;
                            }

                            FdaApiUtil.setStringValue(sender, PlayerAttachments.AWAITING_DUEL_RECIPIENT, "none");

                            CommandUtil.sendTo(recipient, "You declined a duel with " + sender.getName().getString());
                            CommandUtil.sendTo(sender, recipient.getName().getString() + " declined your duel");

                            return 1;
                        }

                        CommandUtil.sendTo(recipient, "You don't have a pending duel request");
                        return 0;
                    })
                )
                .then(Commands.literal("end")
                    .executes(context -> {
                        CommandUtil.checkIfPlayerExecuted(context);

                        ServerPlayer player = context.getSource().getPlayer();

                        if (!FdaApiUtil.getBoolValue(player, PlayerAttachments.IN_DUEL)) {
                            CommandUtil.sendTo(player, "You are not in a duel");
                            return 0;
                        }

                        long ticksSinceHit = player.level().getGameTime() - FdaApiUtil.getLongValue(player, PlayerAttachments.LAST_HIT_TIME);
                        if (ticksSinceHit < 600) {
                            CommandUtil.sendTo(player, "You cannot end a duel within 30 seconds of being hit");
                            return 0;
                        }

                        String opponentUUID = FdaApiUtil.getStringValue(player, PlayerAttachments.DUELING_WITH);
                        ServerPlayer opponent = context.getSource().getServer().getPlayerList().getPlayer(java.util.UUID.fromString(opponentUUID));

                        WhileDuel.endDuel(player, opponent);

                        CommandUtil.sendTo(player, "You have ended the duel");
                        if (opponent != null) CommandUtil.sendTo(opponent, player.getName().getString() + " has ended the duel");

                        return 1;
                    })
                )
        );
    }
}