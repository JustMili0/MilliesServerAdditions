package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.justmili.corelibs.v1.utils.common.CommandUtil;
import net.justmili.corelibs.v1.utils.common.EntityUtil;
import net.justmili.corelibs.v1.utils.common.FdaUtil;
import net.justmili.corelibs.v1.utils.common.MathUtil;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import net.minecraft.world.scores.TeamColor;

import java.util.Optional;

public class Afk {
    private static final String AFK_PLAYERS = "afk_players";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("afk")
            .executes(context -> {
                var source = context.getSource();

                var level = source.getLevel();
                var player = source.getPlayerOrException();

                int cooldown = FdaUtil.getInt(player, PlayerVars.AFK_COOLDOWN);
                if (!FdaUtil.getBool(player, PlayerVars.IS_AFK) && Config.afkCommandCooldown.get() != 0 && cooldown > 0) {
                    CommandUtil.sendFail(source, "You must wait " + MathUtil.ticksToTime(cooldown) + " before using this command again");
                    return 0;
                }

                var scoreboard = level.getScoreboard();
                var team = scoreboard.getPlayerTeam(AFK_PLAYERS);
                if (team == null) team = createTeam(scoreboard);

                return toggleAfk(source, player, scoreboard, team);
            })
        );
    }

    static PlayerTeam createTeam(ServerScoreboard board) {
        var team = board.addPlayerTeam(AFK_PLAYERS);
        team.setNameTagVisibility(Team.Visibility.ALWAYS);
        team.setPlayerPrefix(Component.literal("[AFK] "));
        team.setColor(Optional.of(TeamColor.GRAY));
        return team;
    }

    static int toggleAfk(CommandSourceStack source, ServerPlayer player, ServerScoreboard board, PlayerTeam team) {
        if (FdaUtil.getBool(player, PlayerVars.IS_AFK)) {
            // Remove from team and set IS_AFK to false
            board.removePlayerFromTeam(player.getScoreboardName(), team);
            FdaUtil.set(player, PlayerVars.IS_AFK, false);

            // Reset command cooldown
            FdaUtil.set(player, PlayerVars.AFK_COOLDOWN, Config.afkCommandCooldown.get());

            // If enabled, despawn
            if (Config.despawnMonstersPostAfk.get()) despawnNearbyMonsters(player);

            CommandUtil.sendOk(source, "You are no longer AFK", false);

        } else {
            // Set position at which command was executed at
            // Add to team and set IS_AFK to true
            FdaUtil.set(player, PlayerVars.AFK_POS, player.position());

            board.addPlayerToTeam(player.getScoreboardName(), team);
            FdaUtil.set(player, PlayerVars.IS_AFK, true);

            CommandUtil.sendOk(source, "You are now AFK", false);
        }

        return 1;
    }

    static void despawnNearbyMonsters(ServerPlayer player) {
        var level = player.level();
        if (level.isBrightOutside()) return;

        EntityUtil.executeForNearby(player, Monster.class, 8, monster -> {
            if (monster.hasCustomName() || monster.isPassenger() || monster.isVehicle()) return;
            monster.discard();
        });
    }
}