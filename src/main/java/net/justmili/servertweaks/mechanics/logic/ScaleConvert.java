package net.justmili.servertweaks.mechanics.logic;

import net.justmili.servertweaks.core.util.ScalerUtil;
import net.minecraft.server.level.ServerPlayer;

public class ScaleConvert {
    //Temp convert method
    public static void onServerJoined(ServerPlayer player) {
        ScalerUtil.convertScoreToVar(player);
    }
}
