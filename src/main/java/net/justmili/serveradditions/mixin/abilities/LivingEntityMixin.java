package net.justmili.serveradditions.mixin.abilities;

import net.justmili.serveradditions.config.Config;
import net.justmili.serveradditions.content.abilities.DataManager;
import net.justmili.serveradditions.content.abilities.Events;
import net.justmili.serveradditions.content.abilities.registries.AbilityRegistry;
import net.justmili.serveradditions.mixin.accessors.LivingEntityAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    // TOUGH
    @Inject(method = "knockback", at = @At("HEAD"), cancellable = true)
    private void knockback(double strength, double x, double z, CallbackInfo ci) {
        if (!(Config.playerAbilities.get())) return;
        LivingEntity self = (LivingEntity) (Object) this;
        if (!(self instanceof ServerPlayer player)) return;
        if (DataManager.has(player, AbilityRegistry.TOUGH)) ci.cancel();
    }

    // CANT_BREATHE_AIR
    @Inject(method = "increaseAirSupply", at = @At("HEAD"), cancellable = true)
    private void increaseAirSupply(int currentAir, CallbackInfoReturnable<Integer> cir) {
        if (!(Config.playerAbilities.get())) return;
        if (!((LivingEntity) (Object) this instanceof ServerPlayer player)) return;
        if (!DataManager.has(player, AbilityRegistry.CANT_BREATHE_AIR)) return;
        if (!player.isInWater()) cir.setReturnValue(currentAir);
    }

    // CLIMBS_WALLS
    @Inject(method = "onClimbable", at = @At("RETURN"), cancellable = true)
    private void onClimbable(CallbackInfoReturnable<Boolean> cir) {
        if (!(Config.playerAbilities.get())) return;
        if (!((Object) this instanceof ServerPlayer player)) return;
        if (cir.getReturnValue()) return;
        if (Events.shouldClimb(player)) {
            ((LivingEntityAccessor) this).setLastClimbablePos(Optional.of(player.blockPosition()));
            cir.setReturnValue(true);
        }
    }
}