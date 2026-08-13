package net.justmili.servertweaks.util;

import net.justmili.libs.v1.utils.common.FdaUtil;
import net.justmili.servertweaks.content.commands.arguments.SmpPermsArgumentType;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.server.level.ServerPlayer;

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
