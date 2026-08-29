package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.justmili.libs.v1.utils.common.CommandUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Discard {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("discard").requires(src -> CommandUtil.hasPerms(src, 1))
            // Entity discard
            .then(Commands.argument("entity", EntityArgument.entities())
                .executes(context -> discardEntity(
                    context.getSource(), EntityArgument.getEntities(context, "entity"))))

            // Block discard
            .then(Commands.argument("block", BlockPosArgument.blockPos())
                .executes(context -> discardBlock(
                    context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "block"))))

            // Entity/Block inventory discard
            .then(Commands.literal("inventory")

                // Entity
                .then(Commands.argument("entity", EntityArgument.entity())
                    .executes(context -> discardEntityInv(
                        context.getSource(), EntityArgument.getEntity(context, "entity"))))

                // Block
                .then(Commands.argument("block", BlockPosArgument.blockPos())
                    .executes(context -> discardBlockInv(
                        context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "block"))))
            )
        );
    }

    static int discardEntity(CommandSourceStack source, Collection<? extends Entity> entities) {
        // Get players, and prevent players from being discarded
        List<Entity> players = new ArrayList<>(entities.stream().filter(e -> e instanceof Player).toList());
        if (!players.isEmpty()) {
            for (var player : players) CommandUtil.sendFail(source, "Can't discard entity " + player.getType().toShortString());
            return 0;
        }

        // Discard entities
        for (var entity : entities) entity.discard();

        if (entities.size() == 1) {
            CommandUtil.sendOk(source, "Discarded " + entities.iterator().next().getName().getString());
        } else {
            CommandUtil.sendOk(source, "Discarded " + entities.size() + " entities");
        }
        return entities.size();
    }

    static int discardBlock(CommandSourceStack source, BlockPos pos) {
        var level = source.getLevel();
        var blockEntity = level.getBlockEntity(pos);

        String blockId = level.getBlockState(pos).getBlock().getName().getString();

        if (blockEntity instanceof Clearable clearable) clearable.clearContent();
        level.removeBlock(pos, false);

        CommandUtil.sendOk(source, "Discarded " + blockId + " from " + formatPos(pos));

        return 1;
    }

    static int discardEntityInv(CommandSourceStack source, Entity entity) {
        int cleared = 0;

        if (entity instanceof Player player) { // Players, clear everything, even carried items
            var inv = player.getInventory();
            cleared = countContainer(inv);

            inv.clearContent();
            player.containerMenu.setCarried(ItemStack.EMPTY);
            inv.setChanged();

        } else if (entity instanceof Mob mob) { // Mobs, clear armor, held items, equipped containers
            // Clear armor, held items
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (!mob.getItemBySlot(slot).isEmpty()) cleared++;
                mob.setItemSlot(slot, ItemStack.EMPTY);
            }

            // Clear horse, donkey, mule etc. chest inventory
            if (mob instanceof AbstractChestedHorse chestedHorse && chestedHorse.hasChest()) {
                var inv = chestedHorse.inventory;

                for (int i = 0; i < inv.getContainerSize(); i++) {
                    var item = inv.getItem(i);
                    if (!item.isEmpty()) cleared++;
                    inv.setItem(i, ItemStack.EMPTY);
                }
                chestedHorse.setChest(false);
            }

            // Clear other entity containers (entities that ARE containers with no other inventory)
            // I have no clue if this even works :D
            if (mob instanceof Container container) {
                cleared += countContainer(container);
                container.clearContent();
            }
        }

        CommandUtil.sendOk(source, "Discarded " + cleared + " item(s) from " + entity.getName().getString() + "'s inventory");

        return cleared;
    }

    static int discardBlockInv(CommandSourceStack source, BlockPos pos) {
        var level = source.getLevel();
        var blockEntity = level.getBlockEntity(pos);

        // Get block name
        String blockId = level.getBlockState(pos).getBlock().getName().getString();
        int cleared = 0;

        // Is it a clearable container?
        if (blockEntity instanceof Clearable clearable) {
            if (blockEntity instanceof Container container) cleared = countContainer(container);
            clearable.clearContent();
        } else {
            CommandUtil.sendFail(source, "Could not clear " + blockId + ". Block is not a container");
            return 0;
        }

        // Send message
        CommandUtil.sendOk(source, "Discarded " + cleared + " item(s) from " + blockId + "'s inventory at " + formatPos(pos));

        return cleared;
    }

    // Helper methods
    static int countContainer(Container container) {
        int count = 0;
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (!container.getItem(i).isEmpty()) count++;
        }
        return count;
    }

    static String formatPos(BlockPos pos) {
        return pos.getX() + " " + pos.getY() + " " + pos.getZ();
    }
}