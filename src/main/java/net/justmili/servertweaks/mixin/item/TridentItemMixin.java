package net.justmili.servertweaks.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TridentItem.class)
public class TridentItemMixin {

    @ModifyExpressionValue(method = "releaseUsing", at = @At(value = "CONSTANT", args = "intValue=10"))
    private int servertweaks$fasterRechargeWithRiptide(int original, ItemStack itemStack, Level level, LivingEntity entity, int remainingTime) {
        if (entity instanceof Player player && EnchantmentHelper.getTridentSpinAttackStrength(itemStack, player) > 0.0F) return 3;
        return original;
    }
}