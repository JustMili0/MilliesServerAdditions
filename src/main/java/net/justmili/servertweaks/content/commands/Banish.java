package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.justmili.mlibs.v1.utils.common.CommandUtil;
import net.justmili.mlibs.v1.utils.common.EntityUtil;
import net.justmili.servertweaks.registries.DimRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public class Banish {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("banish").requires(src -> CommandUtil.hasPerms(src, 1))
            .then(Commands.argument("player", EntityArgument.player())
                .executes(context -> banish(context.getSource(), EntityArgument.getPlayer(context, "player"))))
        );
    }

    static int banish(CommandSourceStack source, ServerPlayer player) {
        var banishLevel = source.getServer().getLevel(DimRegistry.BANISHMENT);

        if (banishLevel == null) {
            CommandUtil.sendFail(source, "Banishment dimension is not loaded");
            return 0;
        }
        EntityUtil.teleport(player, banishLevel, 0.5, 2.0, 0.5);

        CommandUtil.sendOkTo(player,
            """
                You have been banished.
                There is no way out - no death or portal can ever save you.
                This infinite world of darkness consumes everything that enters it, not even the void can escape itself.
                """
        );

        return 1;
    }
}
