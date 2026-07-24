package net.justmili.libs.v1.utils;

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

    /**
     * Any integer, long, double or float can be input as-is,
     * and it'll just be turned into a double
     * @param ticks
     * @return
     */
    public static double ticksToSeconds(long ticks) {
        long seconds = ticks / 20;
        return roundHalfUp(seconds, 2);
    }
    public static double ticksToMinutes(long ticks) {
        long minutes = ticks / 20 / 60;
        return roundHalfUp(minutes, 2);
    }
    public static double ticksToHours(long ticks) {
        long hours = ticks / 20 / 60 / 60;
        return roundHalfUp(hours, 2);
    }
    public static double ticksToDays(long ticks) {
        long hours = ticks / 20 / 60 / 60 / 24;
        return roundHalfUp(hours, 2);
    }

    public static float roundHalfUp(double value, int pastDecimal) {
        return BigDecimal.valueOf(value).setScale(pastDecimal, RoundingMode.HALF_UP).floatValue();
    }
    public static float roundHalfDown(double value, int pastDecimal) {
        return BigDecimal.valueOf(value).setScale(pastDecimal, RoundingMode.HALF_DOWN).floatValue();
    }
    public static float roundHalfEven(double value, int pastDecimal) {
        return BigDecimal.valueOf(value).setScale(pastDecimal, RoundingMode.HALF_EVEN).floatValue();
    }
    public static float roundUp(double value, int pastDecimal) {
        return BigDecimal.valueOf(value).setScale(pastDecimal, RoundingMode.UP).floatValue();
    }
    public static float roundDown(double value, int pastDecimal) {
        return BigDecimal.valueOf(value).setScale(pastDecimal, RoundingMode.DOWN).floatValue();
    }
    public static int roundInt(double value) {
        return Math.toIntExact(Math.round(value));
    }
}
