package net.justmili.servertweaks.mixin.item;

import net.justmili.servertweaks.content.abilities.AbilityEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void servertweaks$handleDietItemInteraction(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        // Ironically, out of all the events available in *both* Fabric and NeoForge,
        // none of them handle this specific use case.
        InteractionResult result = AbilityEvents.handleDietItemCall(player, level, hand);
        if (result != InteractionResult.PASS) cir.setReturnValue(result);
    }
}
