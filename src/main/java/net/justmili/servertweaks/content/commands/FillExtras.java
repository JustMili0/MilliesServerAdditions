package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import net.justmili.libs.v1.utils.common.CommandUtil;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class FillExtras {
    private static final Dynamic2CommandExceptionType ERROR_AREA_TOO_LARGE = new Dynamic2CommandExceptionType(
        (max, count) -> Component.translatableEscape("commands.fill.toobig", max, count)
    );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("fillextras")
            .requires(src -> CommandUtil.hasPerms(src, 1))
            .then(Commands.argument("from", BlockPosArgument.blockPos())
                .then(Commands.argument("to", BlockPosArgument.blockPos())
                    .then(Commands.argument("replacement", BlockStateArgument.block(buildContext))

                        .then(Commands.literal("destroyOnly")
                            .then(Commands.argument("target", BlockStateArgument.block(buildContext))
                                .executes(context -> replaceOnly(
                                    context.getSource(),
                                    BlockPosArgument.getLoadedBlockPos(context, "from"),
                                    BlockPosArgument.getLoadedBlockPos(context, "to"),
                                    BlockStateArgument.getBlock(context, "target").getState(),
                                    BlockStateArgument.getBlock(context, "replacement").getState(),
                                    true))))

                        .then(Commands.literal("replaceOnly")
                            .then(Commands.argument("target", BlockStateArgument.block(buildContext))
                                .executes(context -> replaceOnly(
                                    context.getSource(),
                                    BlockPosArgument.getLoadedBlockPos(context, "from"),
                                    BlockPosArgument.getLoadedBlockPos(context, "to"),
                                    BlockStateArgument.getBlock(context, "target").getState(),
                                    BlockStateArgument.getBlock(context, "replacement").getState(),
                                    false))))

                        .then(Commands.literal("silkDestroy")
                            .executes(context -> enchantDestroy(
                                context.getSource(),
                                BlockPosArgument.getLoadedBlockPos(context, "from"),
                                BlockPosArgument.getLoadedBlockPos(context, "to"),
                                BlockStateArgument.getBlock(context, "replacement").getState(),
                                Enchant.SILK_TOUCH, 0)))

                        .then(Commands.literal("silkDestroyOnly")
                            .then(Commands.argument("target", BlockStateArgument.block(buildContext))
                                .executes(context -> enchantDestroyOnly(
                                    context.getSource(),
                                    BlockPosArgument.getLoadedBlockPos(context, "from"),
                                    BlockPosArgument.getLoadedBlockPos(context, "to"),
                                    BlockStateArgument.getBlock(context, "target").getState(),
                                    BlockStateArgument.getBlock(context, "replacement").getState(),
                                    Enchant.SILK_TOUCH, 0))))

                        .then(Commands.literal("fortuneDestroy").then(Commands.argument("fortuneLevel", IntegerArgumentType.integer(1, 3))
                            .executes(context -> enchantDestroy(
                                context.getSource(),
                                BlockPosArgument.getLoadedBlockPos(context, "from"),
                                BlockPosArgument.getLoadedBlockPos(context, "to"),
                                BlockStateArgument.getBlock(context, "replacement").getState(),
                                Enchant.FORTUNE, IntegerArgumentType.getInteger(context, "fortuneLevel")))))

                        .then(Commands.literal("fortuneDestroyOnly")
                            .then(Commands.argument("fortuneLevel", IntegerArgumentType.integer(1, 3))
                                .then(Commands.argument("target", BlockStateArgument.block(buildContext))
                                    .executes(context -> enchantDestroyOnly(
                                        context.getSource(),
                                        BlockPosArgument.getLoadedBlockPos(context, "from"),
                                        BlockPosArgument.getLoadedBlockPos(context, "to"),
                                        BlockStateArgument.getBlock(context, "target").getState(),
                                        BlockStateArgument.getBlock(context, "replacement").getState(),
                                        Enchant.FORTUNE, IntegerArgumentType.getInteger(context, "fortuneLevel"))))))
                    )
                )
            )
        );
    }

    static int checkVolume(ServerLevel level, BlockPos from, BlockPos to) throws CommandSyntaxException {
        var box = BoundingBox.fromCorners(from, to);
        int volume = box.getXSpan() * box.getYSpan() * box.getZSpan();
        int limit = level.getGameRules().get(GameRules.MAX_BLOCK_MODIFICATIONS);
        if (volume > limit) throw ERROR_AREA_TOO_LARGE.create(limit, volume);
        return volume;
    }

    enum Enchant {
        SILK_TOUCH, FORTUNE
    }

    static int replaceOnly(CommandSourceStack source, BlockPos from, BlockPos to, BlockState target, BlockState replacement, boolean destroy) throws CommandSyntaxException {
        var level = source.getLevel();
        int volume = checkVolume(level, from, to);
        var targetBlock = target.getBlock();
        var replaceBlock = replacement.getBlock();

        for (var pos : BlockPos.betweenClosed(from, to)) {
            var current = level.getBlockState(pos);
            if (current.getBlock() == targetBlock) {
                if (destroy) Block.dropResources(current, level, pos, level.getBlockEntity(pos));
                level.setBlock(pos, replacement, Block.UPDATE_CLIENTS);
            }
        }

        sendMessage(source, replaceBlock, volume);
        return volume;
    }

    static int enchantDestroy(CommandSourceStack source, BlockPos from, BlockPos to, BlockState replacement, Enchant enchantment, int fortuneLevel) throws CommandSyntaxException {
        var level = source.getLevel();
        int volume = checkVolume(level, from, to);
        var replaceBlock = replacement.getBlock();

        var tool = switch (enchantment) {
            case SILK_TOUCH -> silkTouchTool(level);
            case FORTUNE -> fortuneTool(level, fortuneLevel);
        };

        for (var pos : BlockPos.betweenClosed(from, to)) {
            var current = level.getBlockState(pos);
            if (!current.isAir()) {
                Block.dropResources(current, level, pos, level.getBlockEntity(pos), null, tool);
                level.setBlock(pos, replacement, Block.UPDATE_CLIENTS);
            }
        }

        sendMessage(source, replaceBlock, volume);
        return volume;
    }

    static int enchantDestroyOnly(CommandSourceStack source, BlockPos from, BlockPos to, BlockState target, BlockState replacement, Enchant enchantment, int fortuneLevel) throws CommandSyntaxException {
        var level = source.getLevel();
        int volume = checkVolume(level, from, to);
        var targetBlock = target.getBlock();
        var replaceBlock = replacement.getBlock();

        var tool = switch (enchantment) {
            case SILK_TOUCH -> silkTouchTool(level);
            case FORTUNE -> fortuneTool(level, fortuneLevel);
        };

        for (var pos : BlockPos.betweenClosed(from, to)) {
            var current = level.getBlockState(pos);
            if (current.getBlock() == targetBlock) {
                Block.dropResources(current, level, pos, level.getBlockEntity(pos), null, tool);
                level.setBlock(pos, replacement, Block.UPDATE_CLIENTS);
            }
        }

        sendMessage(source, replaceBlock, volume);
        return volume;
    }

    static void sendMessage(CommandSourceStack source, Block replacementBlock, int count) {
        CommandUtil.sendOk(source, String.format("Successfully replaced %s block(s) with %s", count, replacementBlock.getName().getString()));
    }

    static ItemStack silkTouchTool(ServerLevel level) {
        var tool = new ItemStack(Items.NETHERITE_PICKAXE);
        var silkTouch = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
        var enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(silkTouch, 1);
        tool.set(DataComponents.ENCHANTMENTS, enchantments.toImmutable());
        return tool;
    }

    static ItemStack fortuneTool(ServerLevel level, int fortuneLevel) {
        var tool = new ItemStack(Items.NETHERITE_PICKAXE);
        var fortune = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
        var enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        enchantments.set(fortune, fortuneLevel);
        tool.set(DataComponents.ENCHANTMENTS, enchantments.toImmutable());
        return tool;
    }
}