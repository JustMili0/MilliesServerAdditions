package net.justmili.servertweaks.mixin.combat;

import net.justmili.servertweaks.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.CombatTracker;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CombatTracker.class)
public class CombatTrackerMixin {
    @Redirect(method = "getDeathMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDisplayName()Lnet/minecraft/network/chat/Component;"))
    public Component obfDeathMessage(LivingEntity entity) {
        return Util.shouldObfuscateDeathMessage(entity);
    }

    @Redirect(method = "getFallMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDisplayName()Lnet/minecraft/network/chat/Component;"))
    public Component obfFallDeathMessage(LivingEntity entity) {
        return Util.shouldObfuscateDeathMessage(entity);
    }

    @Redirect(method = "getFallMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatTracker;getDisplayName(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/network/chat/Component;"))
    public Component obfFallDeathMessage(Entity entity) {
        return Util.shouldObfuscateDeathMessage(entity);
    }

    @Redirect(method = "getMessageForAssistedFall", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDisplayName()Lnet/minecraft/network/chat/Component;"))
    public Component obfAssistedFallDeathMessage(LivingEntity entity) {
        return Util.shouldObfuscateDeathMessage(entity);
    }
}