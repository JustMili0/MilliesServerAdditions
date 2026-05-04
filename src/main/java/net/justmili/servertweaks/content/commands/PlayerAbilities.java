package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.justmili.servertweaks.content.abilities.DataStorage;
import net.justmili.servertweaks.content.abilities.DataManager;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.AbilityModifier;
import net.justmili.servertweaks.content.abilities.arguments.*;
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
                    .then(Commands.argument("preset", PresetArgumentType.setSelect())
                        .suggests(PresetArgumentType::suggest)
                        .executes(context -> {
                            CommandSourceStack source = context.getSource();
                            if (!CommandUtil.checkIfPlayerExecuted(context)) return 0;
                            ServerPlayer player = source.getPlayer();

                            String setName = StringArgumentType.getString(context, "preset");
                            PresetArgumentType.AbilityPreset set = PresetArgumentType.getSet(setName);
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
                        DataStorage.loadFile(server);

                        CommandUtil.sendSucc(context.getSource(), "Reloaded Player Abilities");
                        return 1;
                    })
                )

                .then(Commands.literal("grant")
                    .requires(src -> CommandUtil.hasPerms(src, 2))
                    .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.literal("ability")
                            .then(Commands.argument("abilityOrDebuff", AbilityArgumentType.abilities())
                                .suggests(AbilityArgumentType::suggest)
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    Ability ability = AbilityArgumentType.getAbility(context, "abilityOrDebuff");

                                    DataManager.grantAbility(player, ability);

                                    CommandUtil.sendSucc(context.getSource(), "Granted ability "+ability.getName()+" to player "+player.getName().getString());

                                    return 1;
                                })
                            )
                        )
                        .then(Commands.literal("modifier")
                            .then(Commands.argument("modifier", ModifierArgumentType.modifier())
                                .suggests(ModifierArgumentType::suggest)
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    AbilityModifier modifier = ModifierArgumentType.getModifier(context, "modifier");
                                    DataManager.grantModifier(player, modifier);
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
                            .then(Commands.argument("abilityOrDebuff", AbilityArgumentType.abilities())
                                .suggests(AbilityArgumentType::suggest)
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    Ability ability = AbilityArgumentType.getAbility(context, "abilityOrDebuff");

                                    DataManager.revokeAbility(player, ability);

                                    CommandUtil.sendSucc(context.getSource(), "Removed ability "+ability.getName()+" from player "+player.getName().getString());

                                    return 1;
                                })
                            )
                        )
                        .then(Commands.literal("modifier")
                            .then(Commands.argument("modifier", ModifierArgumentType.modifier())
                                .suggests(ModifierArgumentType::suggest)
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    AbilityModifier modifier = ModifierArgumentType.getModifier(context, "modifier");

                                    DataManager.revokeModifier(player, modifier);

                                    CommandUtil.sendSucc(context.getSource(), "Removed ability modifier "+modifier.getName()+" from player "+player.getName().getString());

                                    return 1;
                                })
                            )
                        )
                        .then(Commands.literal("everything")
                            .executes(context -> {
                                ServerPlayer player = EntityArgument.getPlayer(context, "player");

                                DataManager.clearPlayer(player);
                                FdaApiUtil.setBoolValue(player, PlayerAttachments.PICKED_PRESET, false);

                                CommandUtil.sendSucc(context.getSource(), "Deleted player abilities profile of "+player.getName().getString());

                                return 1;
                            })
                        )
                    )
                )

                .then(Commands.literal("applyPreset")
                    .requires(src -> CommandUtil.hasPerms(src, 2))
                    .then(Commands.argument("preset", PresetArgumentType.setSelect())
                        .suggests(PresetArgumentType::suggest)
                        .then(Commands.argument("player", EntityArgument.player())
                            .executes(context -> {
                                CommandSourceStack source = context.getSource();
                                ServerPlayer player = EntityArgument.getPlayer(context, "player");

                                String setName = StringArgumentType.getString(context, "preset");
                                PresetArgumentType.AbilityPreset set = PresetArgumentType.getSet(setName);
                                if (set == null) {
                                    CommandUtil.sendFail(source, "Unknown ability preset: "+setName);
                                    return 0;
                                }

                                if (FdaApiUtil.getBoolValue(player, PlayerAttachments.PICKED_PRESET)) {
                                    CommandUtil.sendFail(source, "You have already picked an ability preset.");
                                    return 0;
                                }

                                DataManager.applySet(player.getUUID(), set, source.getServer());
                                FdaApiUtil.setBoolValue(player, PlayerAttachments.PICKED_PRESET, true);
                                CommandUtil.sendTo(player, "Applied the "+setName+" preset!");
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