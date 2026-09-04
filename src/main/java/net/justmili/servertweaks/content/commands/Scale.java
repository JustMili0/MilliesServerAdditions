package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.justmili.corelibs.v1.utils.common.CommandUtil;
import net.justmili.corelibs.v1.utils.common.FdaUtil;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.util.ScalerUtil;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

import static net.justmili.servertweaks.util.ScalerUtil.applyScaleToPlayer;

public class Scale {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("scale")
            .then(Commands.argument("height_cm", FloatArgumentType.floatArg(Config.scaleMinHeight.get(), Config.scaleMaxHeight.get()))
                .executes(context -> setScale(context.getSource(), FloatArgumentType.getFloat(context, "height_cm"))))

            .then(Commands.literal("force").requires(src -> CommandUtil.hasPerms(src, 1))
                .then(Commands.argument("player", EntityArgument.players())
                    .then(Commands.argument("height_cm", FloatArgumentType.floatArg(18.5f, 2960.0f))
                        .executes(context -> forceScale(
                            context.getSource(), EntityArgument.getPlayers(context, "player"), FloatArgumentType.getFloat(context, "height_cm"))))))

            .then(Commands.literal("unlock").requires(src -> CommandUtil.hasPerms(src, 1))
                .then(Commands.argument("player", EntityArgument.players())
                    .executes(context -> unlockScale(
                        context.getSource(), EntityArgument.getPlayers(context, "player")))))

            .then(Commands.literal("reset").requires(src -> CommandUtil.hasPerms(src, 1))
                .then(Commands.argument("player", EntityArgument.players())
                    .executes(context -> resetScale(
                        context.getSource(), EntityArgument.getPlayers(context, "player"), true))))

            .then(Commands.literal("reset-nounlock").requires(src -> CommandUtil.hasPerms(src, 1))
                .then(Commands.argument("player", EntityArgument.players())
                    .executes(context -> resetScale(
                        context.getSource(), EntityArgument.getPlayers(context, "player"), false))))
        );
    }

    static int setScale(CommandSourceStack source, float height) throws CommandSyntaxException {
        var player = source.getPlayerOrException();

        if (FdaUtil.getBool(player, PlayerVars.SCALE_LOCKED)) {
            CommandUtil.sendFail(source, "You can not change your height more than once");
            return 0;
        }

        float scale = height / 185f;
        ScalerUtil.applyScaleToPlayer(player, scale);
        FdaUtil.set(player, PlayerVars.SCALE_LOCKED, true);

        CommandUtil.sendOk(source, String.format("Your irl-to-game scale is %.3f (%.1f cm). It is now locked", scale, height), false);

        return 1;
    }

    static int forceScale(CommandSourceStack source, Collection<ServerPlayer> players, float height) {
        float scale = height / 185f;
        for (var player : players) applyScaleToPlayer(player, scale);
        CommandUtil.sendOk(source, String.format("Applied scale %.3f (%.1f cm) to %d player(s)", scale, height, players.size()));

        return players.size();
    }

    static int unlockScale(CommandSourceStack source, Collection<ServerPlayer> players) {
        for (var player : players) FdaUtil.set(player, PlayerVars.SCALE_LOCKED, false);

        CommandUtil.sendOk(source, String.format("Unlocked scale modification for %d player(s)", players.size()));

        return players.size();
    }

    static int resetScale(CommandSourceStack source, Collection<ServerPlayer> players, boolean unlock) {
        for (var player : players) {
            applyScaleToPlayer(player, 1f);
            if (unlock) FdaUtil.set(player, PlayerVars.SCALE_LOCKED, false);
        }

        String unlocked = unlock? "Reset scale and unlocked scale modifications for %d player(s)" : "Reset scale for %d player(s)";
        CommandUtil.sendOk(source, String.format(unlocked, players.size()));

        return players.size();
    }
}
