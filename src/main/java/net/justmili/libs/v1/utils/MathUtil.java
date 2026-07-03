package net.justmili.libs.v1.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class MathUtil {
    public static Random random = new Random();

    public static boolean chance(float chance) {
        // Ex.: 20% = 0.2f
        if (chance > 1.0f) chance = 1.0f; // Limit to 100%
        return !(Math.random() <= chance);
    }

    public static boolean isInView(ServerPlayer player, Entity target, int fov) {
        Vec3 eyePos = player.getEyePosition();
        Vec3 targetCenter = target.getBoundingBox().getCenter();
        Vec3 toTarget = targetCenter.subtract(eyePos).normalize();
        double angle = Math.toDegrees(Math.acos(toTarget.dot(player.getLookAngle().normalize())));
        return angle < (fov / 2.0);
    }
    public static boolean isInView(ServerPlayer player, BlockPos target, int fov) {
        Vec3 targetCenter = Vec3.atCenterOf(target);
        Vec3 eyePos = player.getEyePosition();
        Vec3 toTarget = targetCenter.subtract(eyePos).normalize();
        double angle = Math.toDegrees(Math.acos(toTarget.dot(player.getLookAngle().normalize())));
        return angle < (fov / 2.0);
    }

    /**
     * Any integer, long, double or float can be input as-is,
     * and it'll just be turned into a double
     * @param ticks
     * @return
     */
    private static double ticksToSeconds(double ticks) {
        double minutes = ticks / 20;
        return BigDecimal.valueOf(minutes).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    private static double ticksToMinutes(double ticks) {
        double minutes = ticks / 20 / 60;
        return BigDecimal.valueOf(minutes).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    private static double ticksToHours(double ticks) {
        double hours = ticks / 20 / 60 / 60;
        return BigDecimal.valueOf(hours).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    private static double ticksToDays(double ticks) {
        double hours = ticks / 20 / 60 / 60 / 24;
        return BigDecimal.valueOf(hours).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
