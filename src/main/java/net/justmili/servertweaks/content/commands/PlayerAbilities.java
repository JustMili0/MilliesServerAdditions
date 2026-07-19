package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.justmili.libs.v1.utils.CommandUtil;
import net.justmili.libs.v1.utils.FdaApiUtil;
import net.justmili.servertweaks.content.abilities.arguments.AbilityArgumentType;
import net.justmili.servertweaks.content.abilities.arguments.ModifierArgumentType;
import net.justmili.servertweaks.content.abilities.arguments.PresetArgumentType;
import net.justmili.servertweaks.content.abilities.core.AbilitiesFileUtil;
import net.justmili.servertweaks.content.abilities.core.FileManager;
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
import net.minecraft.network.chat.Style;

public class PlayerAbilities {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection environment) {
        dispatcher.register(
            Commands.literal("abilities")
                .then(Commands.literal("pickPreset")
                    .then(Commands.argument("preset", PresetArgumentType.preset())
                        .suggests(PresetArgumentType::suggest)
                        .executes(context -> {
                            var player = context.getSource().getPlayerOrException();
                            AbilityPreset preset = PresetArgumentType.getPreset(context, "preset");

                            var apply = Component.literal("     [APPLY] ").setStyle(Style.EMPTY.withColor(0x55FF55).withClickEvent(
                                    new ClickEvent.RunCommand("/abilities applyPreset "+ preset.getId() +" "+player.getName().getString())));
                            var cancel = Component.literal(" [CANCEL]").setStyle(Style.EMPTY.withColor(0xFF5555).withClickEvent(
                                    new ClickEvent.RunCommand("/abilities dontApplyPreset "+player.getName().getString())));

                            player.sendSystemMessage(Component.literal(preset.getDesc()+"\n\n").append(apply).append(cancel));
                            return 1;
                        })
                    )
                )

                .then(Commands.literal("reload")
                    .requires(src -> CommandUtil.hasPerms(src, 1))
                    .executes(context -> {
                        var server = context.getSource().getServer();
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
                                    var player = EntityArgument.getPlayer(context, "player");
                                    Ability ability = AbilityArgumentType.getAbility(context, "abilityOrDebuff");

                                    AbilitiesFileUtil.grantAbility(player, ability);
                                    CommandUtil.sendOk(context.getSource(), "Granted ability "+ability.getId()+" to player "+player.getName().getString());

                                    return 1;
                                })
                            )
                        )
                        .then(Commands.literal("modifier")
                            .then(Commands.argument("modifier", ModifierArgumentType.modifier())
                                .suggests(ModifierArgumentType::suggest)
                                .executes(context -> {
                                    var player = EntityArgument.getPlayer(context, "player");
                                    AbilityModifier modifier = ModifierArgumentType.getModifier(context, "modifier");

                                    AbilitiesFileUtil.grantModifier(player, modifier);
                                    CommandUtil.sendOk(context.getSource(), "Granted ability modifier "+modifier.getId()+" to player "+player.getName().getString());

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
                                    var player = EntityArgument.getPlayer(context, "player");
                                    Ability ability = AbilityArgumentType.getAbility(context, "abilityOrDebuff");

                                    AbilitiesFileUtil.revokeAbility(player, ability);
                                    CommandUtil.sendOk(context.getSource(), "Removed ability "+ability.getId()+" from player "+player.getName().getString());

                                    return 1;
                                })
                            )
                        )
                        .then(Commands.literal("modifier")
                            .then(Commands.argument("modifier", ModifierArgumentType.modifier())
                                .suggests(ModifierArgumentType::suggest)
                                .executes(context -> {
                                    var player = EntityArgument.getPlayer(context, "player");
                                    AbilityModifier modifier = ModifierArgumentType.getModifier(context, "modifier");

                                    AbilitiesFileUtil.revokeModifier(player, modifier);
                                    CommandUtil.sendOk(context.getSource(), "Removed ability modifier "+modifier.getId()+" from player "+player.getName().getString());

                                    return 1;
                                })
                            )
                        )
                        .then(Commands.literal("everything")
                            .executes(context -> {
                                var player = EntityArgument.getPlayer(context, "player");

                                AbilitiesFileUtil.clearPlayerProfile(player);
                                FdaApiUtil.setBoolValue(player, PlayerAttachments.HAS_PICKED_PRESET, false);

                                CommandUtil.sendOk(context.getSource(), "Cleared Abilities profile of "+player.getName().getString());

                                return 1;
                            })
                        )
                    )
                )

                .then(Commands.literal("applyPreset")
                    .requires(src -> CommandUtil.hasPerms(src, 4))
                    .then(Commands.argument("preset", PresetArgumentType.preset())
                        .suggests(PresetArgumentType::suggest)
                        .then(Commands.argument("player", EntityArgument.player())
                            .executes(context -> {
                                CommandSourceStack source = context.getSource();
                                var player = EntityArgument.getPlayer(context, "player");

                                AbilityPreset preset = PresetArgumentType.getPreset(context, "preset");

                                if (FdaApiUtil.getBoolValue(player, PlayerAttachments.HAS_PICKED_PRESET)) {
                                    CommandUtil.sendFailTo(player, "You've already picked an abilities preset.");
                                    return 0;
                                }

                                AbilitiesFileUtil.applyPreset(player, source.getServer(), preset);
                                FdaApiUtil.setBoolValue(player, PlayerAttachments.HAS_PICKED_PRESET, true);
                                CommandUtil.sendOkTo(player, "Applied the "+ preset.getId() +" preset!");

                                return 1;
                            })
                        )
                    )
                )
                .then(Commands.literal("dontApplyPreset")
                    .requires(src -> CommandUtil.hasPerms(src, 4))
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> 0))
                )
        );
    }
}