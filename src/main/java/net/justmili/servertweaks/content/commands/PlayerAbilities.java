package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.justmili.libs.v1.utils.CommandUtil;
import net.justmili.libs.v1.utils.FdaApiUtil;
import net.justmili.servertweaks.content.abilities.manage.AbilityManager;
import net.justmili.servertweaks.content.abilities.manage.FileManager;
import net.justmili.servertweaks.content.abilities.arguments.AbilityArgumentType;
import net.justmili.servertweaks.content.abilities.arguments.ModifierArgumentType;
import net.justmili.servertweaks.content.abilities.arguments.PresetArgumentType;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.AbilityModifier;
import net.justmili.servertweaks.content.abilities.type.AbilityPreset;
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
                            ServerPlayer player = context.getSource().getPlayerOrException();

                            String setName = StringArgumentType.getString(context, "preset");
                            AbilityPreset set = PresetArgumentType.getSet(setName);
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
                    .requires(src -> CommandUtil.hasPerms(src, 1))
                    .executes(context -> {
                        MinecraftServer server = context.getSource().getServer();
                        FileManager.loadFile(server);

                        CommandUtil.sendOk(context.getSource(), "Reloaded Player Abilities");
                        return 1;
                    })
                )

                .then(Commands.literal("grant")
                    .requires(src -> CommandUtil.hasPerms(src, 1))
                    .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.literal("ability")
                            .then(Commands.argument("abilityOrDebuff", AbilityArgumentType.abilities())
                                .suggests(AbilityArgumentType::suggest)
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    Ability ability = AbilityArgumentType.getAbility(context, "abilityOrDebuff");

                                    AbilityManager.grantAbility(player, ability);

                                    CommandUtil.sendOk(context.getSource(), "Granted ability "+ability.getId()+" to player "+player.getName().getString());

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
                                    AbilityManager.grantModifier(player, modifier);
                                    CommandUtil.sendOk(context.getSource(), "Granted ability modifier "+modifier.getName()+" to player "+player.getName().getString());
                                    return 1;
                                })
                            )
                        )
                    )
                )

                .then(Commands.literal("revoke")
                    .requires(src -> CommandUtil.hasPerms(src, 1))
                    .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.literal("ability")
                            .then(Commands.argument("abilityOrDebuff", AbilityArgumentType.abilities())
                                .suggests(AbilityArgumentType::suggest)
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    Ability ability = AbilityArgumentType.getAbility(context, "abilityOrDebuff");

                                    AbilityManager.revokeAbility(player, ability);

                                    CommandUtil.sendOk(context.getSource(), "Removed ability "+ability.getId()+" from player "+player.getName().getString());

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

                                    AbilityManager.revokeModifier(player, modifier);

                                    CommandUtil.sendOk(context.getSource(), "Removed ability modifier "+modifier.getName()+" from player "+player.getName().getString());

                                    return 1;
                                })
                            )
                        )
                        .then(Commands.literal("everything")
                            .executes(context -> {
                                ServerPlayer player = EntityArgument.getPlayer(context, "player");

                                AbilityManager.clearPlayer(player);
                                FdaApiUtil.setBoolValue(player, PlayerAttachments.PICKED_PRESET, false);

                                CommandUtil.sendOk(context.getSource(), "Deleted player abilities profile of "+player.getName().getString());

                                return 1;
                            })
                        )
                    )
                )

                .then(Commands.literal("applyPreset")
                    .requires(src -> CommandUtil.hasPerms(src, 4))
                    .then(Commands.argument("preset", PresetArgumentType.setSelect())
                        .suggests(PresetArgumentType::suggest)
                        .then(Commands.argument("player", EntityArgument.player())
                            .executes(context -> {
                                CommandSourceStack source = context.getSource();
                                ServerPlayer player = EntityArgument.getPlayer(context, "player");

                                String setName = StringArgumentType.getString(context, "preset");
                                AbilityPreset set = PresetArgumentType.getSet(setName);
                                if (set == null) {
                                    CommandUtil.sendFailTo(player, "Unknown ability preset: "+setName);
                                    return 0;
                                }

                                if (FdaApiUtil.getBoolValue(player, PlayerAttachments.PICKED_PRESET)) {
                                    CommandUtil.sendFailTo(player, "You have already picked an ability preset.");
                                    return 0;
                                }

                                AbilityManager.applySet(player.getUUID(), set, source.getServer());
                                FdaApiUtil.setBoolValue(player, PlayerAttachments.PICKED_PRESET, true);
                                CommandUtil.sendOkTo(player, "Applied the "+setName+" preset!");
                                return 1;
                            })
                        )
                    )
                )
                .then(Commands.literal("dontApplyPreset")
                    .requires(src -> CommandUtil.hasPerms(src, 4))
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> 0)
                    )
                )
        );
    }
}