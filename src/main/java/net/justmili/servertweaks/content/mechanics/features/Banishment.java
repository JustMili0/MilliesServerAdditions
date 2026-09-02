package net.justmili.servertweaks.content.mechanics.features;

import net.justmili.mlibs.v1.utils.common.ContainerUtil;
import net.justmili.mlibs.v1.utils.common.NbtUtil;
import net.justmili.mlibs.v1.utils.common.EntityUtil;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.registries.DimRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public final class Banishment {
    static final String TORCH_TAG = "banishment_torch";

    public static boolean onEntityHurt(LivingEntity entity, DamageSource source, float value) {
        if (!Config.enableBanishCommand.get()) return true;
        if (!(entity instanceof Player player)) return true;

        if (value >= (1 << 18)) return true;
        return player.level().dimension() != DimRegistry.BANISHMENT;
    }

    public static void onPlayerTick(Player player) {
        if (!Config.enableBanishCommand.get()) return;
        var level = player.level();

        if (level.dimension() != DimRegistry.BANISHMENT) return;

        var inventory = player.getInventory();

        // Give torch so they can even see
        var stack = inventory.getItem(ContainerUtil.HOTBAR_MIDDLE);
        if (stack.isEmpty()) {
            var torch = NbtUtil.custom().addBool(TORCH_TAG, true).applyToStack(new ItemStack(Items.TORCH));
            inventory.setItem(ContainerUtil.HOTBAR_MIDDLE, torch);
        }

        // Safeguard 1 - Strip any tagged torch that isn't in the designated slot, so it can't be hoarded
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (i == ContainerUtil.HOTBAR_MIDDLE) continue;
            if (isBanishmentTorch(inventory.getItem(i))) inventory.setItem(i, ItemStack.EMPTY);
        }
        if (isBanishmentTorch(player.getOffhandItem())) player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);

        // Safeguard 2 - Prevent falling into the deep void if the player breaks the bedrock somehow
        if (player.getY() < -1.0) {
            var pos = player.blockPosition();
            var min = new BlockPos(pos.getX() - 2, 0, pos.getZ() - 2);
            var max = new BlockPos(pos.getX() + 2, 0, pos.getZ() + 2);
            for (var block : BlockPos.betweenClosed(min, max)) {
                if (!level.getBlockState(block).is(Blocks.BEDROCK)) level.setBlock(block, Blocks.BEDROCK.defaultBlockState(), Block.UPDATE_ALL);
            }

            EntityUtil.teleport(player, level, player.getX(), 3.0, player.getZ());
            player.setDeltaMovement(0.0, 0.0, 0.0);
            player.resetFallDistance();
        }
    }

    static boolean isBanishmentTorch(ItemStack stack) {
        if (!stack.is(Items.TORCH)) return false;
        return NbtUtil.getBool(stack, TORCH_TAG, false);
    }

    public static void onEntityLoad(Entity entity, ServerLevel level) {
        // Safeguard 3 - despawn all dropped torch item entities so player can't infinitely dupe them
        if (!Config.enableBanishCommand.get()) return;
        if (level.dimension() != DimRegistry.BANISHMENT) return;
        if (entity instanceof ItemEntity item && item.getItem().is(Items.TORCH)) entity.discard();
    }
}