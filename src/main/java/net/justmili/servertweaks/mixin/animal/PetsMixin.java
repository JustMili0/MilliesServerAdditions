package net.justmili.servertweaks.mixin.animal;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.justmili.servertweaks.content.abilities.Abilities;
import net.justmili.servertweaks.content.abilities.core.AbilitiesFileUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({Cat.class, Wolf.class, AbstractNautilus.class})
public abstract class PetsMixin extends TamableAnimal {
    protected PetsMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Definition(id = "nextInt", method = "Lnet/minecraft/util/RandomSource;nextInt(I)I")
    @Expression("?.nextInt(?) == ?")
    @ModifyExpressionValue(method = "tryToTame", at = @At("MIXINEXTRAS:EXPRESSION"))
    private boolean alwaysTameIfCON(boolean original, @Local(argsOnly = true) Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return original;
        return AbilitiesFileUtil.has(serverPlayer, Abilities.CHILD_OF_NATURE) || original;
    }
}