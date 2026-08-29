package net.justmili.servertweaks.content.mechanics.features;

import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.abilities.Abilities;
import net.justmili.servertweaks.content.abilities.core.AbilityProfilesUtil;
import net.justmili.servertweaks.mixin.accessors.CropBlockAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class RightClickHarvest {
    public static void onUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult blockHitResult) {
        if (!Config.rightClickHarvest.get()) return;
        if (level.isClientSide()) return;
        if (hand != InteractionHand.MAIN_HAND) return;
        if (player.isSpectator()) return;
        if (AbilityProfilesUtil.getAbilities(player).contains(Abilities.HERBIVORE) && player.isShiftKeyDown()) return;

        var pos = blockHitResult.getBlockPos();
        var state = level.getBlockState(pos);
        var block = state.getBlock();
        boolean hoeHeld = player.getMainHandItem().getItem() instanceof HoeItem;

        switch (block) {
            case CropBlock cropBlock -> {
                if (!harvestCrop(player, level, pos, state, cropBlock)) return;
                if (hoeHeld) {
                    for (var near : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 0, 1))) {
                        if (near.equals(pos)) continue;
                        var nearState = level.getBlockState(near);
                        if (nearState.getBlock() instanceof CropBlock nearCrop)
                            harvestCrop(player, level, near.immutable(), nearState, nearCrop);
                    }
                }
            }
            case NetherWartBlock _ -> {
                if (!harvestNetherWart(player, level, pos, state)) return;
                if (hoeHeld) {
                    for (var near : BlockPos.betweenClosed(pos.offset(-1, 0, -1), pos.offset(1, 0, 1))) {
                        if (near.equals(pos)) continue;
                        var nearState = level.getBlockState(near);
                        if (nearState.getBlock() instanceof NetherWartBlock) harvestNetherWart(player, level, near.immutable(), nearState);
                    }
                }
            }
            case CocoaBlock _ -> {
                if (!harvestCocoa(player, level, pos, state)) return;
            }
            case SugarCaneBlock _ -> {
                harvestSugarCane(player, level, pos);
                return;
            }
            default -> {
                return;
            }
        }

        player.swing(InteractionHand.MAIN_HAND, true);
        damageHoeIfHeld(player, player.getMainHandItem(), level);
    }

    private static boolean harvestCrop(Player player, Level level, BlockPos pos, BlockState state, CropBlock cropBlock) {
        if (!cropBlock.isMaxAge(state)) return false;

        var tool = player.getMainHandItem();
        var drops = getDrops(level, pos, state, player, tool);
        removeOneSeed(drops, new ItemStack(cropBlock.asItem()));

        var ageProp = ((CropBlockAccessor) cropBlock).invokeGetAgeProperty();
        level.setBlock(pos, state.setValue(ageProp, 0), Block.UPDATE_ALL);
        for (ItemStack drop : drops) Block.popResource(level, pos, drop);

        return true;
    }

    private static boolean harvestNetherWart(Player player, Level level, BlockPos pos, BlockState state) {
        if (state.getValue(NetherWartBlock.AGE) < NetherWartBlock.MAX_AGE) return false;

        var tool = player.getMainHandItem();
        var drops = getDrops(level, pos, state, player, tool);
        removeOneSeed(drops, new ItemStack(Items.NETHER_WART));

        level.setBlock(pos, state.setValue(NetherWartBlock.AGE, 0), Block.UPDATE_ALL);
        for (ItemStack drop : drops) Block.popResource(level, pos, drop);

        return true;
    }

    private static boolean harvestCocoa(Player player, Level level, BlockPos pos, BlockState state) {
        if (state.getValue(CocoaBlock.AGE) < 2) return false;

        var tool = player.getMainHandItem();
        var drops = getDrops(level, pos, state, player, tool);
        removeOneSeed(drops, new ItemStack(Items.COCOA_BEANS));

        level.setBlock(pos, state.setValue(CocoaBlock.AGE, 0), Block.UPDATE_ALL);
        for (ItemStack drop : drops) Block.popResource(level, pos, drop);

        return true;
    }

    private static void harvestSugarCane(Player player, Level level, BlockPos clickedPos) {
        // Walk down to find the bottom sugar cane block
        var bottom = clickedPos;
        while (level.getBlockState(bottom.below()).is(Blocks.SUGAR_CANE)) bottom = bottom.below();

        var breakFrom = bottom.above();
        if (!level.getBlockState(breakFrom).is(Blocks.SUGAR_CANE)) return;

        var tool = player.getMainHandItem();
        var current = breakFrom;
        while (level.getBlockState(current).is(Blocks.SUGAR_CANE)) {
            var drops = getDrops(level, current, level.getBlockState(current), player, tool);
            level.removeBlock(current, false);
            for (ItemStack drop : drops) Block.popResource(level, current, drop);
            current = current.above();
        }

        player.swing(InteractionHand.MAIN_HAND, true);
    }

    // Builds full LootParams so Fortune, Silk Touch, and all loot table conditions apply correctly
    private static List<ItemStack> getDrops(Level level, BlockPos pos, BlockState state, Player player, ItemStack tool) {
        var builder = new LootParams.Builder((ServerLevel) level)
            .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
            .withParameter(LootContextParams.BLOCK_STATE, state)
            .withParameter(LootContextParams.TOOL, tool)
            .withOptionalParameter(LootContextParams.THIS_ENTITY, player);
        return state.getDrops(builder);
    }

    // Removes one seed from drops so the crop effectively replants itself
    private static void removeOneSeed(List<ItemStack> drops, ItemStack seedItem) {
        for (ItemStack drop : drops) {
            if (ItemStack.isSameItem(drop, seedItem) && drop.getCount() > 0) {
                drop.shrink(1);
                return;
            }
        }
    }

    // Damages hoe by 1 durability if the player is holding one (respects Unbreaking)
    private static void damageHoeIfHeld(Player player, ItemStack stack, Level level) {
        if (stack.getItem() instanceof HoeItem) stack.hurtAndBreak(1, (ServerLevel) level, (ServerPlayer) player, _ -> {});
    }
}