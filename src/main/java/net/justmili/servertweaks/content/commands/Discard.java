package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.justmili.servertweaks.core.util.CommandUtil;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Discard {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("discard").requires(src -> CommandUtil.hasPerms(src, 2))
            // Entity discard
            .then(Commands.argument("entity", EntityArgument.entities())
                .executes(context -> {
                    Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entity");
                    CommandSourceStack source = context.getSource();

                    // Get players, and prevent players from being discarded
                    List<Entity> players = new ArrayList<>(entities.stream().filter(e -> e instanceof Player).toList());
                    if (!players.isEmpty()) {
                        for (Entity player : players) {
                            CommandUtil.sendFail(source, "Can't discard entity "+player.getType().toShortString());
                        }
                        return 0;
                    }

                    // Discard entities
                    for (Entity entity : entities) {
                        entity.discard();
                    }

                    // One or multiple? Send message
                    if (entities.size() == 1) {
                        Entity only = entities.iterator().next();
                        CommandUtil.sendSucc(source, "Discarded "+only.getName().getString());
                    } else {
                        CommandUtil.sendSucc(source, "Discarded "+entities.size()+" entities");
                    }
                    return entities.size();
                })
            )

            // Block discard
            .then(Commands.argument("block", BlockPosArgument.blockPos())
                .executes(context -> {
                    BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "block");
                    ServerLevel level = context.getSource().getLevel();
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    CommandSourceStack source = context.getSource();

                    // Get block name
                    String blockId = level.getBlockState(pos).getBlock().getName().getString();

                    // First clear the inventory of the block
                    if (blockEntity instanceof Clearable clearable) {
                        clearable.clearContent();
                    }
                    // Remove the block from level
                    level.removeBlock(pos, false);

                    // Send message
                    CommandUtil.sendSucc(source, "Discarded "+blockId+" from "+formatPos(pos));
                    return 1;
                })
            )

            // Entity/Block inventory discard
            .then(Commands.literal("inventory")

                // Entity
                .then(Commands.argument("entity", EntityArgument.entity())
                    .executes(context -> {
                        Entity entity = EntityArgument.getEntity(context, "entity");
                        CommandSourceStack source = context.getSource();
                        int cleared = 0;

                        // Players, clear everything, even carried items
                        if (entity instanceof Player player) {
                            cleared = countContainer(player.getInventory());
                            player.getInventory().clearContent();
                            player.containerMenu.setCarried(ItemStack.EMPTY);

                            // Mobs, clear armor, held items, equipped containers
                        } else if (entity instanceof Mob mob) {
                            // Clear armor, held items
                            for (EquipmentSlot slot : EquipmentSlot.values()) {
                                if (!mob.getItemBySlot(slot).isEmpty()) cleared++;
                                mob.setItemSlot(slot, ItemStack.EMPTY);
                            }
                            // Clear horse, donkey, mule etc chest inventory
                            if (mob instanceof AbstractChestedHorse chestedHorse && chestedHorse.hasChest()) {
                                if (chestedHorse instanceof Container container) {
                                    cleared += countContainer(container);
                                    container.clearContent();
                                }
                            }
                            // Clear other entity containers
                            if (mob instanceof Container container) {
                                cleared += countContainer(container);
                                container.clearContent();
                            }
                        }

                        // Send message
                        CommandUtil.sendSucc(source, "Discarded "+cleared+" item(s) from "+entity.getName().getString()+"'s inventory");
                        return cleared;
                    })
                )
                .then(Commands.argument("block", BlockPosArgument.blockPos())
                    .executes(context -> {
                        BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "block");
                        ServerLevel level = context.getSource().getLevel();
                        BlockEntity blockEntity = level.getBlockEntity(pos);
                        CommandSourceStack source = context.getSource();

                        // Get block name
                        String blockId = level.getBlockState(pos).getBlock().getName().getString();
                        int cleared = 0;

                        // Is it a clearable container?
                        if (blockEntity instanceof Clearable clearable) {
                            // Get how much will be cleared
                            if (blockEntity instanceof Container container) {
                                cleared = countContainer(container);
                            }
                            // Clear
                            clearable.clearContent();
                        } else {
                            // ...No? Fail.
                            CommandUtil.sendFail(source, "Could not clear "+blockId+". Block is not a container");
                            return 0;
                        }

                        // Send message
                        CommandUtil.sendSucc(source, "Discarded "+cleared+" item(s) from "+blockId+"'s inventory at "+formatPos(pos));
                        return cleared;
                    })
                )
            )
        );
    }

    // Helper methods
    private static int countContainer(Container container) {
        int count = 0;
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (!container.getItem(i).isEmpty()) count++;
        }
        return count;
    }

    private static String formatPos(BlockPos pos) {
        return pos.getX()+" "+pos.getY()+" "+pos.getZ();
    }
}