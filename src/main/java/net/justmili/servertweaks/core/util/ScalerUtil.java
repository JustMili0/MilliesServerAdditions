package net.justmili.servertweaks.core.util;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.justmili.libs.v1.utils.AttributeUtil;
import net.justmili.libs.v1.utils.FdaApiUtil;
import net.justmili.servertweaks.core.variables.PlayerAttachments;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.scores.ScoreHolder;
import org.jspecify.annotations.Nullable;

public class ScalerUtil {

    //Converts old scoreboard scuff to fresh variables (purely for my own Minecraft server)
    public static void convertScoreToVar(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
        ServerPlayer player = handler.player;
        if (FdaApiUtil.getBoolValue(player, PlayerAttachments.SCALE_LOCKED)) return;

        var board = player.level().getServer().getScoreboard();
        var objective = board.getObjective("scaleLocked");
        if (objective == null) return;

        var holder = ScoreHolder.forNameOnly(player.getScoreboardName());
        var score = board.getOrCreatePlayerScore(holder, objective);

        // Migrate value
        if (score.get() > 0) {
            FdaApiUtil.setBoolValue(player, PlayerAttachments.SCALE_LOCKED, true);
            board.resetSinglePlayerScore(holder, objective);
        }
    }

    //Applies calculated scale
    public static void applyScaleToPlayer(ServerPlayer player, double scale) {
        double min = 0.1, max = 5.0;
        if (Double.isNaN(scale) || scale <= 0.0) scale = 1.0;
        scale = Math.clamp(scale, min, max);

        if (getScale(player) != null) {
            setScale(player, scale);
            player.refreshDimensions();
        }
    }

    public static @Nullable AttributeInstance getScale(ServerPlayer player) {
        return AttributeUtil.getAttribute(player, Attributes.SCALE);
    }

    public static void setScale(ServerPlayer player, double scale) {
        getScale(player).setBaseValue(scale);
    }
}
