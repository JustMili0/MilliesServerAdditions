package net.justmili.servertweaks.mixin.block;

import net.justmili.servertweaks.content.abilities.Abilities;
import net.justmili.servertweaks.content.abilities.core.AbilityProfilesUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WebBlock.class)
public class WebBlockMixin {

    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    private void servertweaks$weaverWebMovement(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise, CallbackInfo ci) {
        if (entity instanceof Player player && AbilityProfilesUtil.has(player, Abilities.WEAVER)) ci.cancel();
    }
}