package net.justmili.servertweaks.util;

import net.justmili.servertweaks.config.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class Util {
    // generic util class for things I don't know where to put

    public static Component shouldObfuscateDeathMessage(Entity entity) {
        if (entity == null) return Component.empty();
        if (!Config.obfInvisDeathMessages.get()) return entity.getDisplayName();
        if (!(entity instanceof Player player)) return entity.getDisplayName();

        if (player.isInvisible()) return Component.literal("Unknown").withStyle(ChatFormatting.OBFUSCATED);
        return player.getDisplayName();
    }

    public static <T extends Mob> void scareMob(ServerPlayer player, T mob, double speedModifier) {
        mob.setTarget(null);
        mob.getNavigation().moveTo(mob.getX() + (mob.getX() - player.getX()), mob.getY() + 8, mob.getZ() + (mob.getZ() - player.getZ()), speedModifier);
    }
}
