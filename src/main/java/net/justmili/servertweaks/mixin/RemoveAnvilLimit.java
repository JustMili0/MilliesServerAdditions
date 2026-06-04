package net.justmili.servertweaks.mixin;

import net.justmili.servertweaks.config.Config;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.DataSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class RemoveAnvilLimit {
    // Dev note: I was fed up with the limit
    @Shadow
    private DataSlot cost;

    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 40))
    private int removeTooExpensive(int i) {
        if (!Config.removeAnvilLimit.get()) return i;
        return Integer.MAX_VALUE;
    }

    @Inject(method = "createResult", at = @At("RETURN"))
    private void clampCost(CallbackInfo ci) {
        if (!Config.removeAnvilLimit.get()) return;
        if (this.cost.get() > 39) this.cost.set(39);
    }
}
