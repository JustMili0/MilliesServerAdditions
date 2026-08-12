package net.justmili.servertweaks.content.commands.permissions;

import net.justmili.servertweaks.content.commands.arguments.SmpPermsArgumentType;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionSet;

public record SmpPermsMatchingPermissionSet(PermissionSet original, SmpPermsArgumentType.PermissionLevel level) implements PermissionSet {
    @Override
    public boolean hasPermission(Permission permission) {
        return original.hasPermission(permission) || level.getPermissionSet().hasPermission(permission);
    }
}
