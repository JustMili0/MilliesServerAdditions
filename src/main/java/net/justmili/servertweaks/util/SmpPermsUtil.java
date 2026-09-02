package net.justmili.servertweaks.util;

import net.justmili.libs.v1.utils.common.FdaUtil;
import net.justmili.libs.v1.utils.server.ServerUtil;
import net.justmili.servertweaks.content.commands.arguments.SmpPermsArgumentType;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionLevel;

import java.util.Set;

public class SmpPermsUtil {
    public static final Set<String> ALLOWED_FOR_LIMITED_OP = Set.of(
        "stop", "ban", "ban-ip", "pardon", "pardon-ip", "kick", "banish", "discard", "gamemode", "fly", "trigger",
        "abilities", "scale", "flan", "waypoint", "function", "whitelist", "banlist", "reload", "datapack", "graves", "servercore", "servux",
        "spectate", "sit", "vanish"
    );

    public static boolean isLimitedOperator(ServerPlayer player) {
        return FdaUtil.getInt(player, PlayerVars.SMP_PERM_LEVEL) == SmpPermsUtil.limitedOpPerms();
    }

    public static String permissionNameByLevel(int level) { // Unused, why? I don't remember
        for (var value : SmpPermsArgumentType.PermissionLevel.values()) {
            if (value.getPermissionLevel() == level) return value.getDisplayName();
        }
        return SmpPermsArgumentType.PermissionLevel.DEFAULT.getDisplayName();
    }

    public static void op(ServerPlayer player, int smpPermLevel, int permLevel) {
        FdaUtil.set(player, PlayerVars.SMP_PERM_LEVEL, smpPermLevel);
        ServerUtil.opPlayer(player, LevelBasedPermissionSet.forLevel(PermissionLevel.byId(permLevel)), ServerUtil.isOp(player));
        ServerUtil.commands(player.level().getServer()).sendCommands(player);
    }
    public static void deop(ServerPlayer player) {
        FdaUtil.set(player, PlayerVars.SMP_PERM_LEVEL, SmpPermsUtil.defaultPerms());
        ServerUtil.deopPlayer(player);
        ServerUtil.commands(player.level().getServer()).sendCommands(player);
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
