package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.justmili.servertweaks.content.abilities.AbilityManager;
import net.justmili.servertweaks.content.abilities.AbilityUtil;
import net.justmili.servertweaks.content.abilities.ability.Ability;
import net.justmili.servertweaks.content.abilities.ability.AbilityModifier;
import net.justmili.servertweaks.content.commands.arguments.*;
import net.justmili.servertweaks.core.util.CommandUtil;
import net.justmili.servertweaks.core.util.FdaApiUtil;
import net.justmili.servertweaks.core.variables.PlayerAttachments;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class PlayerAbilities {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection environment) {
        dispatcher.register(
            Commands.literal("abilities")
                .then(Commands.literal("pickPreset")
                    .then(Commands.argument("preset", AbilitySetArgumentType.setSelect())
                        .suggests(AbilitySetArgumentType::suggest)
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            if (!CommandUtil.checkIfPlayerExecuted(context)) return 0;
                            ServerPlayer player = source.getPlayer();

                            String setName = StringArgumentType.getString(context, "preset");
                            AbilitySetArgumentType.AbilitySet set = AbilitySetArgumentType.getSet(setName);
                            if (set == null) {
                                CommandUtil.sendFail(source, "Unknown ability preset: "+setName);
                                return 0;
                            }

                            MutableComponent apply = Component.literal("     [APPLY] ").setStyle(Style.EMPTY.withColor(0x55FF55).withClickEvent(
                                    new ClickEvent.RunCommand("/abilities applyPreset "+setName+" "+player.getName().getString())));
                            MutableComponent cancel = Component.literal(" [CANCEL]").setStyle(Style.EMPTY.withColor(0xFF5555).withClickEvent(
                                    new ClickEvent.RunCommand("/abilities dontApplyPreset "+player.getName().getString())));

                            player.sendSystemMessage(Component.literal(set.description()+"\n\n").append(apply).append(cancel));
                            return 1;
                        })
                    )
                )

                .then(Commands.literal("reload")
                    .requires(src -> CommandUtil.hasPerms(src, 2))
                    .executes(context -> {
                        MinecraftServer server = context.getSource().getServer();
                        AbilityManager.loadFile(server);

                        CommandUtil.sendSucc(context.getSource(), "Reloaded Player Abilities");
                        return 1;
                    })
                )

                .then(Commands.literal("grant")
                    .requires(src -> CommandUtil.hasPerms(src, 2))
                    .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.literal("ability")
                            .then(Commands.argument("abilityOrDebuff", AbilitiesArgumentType.abilities())
                                .suggests(AbilitiesArgumentType::suggest)
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    Ability ability = AbilitiesArgumentType.getAbility(context, "abilityOrDebuff");

                                    AbilityUtil.grantAbility(player, ability);

                                    CommandUtil.sendSucc(context.getSource(), "Granted ability "+ability.getName()+" to player "+player.getName().getString());

                                    return 1;
                                })
                            )
                        )
                        .then(Commands.literal("modifier")
                            .then(Commands.argument("modifier", ModifiersArgumentType.modifier())
                                .suggests(ModifiersArgumentType::suggest)
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    AbilityModifier modifier = ModifiersArgumentType.getModifier(context, "modifier");
                                    AbilityUtil.grantModifier(player, modifier);
                                    CommandUtil.sendSucc(context.getSource(), "Granted ability modifier "+modifier.getName()+" to player "+player.getName().getString());
                                    return 1;
                                })
                            )
                        )
                    )
                )

                .then(Commands.literal("revoke")
                    .requires(src -> CommandUtil.hasPerms(src, 2))
                    .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.literal("ability")
                            .then(Commands.argument("abilityOrDebuff", AbilitiesArgumentType.abilities())
                                .suggests(AbilitiesArgumentType::suggest)
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    Ability ability = AbilitiesArgumentType.getAbility(context, "abilityOrDebuff");

                                    AbilityUtil.revokeAbility(player, ability);

                                    CommandUtil.sendSucc(context.getSource(), "Removed ability "+ability.getName()+" from player "+player.getName().getString());

                                    return 1;
                                })
                            )
                        )
                        .then(Commands.literal("modifier")
                            .then(Commands.argument("modifier", ModifiersArgumentType.modifier())
                                .suggests(ModifiersArgumentType::suggest)
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    AbilityModifier modifier = ModifiersArgumentType.getModifier(context, "modifier");

                                    AbilityUtil.revokeModifier(player, modifier);

                                    CommandUtil.sendSucc(context.getSource(), "Removed ability modifier "+modifier.getName()+" from player "+player.getName().getString());

                                    return 1;
                                })
                            )
                        )
                        .then(Commands.literal("everything")
                            .executes(context -> {
                                ServerPlayer player = EntityArgument.getPlayer(context, "player");

                                AbilityUtil.clearPlayer(player);
                                FdaApiUtil.setBoolValue(player, PlayerAttachments.PICKED_PRESET, false);

                                CommandUtil.sendSucc(context.getSource(), "Deleted player abilities profile of "+player.getName().getString());

                                return 1;
                            })
                        )
                    )
                )

                .then(Commands.literal("applyPreset")
                    .requires(src -> CommandUtil.hasPerms(src, 2))
                    .then(Commands.argument("preset", AbilitySetArgumentType.setSelect())
                        .suggests(AbilitySetArgumentType::suggest)
                        .then(Commands.argument("player", EntityArgument.player())
                            .executes(context -> {
                                CommandSourceStack source = context.getSource();
                                ServerPlayer player = EntityArgument.getPlayer(context, "player");

                                String setName = StringArgumentType.getString(context, "preset");
                                AbilitySetArgumentType.AbilitySet set = AbilitySetArgumentType.getSet(setName);
                                if (set == null) {
                                    CommandUtil.sendFail(source, "Unknown ability preset: "+setName);
                                    return 0;
                                }

                                if (FdaApiUtil.getBoolValue(player, PlayerAttachments.PICKED_PRESET)) {
                                    CommandUtil.sendFail(source, "You have already picked an ability preset.");
                                    return 0;
                                }

                                AbilityUtil.applySet(player.getUUID(), set, source.getServer());
                                FdaApiUtil.setBoolValue(player, PlayerAttachments.PICKED_PRESET, true);
                                CommandUtil.sendSucc(source, "Applied the "+setName+" preset!");
                                return 1;
                            })
                        )
                    )
                )
                .then(Commands.literal("dontApplyPreset")
                    .requires(src -> CommandUtil.hasPerms(src, 2))
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> 1)
                    )
                )
        );
    }
}