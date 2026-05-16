package net.justmili.serveradditions.mechanics.features;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public class AnvilRepair {
    public static InteractionResult onUseBlock(Player interacting, Level level, InteractionHand hand, BlockHitResult blockHitResult) {
        if (!(interacting instanceof ServerPlayer player)) return InteractionResult.PASS;
        if (!player.isShiftKeyDown()) return InteractionResult.PASS;

        var blockState = level.getBlockState(blockHitResult.getBlockPos());
        if (!blockState.is(BlockTags.ANVIL)) return InteractionResult.PASS;
        Block block = blockState.getBlock();
        if (block == Blocks.ANVIL) return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);
        boolean hasIngot = stack.is(Items.IRON_INGOT);
        boolean hasBlock = stack.is(Items.IRON_BLOCK);
        if (!hasIngot && !hasBlock) return InteractionResult.PASS;

        double chance;
        if (block == Blocks.CHIPPED_ANVIL) {
            chance = hasBlock ? 1.0 : 0.33;
        } else if (block == Blocks.DAMAGED_ANVIL) {
            chance = hasBlock ? 0.80 : 0.25;
        } else {
            return InteractionResult.PASS;
        }

        if (!player.isCreative()) {
            level.levelEvent(2001, blockHitResult.getBlockPos(), Block.getId(blockState));
            stack.shrink(1);
        }

        if (Math.random() <= chance) {
            Block repairedBlock = (block == Blocks.DAMAGED_ANVIL) ? Blocks.CHIPPED_ANVIL : Blocks.ANVIL;
            level.setBlock(
                blockHitResult.getBlockPos(),
                repairedBlock.defaultBlockState().setValue(
                    BlockStateProperties.HORIZONTAL_FACING,
                    blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)
                ),
                Block.UPDATE_ALL
            );
            level.levelEvent(3005, blockHitResult.getBlockPos(), 0);
            level.playSound(null, blockHitResult.getBlockPos(), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.4f, 1.8f);
        }

        return InteractionResult.SUCCESS;
    }
}