package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.justmili.libs.v1.utils.common.CommandUtil;
import net.justmili.libs.v1.utils.common.FdaUtil;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.util.ScalerUtil;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

import static net.justmili.servertweaks.util.ScalerUtil.applyScaleToPlayer;

public class Scale {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection environment) {
        dispatcher.register(
            Commands.literal("scale")
                .then(Commands.argument("height_cm", FloatArgumentType.floatArg(Config.scaleMinHeight.get(), Config.scaleMaxHeight.get()))
                    .executes(context -> {
                        var source = context.getSource();
                        var player = source.getPlayerOrException();

                        if (FdaUtil.getBool(player, PlayerVars.SCALE_LOCKED)) {
                            CommandUtil.sendFail(source, "You can not change your height more than once");
                            return 0;
                        }

                        float heightCm = FloatArgumentType.getFloat(context, "height_cm");
                        float scale = heightCm / 185f;
                        ScalerUtil.applyScaleToPlayer(player, scale);
                        FdaUtil.set(player, PlayerVars.SCALE_LOCKED, true);

                        CommandUtil.sendOk(source, String.format("Your irl-to-game scale is %.3f (%.1f cm). It is now locked", scale, heightCm), false);
                        return 1;
                    })
                )
                .then(Commands.literal("force")
                    .requires(src -> CommandUtil.hasPerms(src, 1))
                    .then(Commands.argument("player", EntityArgument.players())
                        .then(Commands.argument("height_cm", FloatArgumentType.floatArg(18.5f, 2960.0f))
                            .executes(context -> {
                                var source = context.getSource();

                                float heightCm = FloatArgumentType.getFloat(context, "height_cm");
                                float scale = heightCm / 185f;

                                var players = EntityArgument.getPlayers(context, "player");
                                for (var player : players) applyScaleToPlayer(player, scale);

                                CommandUtil.sendOk(source, String.format("Applied scale %.3f (%.1f cm) to %d player(s)", scale, heightCm, players.size()), true);
                                return players.size();
                            })
                        )
                    )
                )
                .then(Commands.literal("unlock")
                    .requires(src -> CommandUtil.hasPerms(src, 1))
                    .then(Commands.argument("player", EntityArgument.players())
                        .executes(context -> {
                            var source = context.getSource();

                            var players = EntityArgument.getPlayers(context, "player");
                            for (var player : players) FdaUtil.set(player, PlayerVars.SCALE_LOCKED, false);

                            CommandUtil.sendOk(source, String.format("Unlocked scale modification for %d player(s)", players.size()), true);
                            return players.size();
                        })
                    )
                )
                .then(Commands.literal("reset")
                    .requires(src -> CommandUtil.hasPerms(src, 1))
                    .then(Commands.argument("player", EntityArgument.players())
                        .executes(context -> {
                            var source = context.getSource();

                            var players = EntityArgument.getPlayers(context, "player");
                            for (var player : players) {
                                applyScaleToPlayer(player, 1f);
                                FdaUtil.set(player, PlayerVars.SCALE_LOCKED, false);
                            }

                            CommandUtil.sendOk(source, String.format("Reset scale and unlocked scale modifications for %d player(s)", players.size()), true);
                            return players.size();
                        })
                    )
                )
                .then(Commands.literal("reset-nounlock")
                    .requires(src -> CommandUtil.hasPerms(src, 1))
                    .then(Commands.argument("player", EntityArgument.players())
                        .executes(context -> {
                            var source = context.getSource();

                            var players = EntityArgument.getPlayers(context, "player");
                            for (var player : players) applyScaleToPlayer(player, 1f);

                            CommandUtil.sendOk(source, String.format("Reset scale for %d player(s)", players.size()), true);
                            return players.size();
                        })
                    )
                )
        );
    }
}
