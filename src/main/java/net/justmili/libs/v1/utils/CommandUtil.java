package net.justmili.libs.v1.utils;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.level.LevelAccessor;

public class CommandUtil {
    // Replace "new" permission system with the good ol' numbers
    public static boolean hasPerms(CommandSourceStack source, int level) {
        return source.permissions().hasPermission(new Permission.HasCommandLevel(PermissionLevel.byId(level)));
    }

    // Command success/fail response
    public static void sendOk(CommandSourceStack source, String message) {
        source.sendSuccess(() -> Component.literal(message), false);
    }
    public static void sendFail(CommandSourceStack source, String message) {
        source.sendFailure(Component.literal(message));
    }
    // Send chat message to player/server
    public static void sendFailTo(ServerPlayer player, String message) {
        player.sendSystemMessage(Component.literal("§c"+message));
    }
    public static void sendOkTo(ServerPlayer player, String message) {
        player.sendSystemMessage(Component.literal(message));
    }
    public static void broadcastTo(LevelAccessor world, String message, boolean bypassHiddenChat) {
        world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(message), bypassHiddenChat);
    }
    public static void broadcastTo(LevelAccessor world, String message) {
        broadcastTo(world, message, false);
    }
    public static void broadcastFailTo(LevelAccessor world, String message, boolean bypassHiddenChat) {
        world.getServer().getPlayerList().broadcastSystemMessage(Component.literal("§c"+message), bypassHiddenChat);
    }
    public static void broadcastFailTo(LevelAccessor world, String message) {
        broadcastTo(world, message, false);
    }

    // Other
    public static void executeAsPlayer(ServerPlayer player, String command) {
        if (player != null) player.level().getServer().getCommands()
            .performPrefixedCommand(player.createCommandSourceStack().withSuppressedOutput(), command);
    }
    public static void executeAsServer(MinecraftServer server, String command) {
        if (server != null) server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), command);
    }
}
