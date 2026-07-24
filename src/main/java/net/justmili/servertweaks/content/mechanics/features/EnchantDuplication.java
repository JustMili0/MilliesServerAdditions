package net.justmili.servertweaks.content.mechanics.features;

import net.justmili.libs.v1.utils.MathUtil;
import net.justmili.servertweaks.config.Config;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Map;

public class EnchantDuplication {

    public static void onUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (!Config.enableEnchantDuplication.get()) return;
        if (!level.getBlockState(hitResult.getBlockPos()).is(Blocks.ENCHANTING_TABLE)) return;
        if (!hand.equals(InteractionHand.MAIN_HAND)) return; // Prevent call on both hands
        if (player.experienceLevel == 0) return;

        ItemStack mainHand = player.getMainHandItem(), offhand = player.getOffhandItem();
        boolean isShifting = player.isShiftKeyDown();

        if (isShifting && offhand.getItem() == Items.ENCHANTED_BOOK && mainHand.getItem() == Items.BOOK) {
            ItemEnchantments storedEnchantments = offhand.get(DataComponents.STORED_ENCHANTMENTS);
            if (storedEnchantments == null || storedEnchantments.size() != 1) return;

            Map.Entry<Holder<Enchantment>, Integer> enchantEntry = storedEnchantments.entrySet().iterator().next();
            Holder<Enchantment> enchantHolder = enchantEntry.getKey();
            int enchantLevel = enchantEntry.getValue();
            Enchantment enchantment = enchantHolder.value();
            int enchantMaxLevel = enchantment.getMaxLevel();

            int multiplier = (enchantMaxLevel * enchantLevel) / 4;
            int requiredExp = switch (getRarity(enchantHolder)) {
                case "COMMON" -> MathUtil.roundInt(0.1f + multiplier);
                case "UNCOMMON" -> MathUtil.roundInt(0.5f + multiplier);
                case "RARE" -> MathUtil.roundInt(1.2f + multiplier);
                case "VERY_RARE" -> MathUtil.roundInt(1.5f + multiplier);
                case "TREASURE" -> MathUtil.roundInt(2f + multiplier);
                default -> 3;
            };

            int expCost = player.experienceLevel - requiredExp;
            if (expCost <= 0) return;
            player.experienceLevel = expCost;
            player.giveExperiencePoints(1); // Update clients about exp changes

            mainHand.shrink(1);
            ItemStack duplicatedBook = offhand.copy();
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