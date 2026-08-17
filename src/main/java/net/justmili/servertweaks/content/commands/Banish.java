package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.justmili.libs.v1.utils.common.CommandUtil;
import net.justmili.servertweaks.registries.DimRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Relative;

public class Banish {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("banish")
                .requires(src -> CommandUtil.hasPerms(src, 1))
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(context -> {
                        var player = EntityArgument.getPlayer(context, "player");
                        var source = context.getSource();

                        return banish(source, player);
                    })
                )
        );
    }

    private static int banish(CommandSourceStack source, ServerPlayer player) {
        var banishLevel = source.getServer().getLevel(DimRegistry.BANISHMENT);

        if (banishLevel == null) {
            CommandUtil.sendFail(source, "Banishment dimension is not loaded");
            return 0;
        }
        player.teleportTo(banishLevel, 0.5, 2.0, 0.5, Relative.DELTA, player.getYRot(), player.getXRot(), true);

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
