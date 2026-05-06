package net.justmili.serveradditions.core.util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;

public class CommandUtil {
    //Fuck the new perms system, I want my numbers back
    public static boolean hasPerms(CommandSourceStack source, int level) {
        return source.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.byId(level)));
    }

    public static void sendSucc(CommandSourceStack source, String message) {
        source.sendSuccess(() -> Component.literal(message), false);
    }

    public static void sendFail(CommandSourceStack source, String message) {
        source.sendFailure(Component.literal(message));
    }

    public static void sendTo(ServerPlayer player, String message) {
        player.sendSystemMessage(Component.literal(message));
    }
}
