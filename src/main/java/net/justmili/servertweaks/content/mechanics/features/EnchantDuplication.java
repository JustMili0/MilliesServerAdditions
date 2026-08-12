package net.justmili.servertweaks.content.mechanics.features;

import net.justmili.libs.v1.utils.common.MathUtil;
import net.justmili.servertweaks.config.Config;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Map;

public class EnchantDuplication {

    public static void onUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (!Config.enableEnchantDuplication.get()) return;
        if (!level.getBlockState(hitResult.getBlockPos()).is(Blocks.ENCHANTING_TABLE)) return;

        if (!hand.equals(InteractionHand.MAIN_HAND)) return; // Prevent call on both hands
        if (player.experienceLevel <= 0) return;

        var mainHand = player.getMainHandItem();
        var offhand = player.getOffhandItem();
        boolean isShifting = player.isShiftKeyDown();

        if (isShifting && offhand.getItem() == Items.ENCHANTED_BOOK && mainHand.getItem() == Items.BOOK) {
            var storedEnchantments = offhand.get(DataComponents.STORED_ENCHANTMENTS);
            if (storedEnchantments == null || storedEnchantments.size() != 1) return;

            Map.Entry<Holder<Enchantment>, Integer> storedEnchantment = storedEnchantments.entrySet().iterator().next();
            var enchantment = storedEnchantment.getKey();

            float requiredExp = switch (getRarity(enchantment)) {
                case "COMMON" -> 0.1f;
                case "UNCOMMON" -> 0.5f;
                case "RARE" -> 1.2f;
                case "VERY_RARE" -> 1.5f;
                case "TREASURE" -> 2f;
                default -> 3;
            };
            int multiplier = (storedEnchantment.getValue() * enchantment.value().getMaxLevel()) / 4;
            int expCost = player.experienceLevel - MathUtil.roundInt(requiredExp + multiplier);
            if (expCost <= 0) return;

            player.experienceLevel = expCost;
            player.giveExperiencePoints(1); // Update clients about exp changes

            var duplicatedBook = offhand.copy();
            mainHand.shrink(1);
            duplicatedBook.remove(DataComponents.REPAIR_COST);
            duplicatedBook.setCount(1);

            if (!player.getInventory().add(duplicatedBook)) {
                player.drop(duplicatedBook, false);
            }
            level.playSound(null, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.6F, 1.0F);
            player.getInventory().setChanged(); // Update clients about inv changes
        }

    }

    private static String getRarity(Holder<Enchantment> enchantmentHolder) {
        if (!enchantmentHolder.is(EnchantmentTags.NON_TREASURE)) return "TREASURE";

        int weight = enchantmentHolder.value().definition().weight();
        if (weight >= 10) return "COMMON";
        if (weight >= 5) return "UNCOMMON";
        if (weight >= 2) return "RARE";
        return "VERY_RARE";
    }
}