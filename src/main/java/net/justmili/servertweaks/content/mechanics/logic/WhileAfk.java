package net.justmili.servertweaks.content.mechanics.logic;

import net.justmili.mlibs.v1.utils.common.EntityUtil;
import net.justmili.mlibs.v1.utils.common.FdaUtil;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class WhileAfk {
    public static boolean onEntityHurt(LivingEntity entity, DamageSource source, float value) {
        if (source.getEntity() instanceof Player attacker && FdaUtil.getBool(attacker, PlayerVars.IS_AFK)) return false;
        if (entity instanceof Player victim && FdaUtil.getBool(victim, PlayerVars.IS_AFK)) return false;

        return true;
    }

    public static void onPlayerTick(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        // Teleport the player to saved position to prevent movement
        if (FdaUtil.getBool(serverPlayer, PlayerVars.IS_AFK)) {
            var pos = FdaUtil.get(serverPlayer, PlayerVars.AFK_POS);

            player.setDeltaMovement(Vec3.ZERO);
            player.resetFallDistance();

            if (player.distanceToSqr(pos.x, pos.y, pos.z) > 0.001f) EntityUtil.teleport(player, pos);
        }

        // Set/reset command timer
        if (!FdaUtil.getBool(serverPlayer, PlayerVars.IS_AFK) && Config.afkCommandCooldown.get() != 0) {
            int cooldown = FdaUtil.getInt(serverPlayer, PlayerVars.AFK_COOLDOWN);
            if (cooldown > 0) FdaUtil.set(serverPlayer, PlayerVars.AFK_COOLDOWN, cooldown - 1);
        }
    }
}
