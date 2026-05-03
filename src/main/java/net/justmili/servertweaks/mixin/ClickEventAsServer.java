package net.justmili.servertweaks.mixin;

import com.mojang.brigadier.ParseResults;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ClickEventAsServer {
    @Inject(method = "performUnsignedChatCommand", at = @At("HEAD"), cancellable = true)
    private void servertweaks$elevateAbilityCommands(String command, CallbackInfo ci) {
        if (command.startsWith("abilities applyPreset ") || command.equals("abilities dontApplyPreset")) {
            ServerPlayer player = ((ServerGamePacketListenerImpl) (Object) this).player;
            MinecraftServer server = player.level().getServer();

            CommandSourceStack source = server.createCommandSourceStack().withEntity(player).withLevel(player.level());
            ParseResults<CommandSourceStack> parseResults = server.getCommands().getDispatcher().parse(command, source);
            server.getCommands().performCommand(parseResults, command);

            ci.cancel();
        }
    }
}