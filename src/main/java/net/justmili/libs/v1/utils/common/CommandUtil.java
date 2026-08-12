package net.justmili.libs.v1.utils.common;

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
        server.getPlayerList().broadcastSystemMessage(message, showAboveHotbar);
    }

    public static void broadcastServer(MinecraftServer server, String message, boolean showAboveHotbar) {
        broadcastServer(server, Component.literal(message), showAboveHotbar);
    }

    public static void broadcastServer(MinecraftServer server, Component message) {
        server.getPlayerList().broadcastSystemMessage(message, false);
    }

    public static void broadcastServer(MinecraftServer server, String message) {
        broadcastServer(server, Component.literal(message), false);
    }

    // Other
    public static void executeAsPlayer(MinecraftServer server, ServerPlayer player, String command) {
        if (player != null && server != null) server.getCommands().performPrefixedCommand(player.createCommandSourceStack().withSuppressedOutput(), command);
    }

    public static void executeAsServer(MinecraftServer server, String command) {
        if (server != null) server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), command);
    }
}
