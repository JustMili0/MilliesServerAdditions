package net.justmili.servertweaks.util;

import net.justmili.libs.v1.utils.common.FdaUtil;
import net.justmili.servertweaks.content.commands.arguments.SmpPermsArgumentType;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionLevel;

import java.util.Optional;

public class SmpPermsUtil {
    public static boolean isLimitedOperator(ServerPlayer player) {
        return FdaUtil.getInt(player, PlayerVars.SMP_PERM_LEVEL) == SmpPermsUtil.limitedOpPerms();
    }

    public static String permissionNameByLevel(int level) {
        for (var value : SmpPermsArgumentType.PermissionLevel.values()) {
            if (value.getPermissionLevel() == level) return value.getDisplayName();
        }
        return SmpPermsArgumentType.PermissionLevel.DEFAULT.getDisplayName();
    }

    public static void op(ServerPlayer player, int smpPermLevel, int permLevel) {
        var server = player.level().getServer();
        FdaUtil.set(player, PlayerVars.SMP_PERM_LEVEL, smpPermLevel);
        server.getPlayerList().op(player.nameAndId(), Optional.of(LevelBasedPermissionSet.forLevel(PermissionLevel.byId(permLevel))), Optional.of(false));
        server.getCommands().sendCommands(player);
    }
    public static void deop(ServerPlayer player) {
        var server = player.level().getServer();
        FdaUtil.set(player, PlayerVars.SMP_PERM_LEVEL, SmpPermsUtil.defaultPerms());
        server.getPlayerList().deop(player.nameAndId());
        server.getCommands().sendCommands(player);
    }

    public static int defaultPerms() {
        return SmpPermsArgumentType.PermissionLevel.DEFAULT.getPermissionLevel();
    }

    public static int moderatorPerms() {
        return SmpPermsArgumentType.PermissionLevel.MODERATOR.getPermissionLevel();
    }

    public static int adminPerms() {
        return SmpPermsArgumentType.PermissionLevel.ADMINISTRATOR.getPermissionLevel();
    }

    public static int limitedOpPerms() {
        return SmpPermsArgumentType.PermissionLevel.LIMITED_OPERATOR.getPermissionLevel();
    }

    public static int operatorPerms() {
        return SmpPermsArgumentType.PermissionLevel.OPERATOR.getPermissionLevel();
    }
}
