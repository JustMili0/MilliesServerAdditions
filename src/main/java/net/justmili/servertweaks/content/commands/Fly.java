package net.justmili.servertweaks.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.justmili.libs.v1.utils.common.CommandUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class Fly {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("fly")
            .requires(src -> CommandUtil.hasPerms(src, 1))
            .executes(context ->
                toggleFly(context.getSource().getPlayerOrException(), context.getSource()))
            .then(Commands.argument("player", EntityArgument.players())
                .executes(context -> toggleFly(context.getSource(), EntityArgument.getPlayers(context, "player")))
            )
        );
    }

    static int toggleFly(CommandSourceStack source, Collection<ServerPlayer> players) {
        int count = 0;
        for (var player : players) {
            toggleFly(player, source);
            count++;
        }
        return count;
    }

    static int toggleFly(ServerPlayer player, CommandSourceStack source) {
        var abilities = player.getAbilities();

        abilities.mayfly = !abilities.mayfly;
        if (!abilities.mayfly) abilities.flying = false;

        player.onUpdateAbilities();

        CommandUtil.sendOk(source, (abilities.mayfly? "Enabled" : "Disabled") + " creative flight for " + player.getName().getString());
        return 1;
    }
}