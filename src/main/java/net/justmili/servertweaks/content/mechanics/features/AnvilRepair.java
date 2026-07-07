package net.justmili.servertweaks.content.mechanics.features;

import net.justmili.servertweaks.config.Config;
import net.minecraft.core.BlockPos;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnvilRepair {
    private static final Map<UUID, Integer> repairAttemptsIngot = new HashMap<>(), repairAttemptsBlock = new HashMap<>();
    private static final Map<UUID, BlockPos> anvilPosition = new HashMap<>();

    public static InteractionResult onUseBlock(Player interacting, Level level, InteractionHand hand, BlockHitResult blockHitResult) {
        if (!Config.enableAnvilRepair.get()) return InteractionResult.PASS;
        if (!(interacting instanceof ServerPlayer player)) return InteractionResult.PASS;
        if (!player.isShiftKeyDown()) return InteractionResult.PASS;

        // Block checks
        var blockState = level.getBlockState(blockHitResult.getBlockPos());
        if (!blockState.is(BlockTags.ANVIL)) return InteractionResult.PASS;
        Block block = blockState.getBlock();
        if (block == Blocks.ANVIL) return InteractionResult.PASS;

        // Item checks
        ItemStack stack = player.getItemInHand(hand);
        boolean hasIngot = stack.is(Items.IRON_INGOT), hasBlock = stack.is(Items.IRON_BLOCK);
        if (!hasIngot && !hasBlock) return InteractionResult.PASS;

        // Roll chances
        double chance;
        if (block == Blocks.CHIPPED_ANVIL) {
            chance = hasBlock ? 1.0 : 0.33;
        } else if (block == Blocks.DAMAGED_ANVIL) {
            chance = hasBlock ? 0.80 : 0.25;
        } else {
            return InteractionResult.PASS;
        }

        UUID uuid = player.getUUID();
        var attempts = hasBlock ? repairAttemptsBlock : repairAttemptsIngot;
        var currentPos = blockHitResult.getBlockPos();

        // Clear map data if player moved to a different anvil
        if (!currentPos.equals(anvilPosition.get(uuid))) {
            repairAttemptsIngot.remove(uuid);
            repairAttemptsBlock.remove(uuid);
            anvilPosition.put(uuid, currentPos);
        }

        // Shrink used repair item
        if (!player.isCreative()) {
            level.levelEvent(2001, blockHitResult.getBlockPos(), Block.getId(blockState));
            stack.shrink(1);
        }

        if (attempts.getOrDefault(uuid, 0) >= 2 || Math.random() <= chance) {
            // Clear map data
            repairAttemptsIngot.remove(uuid);
            repairAttemptsBlock.remove(uuid);
            anvilPosition.remove(uuid);

            // Set new block
            Block repairedBlock = (block == Blocks.DAMAGED_ANVIL) ? Blocks.CHIPPED_ANVIL : Blocks.ANVIL;
            level.setBlock(
                blockHitResult.getBlockPos(),
                repairedBlock.defaultBlockState().setValue(
                    BlockStateProperties.HORIZONTAL_FACING,
                    blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)
                ),
                Block.UPDATE_ALL
            );
            // Play particles and sound
            level.levelEvent(3005, blockHitResult.getBlockPos(), 0);
            level.playSound(null, blockHitResult.getBlockPos(), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.4f, 1.8f);
        } else {
            attempts.merge(uuid, 1, Integer::sum);
        }

        return InteractionResult.SUCCESS;
    }
}