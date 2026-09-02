package net.justmili.servertweaks.mixin.entity;

import net.justmili.servertweaks.content.abilities.Abilities;
import net.justmili.servertweaks.content.abilities.core.AbilityProfilesUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSkeleton.class)
public class AbstractSkeletonMixin {

    // SCARES_SKELETONS
    @Inject(method = "performRangedAttack", at = @At("HEAD"), cancellable = true)
    private void servertweaks$scaresSkeletonBoneChill(LivingEntity target, float power, CallbackInfo ci) { // I thought I could throw in a skeleton joke, because I can
        if (target instanceof ServerPlayer player
            && player.gameMode().isSurvival()
            && AbilityProfilesUtil.has(player, Abilities.SCARES_SKELETONS)) ci.cancel();
    }
}
