package net.justmili.servertweaks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.justmili.servertweaks.util.CommandUtil;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FillExtras {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("fillExtras")
            .requires(src -> CommandUtil.hasPerms(src, 2))
            .then(Commands.argument("from", BlockPosArgument.blockPos())
                .then(Commands.argument("to", BlockPosArgument.blockPos())
                    .then(Commands.argument("block", BlockStateArgument.block(commandBuildContext))
                        .then(Commands.literal("destroyOnly")
                            .then(Commands.argument("target", BlockStateArgument.block(commandBuildContext))
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    ServerLevel level = source.getLevel();

                                    BlockPos from = BlockPosArgument.getLoadedBlockPos(context, "from");
                                    BlockPos to = BlockPosArgument.getLoadedBlockPos(context, "to");
                                    BlockState replaceWith = BlockStateArgument.getBlock(context, "block").getState();
                                    BlockState targetBlock = BlockStateArgument.getBlock(context, "target").getState();

                                    int minX = Math.min(from.getX(), to.getX()), maxX = Math.max(from.getX(), to.getX());
                                    int minY = Math.min(from.getY(), to.getY()), maxY = Math.max(from.getY(), to.getY());
                                    int minZ = Math.min(from.getZ(), to.getZ()), maxZ = Math.max(from.getZ(), to.getZ());

                                    int count = 0;
                                    for (int x = minX; x <= maxX; x++)
                                        for (int y = minY; y <= maxY; y++)
                                            for (int z = minZ; z <= maxZ; z++) {
                                                BlockPos pos = new BlockPos(x, y, z);
                                                BlockState current = level.getBlockState(pos);
                                                if (current.getBlock() == targetBlock.getBlock()) {
                                                    Block.dropResources(current, level, pos, level.getBlockEntity(pos));
                                                    level.setBlock(pos, replaceWith, 3);
                                                    count++;
                                                }
                                            }
                                    CommandUtil.sendSucc(source, "Successfully filled "+count+" block(s) of "+targetBlock.getBlock().getName().getString()+".");
                                    return count;
                                })
                            )
                        )
                        .then(Commands.literal("replaceOnly")
                            .then(Commands.argument("target", BlockStateArgument.block(commandBuildContext))
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    ServerLevel level = source.getLevel();

                                    BlockPos from = BlockPosArgument.getLoadedBlockPos(context, "from");
                                    BlockPos to = BlockPosArgument.getLoadedBlockPos(context, "to");
                                    BlockState replaceWith = BlockStateArgument.getBlock(context, "block").getState();
                                    BlockState targetBlock = BlockStateArgument.getBlock(context, "target").getState();

                                    int minX = Math.min(from.getX(), to.getX()), maxX = Math.max(from.getX(), to.getX());
                                    int minY = Math.min(from.getY(), to.getY()), maxY = Math.max(from.getY(), to.getY());
                                    int minZ = Math.min(from.getZ(), to.getZ()), maxZ = Math.max(from.getZ(), to.getZ());

                                    int count = 0;
                                    for (int x = minX; x <= maxX; x++)
                                        for (int y = minY; y <= maxY; y++)
                                            for (int z = minZ; z <= maxZ; z++) {
                                                BlockPos pos = new BlockPos(x, y, z);
                                                BlockState current = level.getBlockState(pos);
                                                if (current.getBlock() == targetBlock.getBlock()) {
                                                    level.setBlock(pos, replaceWith, 3);
                                                    count++;
                                                }
                                            }
                                    CommandUtil.sendSucc(source, "Successfully filled "+count+" block(s) of "+targetBlock.getBlock().getName().getString()+".");
                                    return count;
                                })
                            )
                        )

                        .then(Commands.literal("silkDestroy")
                            .executes(context -> {
                                CommandSourceStack source = context.getSource();
                                ServerLevel level = source.getLevel();
                                BlockPos from = BlockPosArgument.getLoadedBlockPos(context, "from");
                                BlockPos to = BlockPosArgument.getLoadedBlockPos(context, "to");
                                BlockState replaceWith = BlockStateArgument.getBlock(context, "block").getState();
                                ItemStack silkTool = makeSilkTouchTool(level);
                                int minX = Math.min(from.getX(), to.getX()), maxX = Math.max(from.getX(), to.getX());
                                int minY = Math.min(from.getY(), to.getY()), maxY = Math.max(from.getY(), to.getY());
                                int minZ = Math.min(from.getZ(), to.getZ()), maxZ = Math.max(from.getZ(), to.getZ());
                                int count = 0;
                                for (int x = minX; x <= maxX; x++)
                                    for (int y = minY; y <= maxY; y++)
                                        for (int z = minZ; z <= maxZ; z++) {
                                            BlockPos pos = new BlockPos(x, y, z);
                                            BlockState current = level.getBlockState(pos);
                                            if (!current.isAir()) {
                                                Block.dropResources(current, level, pos, level.getBlockEntity(pos), null, silkTool);
                                                level.setBlock(pos, replaceWith, 3);
                                                count++;
                                            }
                                        }
                                CommandUtil.sendSucc(source, "Silk-touch destroyed "+count+" block(s).");
                                return count;
                            })
                        )
                        .then(Commands.literal("silkDestroyOnly")
                            .then(Commands.argument("target", BlockStateArgument.block(commandBuildContext))
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    ServerLevel level = source.getLevel();
                                    BlockPos from = BlockPosArgument.getLoadedBlockPos(context, "from");
                                    BlockPos to = BlockPosArgument.getLoadedBlockPos(context, "to");
                                    BlockState replaceWith = BlockStateArgument.getBlock(context, "block").getState();
                                    BlockState targetBlock = BlockStateArgument.getBlock(context, "target").getState();
                                    ItemStack silkTool = makeSilkTouchTool(level);
                                    int minX = Math.min(from.getX(), to.getX()), maxX = Math.max(from.getX(), to.getX());
                                    int minY = Math.min(from.getY(), to.getY()), maxY = Math.max(from.getY(), to.getY());
                                    int minZ = Math.min(from.getZ(), to.getZ()), maxZ = Math.max(from.getZ(), to.getZ());
                                    int count = 0;
                                    for (int x = minX; x <= maxX; x++)
                                        for (int y = minY; y <= maxY; y++)
                                            for (int z = minZ; z <= maxZ; z++) {
                                                BlockPos pos = new BlockPos(x, y, z);
                                                BlockState current = level.getBlockState(pos);
                                                if (current.getBlock() == targetBlock.getBlock()) {
                                                    Block.dropResources(current, level, pos, level.getBlockEntity(pos), null, silkTool);
                                                    level.setBlock(pos, replaceWith, 3);
                                                    count++;
                                                }
                                            }
                                    CommandUtil.sendSucc(source, "Silk-touch destroyed "+count+" block(s) of "+targetBlock.getBlock().getName().getString()+".");
                                    return count;
                                })
                            )
                        )

                        .then(Commands.literal("fortuneDestroy")
                            .then(Commands.argument("fortuneLevel", IntegerArgumentType.integer(1, 3))
                                .executes(context -> {
                                    CommandSourceStack source = context.getSource();
                                    ServerLevel level = source.getLevel();
                                    BlockPos from = BlockPosArgument.getLoadedBlockPos(context, "from");
                                    BlockPos to = BlockPosArgument.getLoadedBlockPos(context, "to");
                                    BlockState replaceWith = BlockStateArgument.getBlock(context, "block").getState();
                                    int fortuneLevel = IntegerArgumentType.getInteger(context, "fortuneLevel");
                                    ItemStack fortuneTool = makeFortuneTool(level, fortuneLevel);
                                    int minX = Math.min(from.getX(), to.getX()), maxX = Math.max(from.getX(), to.getX());
                                    int minY = Math.min(from.getY(), to.getY()), maxY = Math.max(from.getY(), to.getY());
                                    int minZ = Math.min(from.getZ(), to.getZ()), maxZ = Math.max(from.getZ(), to.getZ());
                                    int count = 0;
                                    for (int x = minX; x <= maxX; x++)
                                        for (int y = minY; y <= maxY; y++)
                                            for (int z = minZ; z <= maxZ; z++) {
                                                BlockPos pos = new BlockPos(x, y, z);
                                                BlockState current = level.getBlockState(pos);
                                                if (!current.isAir()) {
                                                    Block.dropResources(current, level, pos, level.getBlockEntity(pos), null, fortuneTool);
                                                    level.setBlock(pos, replaceWith, 3);
                                                    count++;
                                                }
                                            }
                                    CommandUtil.sendSucc(source, "Fortune "+fortuneLevel+" destroyed "+count+" block(s).");
                                    return count;
                                })
                            )
                        )
                        .then(Commands.literal("fortuneDestroyOnly")
                            .then(Commands.argument("fortuneLevel", IntegerArgumentType.integer(1, 3))
                                .then(Commands.argument("target", BlockStateArgument.block(commandBuildContext))
                                    .executes(context -> {
                                        CommandSourceStack source = context.getSource();
                                        ServerLevel level = source.getLevel();
                                        BlockPos from = BlockPosArgument.getLoadedBlockPos(context, "from");
                                        BlockPos to = BlockPosArgument.getLoadedBlockPos(context, "to");
                                        BlockState replaceWith = BlockStateArgument.getBlock(context, "block").getState();
                                        BlockState targetBlock = BlockStateArgument.getBlock(context, "target").getState();
                                        int fortuneLevel = IntegerArgumentType.getInteger(context, "fortuneLevel");
                                        ItemStack fortuneTool = makeFortuneTool(level, fortuneLevel);
                                        int minX = Math.min(from.getX(), to.getX()), maxX = Math.max(from.getX(), to.getX());
                                        int minY = Math.min(from.getY(), to.getY()), maxY = Math.max(from.getY(), to.getY());
                                        int minZ = Math.min(from.getZ(), to.getZ()), maxZ = Math.max(from.getZ(), to.getZ());
                                        int count = 0;
                                        for (int x = minX; x <= maxX; x++)
                                            for (int y = minY; y <= maxY; y++)
                                                for (int z = minZ; z <= maxZ; z++) {
                                                    BlockPos pos = new BlockPos(x, y, z);
                                                    BlockState current = level.getBlockState(pos);
                                                    if (current.getBlock() == targetBlock.getBlock()) {
                                                        Block.dropResources(current, level, pos, level.getBlockEntity(pos), null, fortuneTool);
                                                        level.setBlock(pos, replaceWith, 3);
                                                        count++;
                                                    }
                                                }
                                        CommandUtil.sendSucc(source, "Fortune "+fortuneLevel+" destroyed "+count+" block(s) of "+targetBlock.getBlock().getName().getString()+".");
                                        return count;
                                    })
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    private static ItemStack makeSilkTouchTool(ServerLevel level) {
        ItemStack tool = new ItemStack(Items.NETHERITE_PICKAXE);
        Holder<Enchantment> silkTouch = level.registryAccess()
            .lookupOrThrow(Registries.ENCHANTMENT)
            .getOrThrow(Enchantments.SILK_TOUCH);
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(silkTouch, 1);
        tool.set(DataComponents.ENCHANTMENTS, enchantments.toImmutable());
        return tool;
    }

    private static ItemStack makeFortuneTool(ServerLevel level, int fortuneLevel) {
        ItemStack tool = new ItemStack(Items.NETHERITE_PICKAXE);
        Holder<Enchantment> fortune = level.registryAccess()
            .lookupOrThrow(Registries.ENCHANTMENT)
            .getOrThrow(Enchantments.FORTUNE);
        ItemEnchantments.Mutable enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(fortune, fortuneLevel);
        tool.set(DataComponents.ENCHANTMENTS, enchantments.toImmutable());
        return tool;
    }
}