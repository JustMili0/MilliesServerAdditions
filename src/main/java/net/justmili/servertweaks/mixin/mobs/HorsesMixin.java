package net.justmili.servertweaks.mixin.mobs;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.justmili.servertweaks.content.abilities.Abilities;
import net.justmili.servertweaks.content.abilities.core.AbilitiesFileUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RunAroundLikeCrazyGoal.class)
public abstract class HorsesMixin extends Goal {

    @Definition(id = "horse", field = "Lnet/minecraft/world/entity/ai/goal/RunAroundLikeCrazyGoal;horse:Lnet/minecraft/world/entity/animal/equine/AbstractHorse;")
    @Definition(id = "getRandom", method = "Lnet/minecraft/world/entity/animal/equine/AbstractHorse;getRandom()Lnet/minecraft/util/RandomSource;")
    @Definition(id = "nextInt", method = "Lnet/minecraft/util/RandomSource;nextInt(I)I")
    @Expression("this.horse.getRandom().nextInt(?) < ?")
    @ModifyExpressionValue(method = "tick", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean alwaysTameIfCON(boolean original, @Local Entity passenger) {
        // Covers anything that extends AbstractHorse
        // Horses (all), Donkeys, Mules, Llamas
        if (!(passenger instanceof ServerPlayer serverPlayer)) return original;
        return AbilitiesFileUtil.has(serverPlayer, Abilities.CHILD_OF_NATURE) || original;
    }
}