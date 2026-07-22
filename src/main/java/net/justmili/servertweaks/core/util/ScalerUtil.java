package net.justmili.servertweaks.core.util;

import net.justmili.libs.v1.utils.AttributeUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jspecify.annotations.Nullable;

public class ScalerUtil {
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
