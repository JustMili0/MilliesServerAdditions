package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.justmili.libs.v1.utils.common.CommandUtil;
import net.justmili.libs.v1.utils.common.FdaUtil;
import net.justmili.servertweaks.content.abilities.core.AbilityProfiles;
import net.justmili.servertweaks.content.abilities.core.AbilityProfilesUtil;
import net.justmili.servertweaks.content.abilities.type.Ability;
import net.justmili.servertweaks.content.abilities.type.AbilityModifier;
import net.justmili.servertweaks.content.abilities.type.AbilityPreset;
import net.justmili.servertweaks.content.commands.arguments.AbilityArgumentType;
import net.justmili.servertweaks.content.commands.arguments.ModifierArgumentType;
import net.justmili.servertweaks.content.commands.arguments.PresetArgumentType;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

public class PlayerAbilities {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("abilities")
            .then(Commands.literal("pickPreset")
                .then(Commands.argument("preset", PresetArgumentType.preset())
                    .suggests(PresetArgumentType::suggest)
                    .executes(context -> presentPreset(context.getSource(), PresetArgumentType.getPreset(context, "preset")))))

            .then(Commands.literal("reload").requires(src -> CommandUtil.hasPerms(src, 1))
                .executes(context -> reload(context.getSource())))

            .then(Commands.literal("grant").requires(src -> CommandUtil.hasPerms(src, 1))
                .then(Commands.argument("player", EntityArgument.player())

                    .then(Commands.literal("ability").then(Commands.argument("abilityOrDebuff", AbilityArgumentType.abilities())
                        .suggests(AbilityArgumentType::suggest)
                        .executes(context -> manage(
                            context.getSource(),
                            EntityArgument.getPlayer(context, "player"),
                            AbilityArgumentType.getAbility(context, "abilityOrDebuff"),
                            0))))

                    .then(Commands.literal("modifier").then(Commands.argument("modifier", ModifierArgumentType.modifier())
                        .suggests(ModifierArgumentType::suggest)
                        .executes(context -> manage(
                            context.getSource(),
                            EntityArgument.getPlayer(context, "player"),
                            ModifierArgumentType.getModifier(context, "modifier"),
                            0))))
                )
            )

            .then(Commands.literal("revoke").requires(src -> CommandUtil.hasPerms(src, 1))
                .then(Commands.argument("player", EntityArgument.player())

                    .then(Commands.literal("ability").then(Commands.argument("abilityOrDebuff", AbilityArgumentType.abilities())
                        .suggests(AbilityArgumentType::suggest)
                        .executes(context -> manage(
                            context.getSource(),
                            EntityArgument.getPlayer(context, "player"),
                            AbilityArgumentType.getAbility(context, "abilityOrDebuff"),
                            1))))
                    .then(Commands.literal("modifier").then(Commands.argument("modifier", ModifierArgumentType.modifier())
                        .suggests(ModifierArgumentType::suggest)
                        .executes(context -> manage(
                            context.getSource(),
                            EntityArgument.getPlayer(context, "player"),
                            ModifierArgumentType.getModifier(context, "modifier"),
                            1))))

                    .then(Commands.literal("everything")
                        .executes(context -> clear(context.getSource(), EntityArgument.getPlayer(context, "player"))))
                )
            )

            .then(Commands.literal("applyPreset").requires(src -> CommandUtil.hasPerms(src, 4))
                .then(Commands.argument("preset", PresetArgumentType.preset())
                    .suggests(PresetArgumentType::suggest)
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> setPrest(context.getSource(), PresetArgumentType.getPreset(context, "preset"))))))
            .then(Commands.literal("dontApplyPreset").requires(src -> CommandUtil.hasPerms(src, 4))
                .then(Commands.argument("player", EntityArgument.player()).executes(_ -> 1)))
        );
    }

    static int manage(CommandSourceStack source, ServerPlayer player, Ability ability, int action) {
        var id = ability.getId();
        var name = player.getName().getString();

        switch (action) {
            case 0 -> {
                AbilityProfilesUtil.grantAbility(player, ability);
                CommandUtil.sendOk(source, "Granted ability " + id + " to player " + name);
            }
            case 1 -> {
                AbilityProfilesUtil.revokeAbility(player, ability);
                CommandUtil.sendOk(source, "Revoked ability " + id + " from player " + name);
            }
        }

        return 1;
    }

    static int manage(CommandSourceStack source, ServerPlayer player, AbilityModifier modifier, int action) {
        var id = modifier.getId();
        var name = player.getName().getString();

        switch (action) {
            case 0 -> {
                AbilityProfilesUtil.grantModifier(player, modifier);
                CommandUtil.sendOk(source, "Granted ability modifier " + id + " to player " + name);
            }
            case 1 -> {
                AbilityProfilesUtil.revokeModifier(player, modifier);
                CommandUtil.sendOk(source, "Revoked ability modifier " + id + " from player " + name);
            }
        }

        return 1;
    }

    static int clear(CommandSourceStack source, ServerPlayer player) {
        AbilityProfilesUtil.clearPlayerProfile(player);
        FdaUtil.set(player, PlayerVars.HAS_PICKED_PRESET, false);

        CommandUtil.sendOk(source, "Cleared the Abilities Profile of " + player.getName().getString());

        return 1;
    }

    static int reload(CommandSourceStack source) {
        var server = source.getServer();
        AbilityProfiles.loadFile(server);

        CommandUtil.sendOk(source, "Reloaded Player Abilities");
        return 1;
    }

    static int presentPreset(CommandSourceStack source, AbilityPreset preset) throws CommandSyntaxException {
        var player = source.getPlayerOrException();

        var apply = Component.literal("     [APPLY] ").setStyle(Style.EMPTY.withColor(0x55FF55).withClickEvent(
            new ClickEvent.RunCommand("/abilities applyPreset " + preset.getId() + " " + player.getName().getString())));
        var cancel = Component.literal(" [CANCEL]").setStyle(Style.EMPTY.withColor(0xFF5555).withClickEvent(
            new ClickEvent.RunCommand("/abilities dontApplyPreset " + player.getName().getString())));

        CommandUtil.sendOkTo(player, Component.literal(preset.getDesc() + "\n\n").append(apply).append(cancel), false);
        return 1;
    }

    static int setPrest(CommandSourceStack source, AbilityPreset preset) throws CommandSyntaxException {
        var player = source.getPlayerOrException();

        if (FdaUtil.getBool(player, PlayerVars.HAS_PICKED_PRESET)) {
            CommandUtil.sendFailTo(player, "You've already picked an abilities preset.");
            return 0;
        }

        AbilityProfilesUtil.applyPreset(player, source.getServer(), preset);
        FdaUtil.set(player, PlayerVars.HAS_PICKED_PRESET, true);
        CommandUtil.sendOkTo(player, "Applied the " + preset.getId() + " preset!");

        return 1;
    }
}