package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.justmili.libs.v1.utils.CommandUtil;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Abilities;

import java.util.Collection;

public class Fly {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection environment) {
        dispatcher.register(Commands.literal("fly")
            .requires(src -> CommandUtil.hasPerms(src, 1))
            .executes(context ->
                toggleFly(context.getSource().getPlayerOrException(), context.getSource()))
            .then(Commands.argument("player", EntityArgument.players())
                .executes(context -> {
                    Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "player");
                    int count = 0;
                    for (var player : players) {
                        toggleFly(player, context.getSource());
                        count++;
                    }
                    return count;
                })
            )
        );
    }

    private static int toggleFly(ServerPlayer player, CommandSourceStack source) {
        Abilities abilities = player.getAbilities();

        abilities.mayfly = !abilities.mayfly;
        if (!abilities.mayfly) abilities.flying = false;

        player.onUpdateAbilities();

        CommandUtil.sendOk(source, (abilities.mayfly ? "Enabled" : "Disabled")+" creative flight for "+player.getName().getString());
        return 1;
    }
}