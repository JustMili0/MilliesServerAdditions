package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.justmili.libs.v1.utils.CommandUtil;
import net.justmili.libs.v1.utils.FdaUtil;
import net.justmili.libs.v1.utils.MathUtil;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.scores.Team;

public class Afk {
    private static final String AFK_PLAYERS = "afk_players";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("afk")
            .executes(context -> {
                var source = context.getSource();

                var level = source.getLevel();
                var player = context.getSource().getPlayerOrException();

                int cooldown = FdaUtil.getInt(player, PlayerVars.AFK_COOLDOWN);
                if (!FdaUtil.getBool(player, PlayerVars.IS_AFK) && Config.afkCommandCooldown.get() != 0 && cooldown > 0) {
                    CommandUtil.sendFail(source, "You must wait " + MathUtil.ticksToSeconds(cooldown) + "s before using this command again");
                    return 0;
                }

                var scoreboard = level.getScoreboard();
                var team = scoreboard.getPlayerTeam(AFK_PLAYERS);

                // Create team if it doesn't exist
                if (team == null) {
                    team = scoreboard.addPlayerTeam(AFK_PLAYERS);
                    team.setNameTagVisibility(Team.Visibility.ALWAYS);
                    team.setPlayerPrefix(Component.literal("[AFK] "));
                    team.setColor(ChatFormatting.GRAY);
                }

                if (FdaUtil.getBool(player, PlayerVars.IS_AFK)) {
                    // Remove from team and set IS_AFK to false
                    scoreboard.removePlayerFromTeam(player.getScoreboardName(), team);
                    FdaUtil.set(player, PlayerVars.IS_AFK, false);

                    // Reset command cooldown
                    FdaUtil.set(player, PlayerVars.AFK_COOLDOWN, Config.afkCommandCooldown.get());

                    // If enabled, despawn
                    if (Config.despawnMonstersPostAfk.get()) despawnNearbyMonsters(player);

                    CommandUtil.sendOk(source, "You are no longer AFK");

                } else {
                    // Set position at which command was executed at
                    // Add to team and set IS_AFK to true
                    FdaUtil.set(player, PlayerVars.AFK_POS, player.blockPosition());

                    scoreboard.addPlayerToTeam(player.getScoreboardName(), team);
                    FdaUtil.set(player, PlayerVars.IS_AFK, true);

                    CommandUtil.sendOk(source, "You are now AFK");
                }

                return 1;
            })
        );
    }

    private static void despawnNearbyMonsters(ServerPlayer player) {
        var level = player.level();
        if (level.isBrightOutside()) return;
        var box = new AABB(
            player.getX() - 8, player.getY() - 8, player.getZ() - 8,
            player.getX() + 8, player.getY() + 8, player.getZ() + 8
        );

        for (Monster monster : level.getEntitiesOfClass(Monster.class, box)) {
            if (monster.hasCustomName() || monster.isPassenger() || monster.isVehicle()) continue;

            monster.discard();
        }
    }
}