package net.justmili.servertweaks.content.mechanics.logic;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.justmili.libs.v1.utils.common.FdaUtil;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.util.SmpPermsUtil;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.permissions.LevelBasedPermissionSet;

public class UpdateSmpPermsOnLogon {

    public static void onLogon(ServerGamePacketListenerImpl listener, PacketSender sender, MinecraftServer server) {
        if (!Config.enableSmpPermsCommand.get()) return;
        var player = listener.getPlayer();

        if (!FdaUtil.has(player, PlayerVars.SMP_PERM_LEVEL)) {
            applyPermissions(server, player, SmpPermsUtil.defaultPerms());
            return;
        }

        int smpPermLevel = FdaUtil.getInt(player, PlayerVars.SMP_PERM_LEVEL);
        int permLevel = player.permissions() instanceof LevelBasedPermissionSet permSet ? permSet.level().id() : -1;
        if (smpPermLevel != permLevel) applyPermissions(server, player, smpPermLevel);
    }

    private static void applyPermissions(MinecraftServer server, ServerPlayer player, int smpPermLevel) {
        int permLevel = switch (smpPermLevel) {
            case 1 -> 2; // Moderator
            case 2 -> 3; // Administrator
            case 3, 4 -> 4; // Limited Operator, Operator
            default -> 0; // Default
        };

        if (smpPermLevel == SmpPermsUtil.defaultPerms()) {
            SmpPermsUtil.deop(player);
        } else {
            SmpPermsUtil.op(player, smpPermLevel, permLevel);
        }
    }
}