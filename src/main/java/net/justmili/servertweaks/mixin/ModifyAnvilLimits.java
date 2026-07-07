package net.justmili.servertweaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.registries.TagRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class ModifyAnvilLimits extends ItemCombinerMenu {
    @Final @Shadow
    private DataSlot cost;

    public ModifyAnvilLimits(@Nullable MenuType<?> menuType, int containerId, Inventory inventory,
                             ContainerLevelAccess access, ItemCombinerMenuSlotDefinition itemInputSlots) {
        super(menuType, containerId, inventory, access, itemInputSlots);
    }

    // Allow over-vanilla enchant levels
    @WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;getMaxLevel()I"))
    private int modifyMaxLevels(Enchantment enchantment, Operation<Integer> original) {
        int maxLevel = original.call(enchantment);
        if (!Config.enableHigherEnchants.get()) return maxLevel;

        var lookup = this.player.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var enchantmentHolder = lookup.wrapAsHolder(enchantment);

        if (enchantmentHolder.is(TagRegistry.ENCHANT_BOOST_1)) return maxLevel + 1;
        if (enchantmentHolder.is(TagRegistry.ENCHANT_BOOST_2)) return maxLevel + 2;
        if (enchantmentHolder.is(TagRegistry.ENCHANT_BOOST_3)) return maxLevel + 3;
        if (enchantmentHolder.is(TagRegistry.ENCHANT_BOOST_4)) return maxLevel + 4;
        if (enchantmentHolder.is(TagRegistry.ENCHANT_BOOST_5)) return maxLevel + 5;

        return maxLevel;
    }

    // Remove anvil "too expensive"
    @Inject(method = "createResult", at = {@At("RETURN")})
    private void clampResultCost(CallbackInfo ci) {
        if (!Config.disableAnvilLimit.get()) return;
        if (this.cost.get() > 39) this.cost.set(39);
    }

    @ModifyConstant(method = "createResult", constant = {@Constant(intValue = 40)})
    private int modifyCostLimit(int i) {
        if (!Config.disableAnvilLimit.get()) return i;
        return Integer.MAX_VALUE;
    }
}
