package net.justmili.servertweaks.mixin;

import net.justmili.servertweaks.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DamageSource.class)
public class DamageSourceMixin {

    @Redirect(method = "getLocalizedDeathMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getDisplayName()Lnet/minecraft/network/chat/Component;"))
    public Component hideDeathMessages(LivingEntity entity) {
        return Util.shouldObfuscateDeathMessage(entity);
    }

    @Redirect(method = "getLocalizedDeathMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getDisplayName()Lnet/minecraft/network/chat/Component;"))
    private Component hideKillMessages(Entity entity) {
        return Util.shouldObfuscateDeathMessage(entity);
    }
}