package net.justmili.servertweaks.mixin.entity.animal;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.justmili.servertweaks.content.abilities.Abilities;
import net.justmili.servertweaks.content.abilities.core.AbilityProfilesUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Parrot.class)
public class ParrotMixin {

    @Definition(id = "nextInt", method = "Lnet/minecraft/util/RandomSource;nextInt(I)I")
    @Expression("?.nextInt(?) == ?")
    @ModifyExpressionValue(method = "mobInteract", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean servertweaks$alwaysTame(boolean original, @Local(argsOnly = true) Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return original;
        return AbilityProfilesUtil.has(serverPlayer, Abilities.CHILD_OF_NATURE) || original;
    }
}
