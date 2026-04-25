package net.justmili.servertweaks.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.justmili.servertweaks.util.CommandUtil;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Discard {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("discard").requires(src -> CommandUtil.hasPerms(src, 4))
            .then(Commands.argument("entity", EntityArgument.entities())
                .executes(context -> {
                    Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entity");
                    CommandSourceStack source = context.getSource();

                    List<Entity> players = new ArrayList<>(entities.stream().filter(e -> e instanceof Player).toList());
                    if (!players.isEmpty()) {
                        for (Entity player : players) {
                            CommandUtil.sendFail(source, "Can not discard entity " + player.getType().toShortString());
                        }
                        return 0;
                    }

                    for (Entity entity : entities) {
                        entity.discard();
                    }

                    if (entities.size() == 1) {
                        Entity only = entities.iterator().next();
                        CommandUtil.sendSucc(source, "Discarded " + only.getType().toShortString());
                    } else {
                        CommandUtil.sendSucc(source, "Discarded " + entities.size() + " entities");
                    }

                    return entities.size();
                })
            )
            .then(Commands.argument("block", BlockPosArgument.blockPos())
                .executes(context -> {
                    BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "block");
                    ServerLevel level = context.getSource().getLevel();
                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    CommandSourceStack source = context.getSource();

                    String blockId = level.getBlockState(pos).getBlock().builtInRegistryHolder().key().identifier().toString();

                    if (blockEntity instanceof Clearable clearable) {
                        clearable.clearContent();
                    }
                    level.removeBlock(pos, false);

                    CommandUtil.sendSucc(source, "Discarded block " + blockId + " from " + formatPos(pos));

                    return 1;
                })
            )
            .then(Commands.literal("inventory")
                .then(Commands.argument("entity", EntityArgument.entity())
                    .executes(context -> {
                        Entity entity = EntityArgument.getEntity(context, "entity");
                        CommandSourceStack source = context.getSource();
                        int cleared = 0;

                        if (entity instanceof Player player) {
                            cleared = countContainer(player.getInventory());
                            player.getInventory().clearContent();
                            player.containerMenu.setCarried(ItemStack.EMPTY);
                        } else if (entity instanceof Mob mob) {
                            if (mob instanceof Container container) {
                                cleared += countContainer(container);
                                container.clearContent();
                            }
                            for (EquipmentSlot slot : EquipmentSlot.values()) {
                                if (!mob.getItemBySlot(slot).isEmpty()) cleared++;
                                mob.setItemSlot(slot, ItemStack.EMPTY);
                            }
                        } else if (entity instanceof Container container) {
                            cleared = countContainer(container);
                            container.clearContent();
                        }

                        CommandUtil.sendSucc(source, "Discarded " + cleared + " item(s) from " + entity.getType().toShortString() + "'s inventory");
                        return cleared;
                    })
                )
                .then(Commands.argument("block", BlockPosArgument.blockPos())
                    .executes(context -> {
                        BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "block");
                        ServerLevel level = context.getSource().getLevel();
                        BlockEntity blockEntity = level.getBlockEntity(pos);
                        CommandSourceStack source = context.getSource();

                        String blockId = level.getBlockState(pos).getBlock().builtInRegistryHolder().key().identifier().toString();
                        int cleared = 0;

                        if (blockEntity instanceof Clearable clearable) {
                            if (blockEntity instanceof Container container) {
                                cleared = countContainer(container);
                            }
                            clearable.clearContent();
                        }

                        CommandUtil.sendSucc(source, "Discarded " + cleared + " item(s) from " + blockId + "'s inventory at " + formatPos(pos));
                        return cleared;
                    })
                )
            )
        );
    }

    private static int countContainer(Container container) {
        int count = 0;
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (!container.getItem(i).isEmpty()) count++;
        }
        return count;
    }

    private static String formatPos(BlockPos pos) {
        return pos.getX() + " " + pos.getY() + " " + pos.getZ();
    }
}