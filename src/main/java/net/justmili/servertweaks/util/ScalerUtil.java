package net.justmili.servertweaks.util;

import net.justmili.libs.v1.utils.AttributeUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jspecify.annotations.Nullable;

public class ScalerUtil {
    // Applies scale, claps if over min or max limit
    public static void applyScaleToPlayer(ServerPlayer player, float scale) {
        float min = 0.1f, max = 5;
        if (Double.isNaN(scale) || scale <= 0f) scale = 1f;
        scale = Math.clamp(scale, min, max);

        if (getScale(player) != null) {
            setScale(player, scale);
            player.refreshDimensions();
        }
    }

    public static @Nullable AttributeInstance getScale(ServerPlayer player) {
        return AttributeUtil.getAttribute(player, Attributes.SCALE);
    }

    public static void setScale(ServerPlayer player, float scale) {
        getScale(player).setBaseValue(scale);
    }
}