package net.justmili.servertweaks.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class Util {
    // generic util class for things I don't know where to put

    public static Component shouldObfuscateDeathMessage(Entity entity) {
        if (entity == null) return Component.literal("Error with Component, Entity is null");
        if (!(entity instanceof Player player)) return entity.getDisplayName();

        if (player.isInvisible()) return Component.literal("Unknown").withStyle(ChatFormatting.OBFUSCATED);
        return player.getDisplayName();
    }
}
