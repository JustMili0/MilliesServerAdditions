package net.justmili.servertweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import net.justmili.servertweaks.content.commands.permissions.SmpPermsMatchingPermissionSet;
import net.justmili.servertweaks.variables.PlayerVars;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @ModifyReturnValue(method = "permissions", at = @At("RETURN"))
    private PermissionSet attachSmpPermsToExisting(PermissionSet original) {
        return new SmpPermsMatchingPermissionSet(original, this.getAttachedOrCreate(PlayerVars.SMP_PERM_LEVEL));
    }
}
