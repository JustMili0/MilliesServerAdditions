package net.justmili.servertweaks.mixin;

import net.justmili.servertweaks.config.Config;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @ModifyConstant(method = {"handleMovePlayer"}, constant = {@Constant(floatValue = 100.0F)})
    private float uncapPlayerSpeed(float speed) {
        if (Config.limitPlayerSpeed.get()) return speed;
        return Float.MAX_VALUE;
    }

    @ModifyConstant(method = {"handleMovePlayer"}, constant = {@Constant(floatValue = 300.0F)})
    private float uncapElytraSpeed(float speed) {
        if (Config.limitElytraSpeed.get()) return speed;
        return Float.MAX_VALUE;
    }

    @ModifyConstant(method = {"handleMoveVehicle"}, constant = {@Constant(doubleValue = (double) 100.0F)})
    private double uncapVehicleSpeed(double speed) {
        if (Config.limitVehicleSpeed.get()) return speed;
        return Double.MAX_VALUE;
    }

    @Inject(method = "performUnsignedChatCommand", at = @At("HEAD"), cancellable = true)
    private void elevateAbilityCommands(String command, CallbackInfo ci) {
        if (command.startsWith("abilities applyPreset ") || command.startsWith("abilities dontApplyPreset ")) {
            var player = ((ServerGamePacketListenerImpl) (Object) this).player;
            var server = player.level().getServer();

            var source = server.createCommandSourceStack().withEntity(player).withLevel(player.level());
            var parseResults = server.getCommands().getDispatcher().parse(command, source);
            server.getCommands().performCommand(parseResults, command);

            ci.cancel();
        }
    }
}
