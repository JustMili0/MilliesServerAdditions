package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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
import net.justmili.servertweaks.network.packets.ClientboundModCheckPacket;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
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
                            Action.GRANT))))

                    .then(Commands.literal("modifier").then(Commands.argument("modifier", ModifierArgumentType.modifier())
                        .suggests(ModifierArgumentType::suggest)
                        .executes(context -> manage(
                            context.getSource(),
                            EntityArgument.getPlayer(context, "player"),
                            ModifierArgumentType.getModifier(context, "modifier"),
                            Action.GRANT))))
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
                            Action.REVOKE))))
                    .then(Commands.literal("modifier").then(Commands.argument("modifier", ModifierArgumentType.modifier())
                        .suggests(ModifierArgumentType::suggest)
                        .executes(context -> manage(
                            context.getSource(),
                            EntityArgument.getPlayer(context, "player"),
                            ModifierArgumentType.getModifier(context, "modifier"),
                            Action.REVOKE))))

                    .then(Commands.literal("everything")
                        .executes(context -> clear(context.getSource(), EntityArgument.getPlayer(context, "player"))))
                )
            )

            .then(Commands.literal("applyPreset").requires(src -> CommandUtil.hasPerms(src, 4))
                .then(Commands.argument("preset", PresetArgumentType.preset())
                    .suggests(PresetArgumentType::suggest)
                    .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> setPreset(context.getSource(), PresetArgumentType.getPreset(context, "preset"))))))
            .then(Commands.literal("dontApplyPreset").requires(src -> CommandUtil.hasPerms(src, 4))
                .then(Commands.argument("player", EntityArgument.player()).executes(_ -> 1)))
        );
    }

    enum Action {
        GRANT, REVOKE
    }

    static int manage(CommandSourceStack source, ServerPlayer player, Ability ability, Action action) {
        var aName = ability.getDisplayName();
        var pName = player.getName().getString();

        switch (action) {
            case GRANT -> {
                AbilityProfilesUtil.grantAbility(player, ability);
                CommandUtil.sendOk(source, String.format("Granted ability %s to %s", aName, pName));
            }
            case REVOKE -> {
                AbilityProfilesUtil.revokeAbility(player, ability);
                CommandUtil.sendOk(source, String.format("Revoked ability %s from %s", aName, pName));
            }
        }

        return 1;
    }

    static int manage(CommandSourceStack source, ServerPlayer player, AbilityModifier modifier, Action action) {
        var aName = modifier.getDisplayName();
        var pName = player.getName().getString();

        switch (action) {
            case GRANT -> {
                AbilityProfilesUtil.grantModifier(player, modifier);
                CommandUtil.sendOk(source, String.format("Granted ability modifier %s to %s", aName, pName));
            }
            case REVOKE -> {
                AbilityProfilesUtil.revokeModifier(player, modifier);
                CommandUtil.sendOk(source, String.format("Revoked ability modifier %s from %s", aName, pName));
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
        AbilityProfiles.loadFileServer(server);

        CommandUtil.sendOk(source, "Reloaded Player Abilities");
        return 1;
    }

    static int presentPreset(CommandSourceStack source, AbilityPreset preset) throws CommandSyntaxException {
        var player = source.getPlayerOrException();

        if (FdaUtil.getBool(player, PlayerVars.HAS_PICKED_PRESET)) {
            CommandUtil.sendFailTo(player, "You've already picked a Player Abilities Preset");
            return 0;
        }

        var apply = Component.literal("     [APPLY] ").setStyle(Style.EMPTY.withColor(0x55FF55).withClickEvent(
            new ClickEvent.RunCommand("/abilities applyPreset " + preset.getId() + " " + player.getName().getString())));
        var cancel = Component.literal(" [CANCEL]").setStyle(Style.EMPTY.withColor(0xFF5555).withClickEvent(
            new ClickEvent.RunCommand("/abilities dontApplyPreset " + player.getName().getString())));

        CommandUtil.sendOkTo(player, Component.literal(preset.getDesc() + "\n\n").append(apply).append(cancel), false);
        return 1;
    }

    static int setPreset(CommandSourceStack source, AbilityPreset preset) throws CommandSyntaxException {
        var player = source.getPlayerOrException();
        var psName = preset.getDisplayName();

        AbilityProfilesUtil.applyPreset(player, source.getServer(), preset);
        FdaUtil.set(player, PlayerVars.HAS_PICKED_PRESET, true);
        CommandUtil.sendOkTo(player, "\nApplied the \"" + psName + "\" Abilities Preset!");

        if (ServerPlayNetworking.canSend(player, ClientboundModCheckPacket.PACKET_ID)) return 1; // Shush if client already has mod
        for (var ability : preset.getAbilities()) { // Inform client needs mod
            if (ability.isClientRequired()) {
                CommandUtil.sendOkTo(player, Component.literal(
                    String.format("""
                    One of the abilities in %s preset also requires
                    Millie's Server Additions to be installed client-side to function properly.
                    Please make sure you have it installed!
                    """, psName)
                ).withColor(TextColor.YELLOW));
                break; // Close loop after finding just one
            }
        }

        return 1;
    }
}