package net.justmili.servertweaks.mixin;

import net.justmili.servertweaks.config.Config;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class AllowMixingEnchants {
    @Inject(method = "areCompatible", at = @At("HEAD"), cancellable = true)
    private static void makeCompatible(Holder<Enchantment> enchantment, Holder<Enchantment> other, CallbackInfoReturnable<Boolean> cir) {
        if (!Config.allowMixEnchantments.get()) return;

        cir.setReturnValue(true);
    }
}
