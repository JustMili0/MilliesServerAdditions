package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.justmili.corelibs.util.utils.common.CommandUtil;
import net.justmili.corelibs.util.utils.common.FdaUtil;
import net.justmili.corelibs.util.utils.common.Text;
import net.justmili.servertweaks.content.abilities.core.AbilityProfiles;
import net.justmili.servertweaks.content.abilities.core.AbilityProfilesUtil;
import net.justmili.servertweaks.content.abilities.type.*;
import net.justmili.servertweaks.content.commands.arguments.AbilityArgumentType;
import net.justmili.servertweaks.content.commands.arguments.DebuffArgumentType;
import net.justmili.servertweaks.content.commands.arguments.ModifierArgumentType;
import net.justmili.servertweaks.content.commands.arguments.PresetArgumentType;
import net.justmili.servertweaks.network.packets.ClientboundModCheckPacket;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;

import java.util.Set;
import java.util.stream.Stream;

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

                    .then(Commands.literal("ability").then(Commands.argument("ability", AbilityArgumentType.abilities())
                        .suggests(AbilityArgumentType::suggest)
                        .executes(context -> manage(
                            context.getSource(),
                            EntityArgument.getPlayer(context, "player"),
                            AbilityArgumentType.getAbility(context, "ability"),
                            Action.GRANT))))
                    .then(Commands.literal("debuff").then(Commands.argument("debuff", DebuffArgumentType.debuffs())
                        .suggests(DebuffArgumentType::suggest)
                        .executes(context -> manage(
                            context.getSource(),
                            EntityArgument.getPlayer(context, "player"),
                            DebuffArgumentType.getDebuff(context, "debuff"),
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

                    .then(Commands.literal("ability").then(Commands.argument("ability", AbilityArgumentType.abilities())
                        .suggests(AbilityArgumentType::suggest)
                        .executes(context -> manage(
                            context.getSource(),
                            EntityArgument.getPlayer(context, "player"),
                            AbilityArgumentType.getAbility(context, "ability"),
                            Action.REVOKE))))
                    .then(Commands.literal("debuff").then(Commands.argument("debuff", DebuffArgumentType.debuffs())
                        .suggests(DebuffArgumentType::suggest)
                        .executes(context -> manage(
                            context.getSource(),
                            EntityArgument.getPlayer(context, "player"),
                            DebuffArgumentType.getDebuff(context, "debuff"),
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
        var pName = player.getPlainTextName();

        switch (action) {
            case GRANT -> {
                AbilityProfilesUtil.grantAbility(player, ability);
                CommandUtil.sendOk(source, Text.string("Granted ability %s to %s", aName, pName));
            }
            case REVOKE -> {
                AbilityProfilesUtil.revokeAbility(player, ability);
                CommandUtil.sendOk(source, Text.string("Revoked ability %s from %s", aName, pName));
            }
        }

        return 1;
    }

    static int manage(CommandSourceStack source, ServerPlayer player, Modifier modifier, Action action) {
        var mName = modifier.getDisplayName();
        var pName = player.getPlainTextName();

        switch (action) {
            case GRANT -> {
                AbilityProfilesUtil.grantModifier(player, modifier);
                CommandUtil.sendOk(source, Text.string("Granted modifier %s to %s", mName, pName));
            }
            case REVOKE -> {
                AbilityProfilesUtil.revokeModifier(player, modifier);
                CommandUtil.sendOk(source, Text.string("Revoked modifier %s from %s", mName, pName));
            }
        }

        return 1;
    }

    static int manage(CommandSourceStack source, ServerPlayer player, Debuff debuff, Action action) {
        var dName = debuff.getDisplayName();
        var pName = player.getPlainTextName();

        switch (action) {
            case GRANT -> {
                AbilityProfilesUtil.grantDebuff(player, debuff);
                CommandUtil.sendOk(source, Text.string("Granted debuff %s to %s", dName, pName));
            }
            case REVOKE -> {
                AbilityProfilesUtil.revokeDebuff(player, debuff);
                CommandUtil.sendOk(source, Text.string("Revoked debuff %s from %s", dName, pName));
            }
        }

        return 1;
    }

    static int clear(CommandSourceStack source, ServerPlayer player) {
        AbilityProfilesUtil.clearPlayerProfile(player);
        FdaUtil.set(player, PlayerVars.HAS_PICKED_PRESET, false);

        CommandUtil.sendOk(source, Text.string("Cleared the abilities profile of %s", player.getPlainTextName()));

        return 1;
    }

    static int reload(CommandSourceStack source) {
        AbilityProfiles.reloadProfiles(source.getServer());

        CommandUtil.sendOk(source, "Reloaded player abilities");
        return 1;
    }

    static int presentPreset(CommandSourceStack source, Preset preset) throws CommandSyntaxException {
        var player = source.getPlayerOrException();

        if (FdaUtil.getBool(player, PlayerVars.HAS_PICKED_PRESET)) {
            CommandUtil.sendFailTo(player, "You've already picked a player abilities preset");
            return 0;
        }

        var apply = Text.string("     [APPLY] ").setStyle(Style.EMPTY.withColor(0x55FF55).withClickEvent(
            new ClickEvent.RunCommand("/abilities applyPreset " + preset.getId() + " " + player.getPlainTextName())));
        var cancel = Text.string(" [CANCEL]").setStyle(Style.EMPTY.withColor(0xFF5555).withClickEvent(
            new ClickEvent.RunCommand("/abilities dontApplyPreset " + player.getPlainTextName())));

        CommandUtil.sendOkTo(player, Text.string( "%s\n\n", preset.getDesc()).append(apply).append(cancel), false);
        return 1;
    }

    static int setPreset(CommandSourceStack source, Preset preset) throws CommandSyntaxException {
        var player = source.getPlayerOrException();
        var psName = preset.getDisplayName();

        AbilityProfilesUtil.applyPreset(player, preset);
        FdaUtil.set(player, PlayerVars.HAS_PICKED_PRESET, true);
        CommandUtil.sendOkTo(player, Text.string("\nApplied the \"%s\" preset!", psName));

        var abilities = preset.getAbilities();
        var debuffs = preset.getDebuffs();
        var modifiers = preset.getModifiers();
        int total = abilities.size() + debuffs.size() + modifiers.size();

        if (ServerPlayNetworking.canSend(player, ClientboundModCheckPacket.PACKET_ID)) return total; // Shush if client already has mod
        if (Stream.of(abilities, debuffs, modifiers).flatMap(Set::stream).anyMatch(AnyType::isClientRequired)) {
            CommandUtil.sendOkTo(player, Text.string("""
                One of the abilities, debuffs or modifiers in %s preset
                also requires Millie's Server Additions to be installed
                client-side to function properly.
                Please make sure you have it installed!
                """, psName).withColor(TextColor.YELLOW));
        }

        return total;
    }
}