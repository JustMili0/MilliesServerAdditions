package net.justmili.corelibs.v1.utils.common;

import net.justmili.corelibs.v1.utils.server.ServerUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;

public class CommandUtil {
    // Replace "new" permission system with the good ol' numbers
    public static boolean hasPerms(CommandSourceStack source, int level) {
        return source.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.byId(level)));
    }

    // Command success/fail response
    public static void sendOk(CommandSourceStack source, Component message, boolean allowLogging) {
        source.sendSuccess(() -> message, allowLogging);
    }

    public static void sendOk(CommandSourceStack source, String message, boolean allowLogging) {
        sendOk(source, Component.literal(message), allowLogging);
    }

    public static void sendOk(CommandSourceStack source, Component message) {
        sendOk(source, message, true);
    }

    public static void sendOk(CommandSourceStack source, String message) {
        sendOk(source, message, true);
    }

    public static void sendFail(CommandSourceStack source, Component message) {
        source.sendFailure(message);
    }

    public static void sendFail(CommandSourceStack source, String message) {
        sendFail(source, Component.literal(message));
    }

    // Broadcast
    public static void sendOkTo(ServerPlayer player, Component message, boolean showAboveHotbar) {
        player.sendSystemMessage(message, showAboveHotbar);
    }

    public static void sendOkTo(ServerPlayer player, String message, boolean showAboveHotbar) {
        sendOkTo(player, Component.literal(message), showAboveHotbar);
    }

    public static void sendOkTo(ServerPlayer player, Component message) {
        player.sendSystemMessage(message, false);
    }

    public static void sendOkTo(ServerPlayer player, String message) {
        sendOkTo(player, Component.literal(message), false);
    }

    public static void sendFailTo(ServerPlayer player, String message, boolean showAboveHotbar) {
        sendOkTo(player, Component.literal("§c" + message), showAboveHotbar);
    }

    public static void sendFailTo(ServerPlayer player, String message) {
        sendOkTo(player, Component.literal("§c" + message), false);
    }

    public static void broadcastServer(MinecraftServer server, Component message, boolean showAboveHotbar) {
        ServerUtil.broadcast(server, message, showAboveHotbar);
    }

    public static void broadcastServer(MinecraftServer server, String message, boolean showAboveHotbar) {
        broadcastServer(server, Component.literal(message), showAboveHotbar);
    }

    public static void broadcastServer(MinecraftServer server, Component message) {
        ServerUtil.broadcast(server, message, false);
    }

    public static void broadcastServer(MinecraftServer server, String message) {
        broadcastServer(server, Component.literal(message), false);
    }

    // Other
    public static void runCommandAsPlayer(ServerPlayer player, String command) {
        ServerUtil.runCommandAs(player.createCommandSourceStack(), command);
    }

    public static void runCommandAsServer(MinecraftServer server, String command) {
        ServerUtil.runCommandAsServer(server, command);
    }
}
