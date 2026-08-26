package net.justmili.servertweaks.mixin.entity;

import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.content.abilities.Abilities;
import net.justmili.servertweaks.content.abilities.core.AbilityProfilesUtil;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {

    // CANT_SWIM
    @Inject(method = "travel", at = @At("TAIL"))
    private void servertweaks$preventSwimUp(Vec3 input, CallbackInfo ci) {
        if (!Config.playerAbilities.get()) return;
        if (!((Player) (Object) this instanceof ServerPlayer player)) return;
        if (!AbilityProfilesUtil.has(player, Abilities.CANT_SWIM) || !player.isInWater() || !player.gameMode.isSurvival()) return;

        if (player.level().getFluidState(player.blockPosition().below()).isEmpty()) return;

        // Issues:
        // - Still can rise up a little, but it's very slow
        // (generally swimming in any direction is now very slow, where it's only wanted for upwards movement)
        var movement = player.getDeltaMovement();
        if (movement.y > 0.0) player.setDeltaMovement(movement.with(Direction.Axis.Y, 0.0));
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
    }
}