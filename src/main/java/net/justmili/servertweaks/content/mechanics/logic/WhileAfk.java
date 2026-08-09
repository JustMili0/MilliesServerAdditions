package net.justmili.servertweaks.content.mechanics.logic;

import net.justmili.libs.v1.utils.FdaUtil;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.variables.PlayerAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class WhileAfk {
    public static boolean onEntityHurt(LivingEntity entity, DamageSource source, float value) {
        if (source.getEntity() instanceof ServerPlayer attacker
            && FdaUtil.getBool(attacker, PlayerAttachments.IS_AFK)) return false;
        if (entity instanceof ServerPlayer victim
            && FdaUtil.getBool(victim, PlayerAttachments.IS_AFK)) return false;

        return true;
    }

    public static void onPlayerTick(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        // Teleport the player to saved position to prevent movement
        if (FdaUtil.getBool(serverPlayer, PlayerAttachments.IS_AFK)) {
            var pos = FdaUtil.get(serverPlayer, PlayerAttachments.AFK_POS);

            player.setDeltaMovement(Vec3.ZERO);
            player.resetFallDistance();

            if (player.distanceToSqr(pos.x(), pos.y(), pos.z()) > 0.001f)
                serverPlayer.connection.teleport(pos.x(), pos.y(), pos.z(), serverPlayer.getYRot(), serverPlayer.getXRot());
        }

        //Set/reset command timer
        if (!FdaUtil.getBool(serverPlayer, PlayerAttachments.IS_AFK) && Config.afkCommandCooldown.get() != 0) {
            int cooldown = FdaUtil.getInt(serverPlayer, PlayerAttachments.AFK_COOLDOWN);
            if (cooldown > 0) {
                FdaUtil.set(serverPlayer, PlayerAttachments.AFK_COOLDOWN, cooldown - 1);
            }
        }
    }
}
