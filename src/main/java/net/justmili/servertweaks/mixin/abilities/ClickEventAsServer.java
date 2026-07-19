package net.justmili.servertweaks.mixin.abilities;

import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ClickEventAsServer {
    @Inject(method = "performUnsignedChatCommand", at = @At("HEAD"), cancellable = true)
    private void elevateAbilityCommands(String command, CallbackInfo ci) {
        if (command.startsWith("abilities applyPreset ") || command.startsWith("abilities dontApplyPreset ")) {
            var player = ((ServerGamePacketListenerImpl) (Object) this).player;
            var server = player.level().getServer();

            CommandSourceStack source = server.createCommandSourceStack().withEntity(player).withLevel(player.level());
            ParseResults<CommandSourceStack> parseResults = server.getCommands().getDispatcher().parse(command, source);
            server.getCommands().performCommand(parseResults, command);

            ci.cancel();
        }
    }
}