package net.justmili.servertweaks.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.justmili.corelibs.v1.utils.common.EntityUtil;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.abilities.Abilities;
import net.justmili.servertweaks.content.abilities.Debuffs;
import net.justmili.servertweaks.content.abilities.core.AbilityProfilesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    private Optional<BlockPos> lastClimbablePos;

    // TOUGH
    @Inject(method = "knockback(DDDLnet/minecraft/world/damagesource/DamageSource;FZ)V", at = @At("HEAD"), cancellable = true)
    private void servertweaks$knockback(double power, double xd, double zd, DamageSource source, float damage, boolean comesFromEffect, CallbackInfo ci) {
        if (!(Config.playerAbilities.get())) return;
        if (!((LivingEntity) (Object) this instanceof Player player)) return;
        if (AbilityProfilesUtil.has(player, Abilities.TOUGH)) ci.cancel();
    }

    // CANT_BREATHE_AIR
    @Inject(method = "increaseAirSupply", at = @At("HEAD"), cancellable = true)
    private void servertweaks$increaseAirSupply(int currentSupply, CallbackInfoReturnable<Integer> cir) {
        if (!(Config.playerAbilities.get())) return;
        if (!((LivingEntity) (Object) this instanceof Player player)) return;
        if (!AbilityProfilesUtil.has(player, Debuffs.CANT_BREATHE_AIR)) return;
        if (!player.isInWater()) cir.setReturnValue(currentSupply);
    }

    // CANT_SWIM
    @ModifyReturnValue(method = "getFluidFallingAdjustedMovement", at = @At("RETURN"))
    private Vec3 servertweaks$cantSwimNoRise(Vec3 original, double baseGravity, boolean isFalling, Vec3 movement) {
        if (!Config.playerAbilities.get()) return original;
        if (!((LivingEntity) (Object) this instanceof Player player)) return original;

        var gm = player.gameMode();
        // Issue: Works only in singleplayer, because on servers even with client having the mod installed it doesn't know about the abilities
        // Solution: Fabric Data Attachments and rewrite of AbilityProfiles and AbilityProfilesUtil
        if (!AbilityProfilesUtil.has(player, Debuffs.CANT_SWIM) || !player.isInWater() || (gm != null && !gm.isSurvival())) return original;

        var level = player.level();
        var pos = player.blockPosition();
        if (!level.getFluidState(pos.above()).is(FluidTags.WATER) && level.getFluidState(pos.below()).isEmpty()) return original;

        return new Vec3(original.x, Math.min(original.y, -baseGravity * 0.5), original.z);
    }

    // CLIMBS_WALLS
    @ModifyReturnValue(method = "onClimbable", at = @At("RETURN"))
    public boolean doSpiderClimbing(boolean original) {
        if (original) return true;
        if (!((LivingEntity) (Object) this instanceof Player player)) return original;
        if (!AbilityProfilesUtil.has(player, Abilities.CLIMBS_WALLS)) return original;
        if (!EntityUtil.isTouchingWall(player) || player.isInWater()) return original;

        this.lastClimbablePos = Optional.of(player.blockPosition());
        return true;
    }
}