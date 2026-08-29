package net.justmili.servertweaks.content.mechanics.features;

import net.justmili.libs.v1.utils.common.FdaUtil;
import net.justmili.libs.v1.utils.common.MathUtil;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public class AnvilRepair {
    public record RepairState(BlockPos pos, int ingotAttempts, int blockAttempts) {
        public static final RepairState NONE = new RepairState(null, 0, 0);
    }

    public static void onUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult blockHitResult) {
        if (!Config.enableAnvilRepair.get()) return;
        if (!player.isShiftKeyDown()) return;

        // Block checks
        var blockState = level.getBlockState(blockHitResult.getBlockPos());
        if (!blockState.is(BlockTags.ANVIL)) return;
        var block = blockState.getBlock();
        if (block == Blocks.ANVIL) return;

        // Item checks
        var stack = player.getItemInHand(hand);
        boolean hasIngot = stack.is(Items.IRON_INGOT), hasBlock = stack.is(Items.IRON_BLOCK);
        if (!hasIngot && !hasBlock) return;

        // Roll chances
        float chance;
        if (block == Blocks.CHIPPED_ANVIL) {
            chance = hasBlock? 1f : 0.33f;
        } else if (block == Blocks.DAMAGED_ANVIL) {
            chance = hasBlock? 0.8f : 0.25f;
        } else {
            return;
        }

        var hitPos = blockHitResult.getBlockPos();
        var defaultState = new RepairState(hitPos, 0, 0);
        var repairState = FdaUtil.get(player, PlayerVars.ANVIL_REPAIR_STATE, defaultState);

        // Reset attempts if player moved to a different anvil
        if (!hitPos.equals(repairState.pos())) repairState = defaultState;

        int attempts = hasBlock? repairState.blockAttempts() : repairState.ingotAttempts();

        // Shrink used repair item
        if (!player.isCreative()) {
            level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, hitPos, Block.getId(blockState));
            stack.shrink(1);
        }

        if (attempts >= 2 || MathUtil.chance(chance)) {
            // Clear repair state
            FdaUtil.set(player, PlayerVars.ANVIL_REPAIR_STATE, RepairState.NONE);

            // Set new block
            var repairedBlock = (block == Blocks.DAMAGED_ANVIL)? Blocks.CHIPPED_ANVIL : Blocks.ANVIL;
            level.setBlock(
                hitPos,
                repairedBlock.defaultBlockState().setValue(
                    BlockStateProperties.HORIZONTAL_FACING,
                    blockState.getValue(BlockStateProperties.HORIZONTAL_FACING)
                ),
                Block.UPDATE_ALL
            );
            // Play particles and sound
            level.levelEvent(LevelEvent.PARTICLES_SCRAPE, hitPos, 0);
            level.playSound(null, hitPos, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.4f, 1.8f);
        } else {
            var next = hasBlock
                ? new RepairState(hitPos, repairState.ingotAttempts(), repairState.blockAttempts() + 1)
                : new RepairState(hitPos, repairState.ingotAttempts() + 1, repairState.blockAttempts());
            FdaUtil.set(player, PlayerVars.ANVIL_REPAIR_STATE, next);
        }
    }
}