package net.justmili.servertweaks.mixin.entity;

import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.abilities.Abilities;
import net.justmili.servertweaks.content.abilities.core.AbilityProfilesUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    // TOUGH
    @Inject(method = "knockback(DDDLnet/minecraft/world/damagesource/DamageSource;FZ)V", at = @At("HEAD"), cancellable = true)
    private void servertweaks$knockback(double power, double xd, double zd, DamageSource source, float damage, boolean comesFromEffect, CallbackInfo ci) {
        if (!(Config.playerAbilities.get())) return;
        if (!((LivingEntity) (Object) this instanceof ServerPlayer player)) return;
        if (AbilityProfilesUtil.has(player, Abilities.TOUGH)) ci.cancel();
    }

    // CANT_BREATHE_AIR
    @Inject(method = "increaseAirSupply", at = @At("HEAD"), cancellable = true)
    private void servertweaks$increaseAirSupply(int currentAir, CallbackInfoReturnable<Integer> cir) {
        if (!(Config.playerAbilities.get())) return;
        if (!((LivingEntity) (Object) this instanceof ServerPlayer player)) return;
        if (!AbilityProfilesUtil.has(player, Abilities.CANT_BREATHE_AIR)) return;
        if (!player.isInWater()) cir.setReturnValue(currentAir);
    }
}