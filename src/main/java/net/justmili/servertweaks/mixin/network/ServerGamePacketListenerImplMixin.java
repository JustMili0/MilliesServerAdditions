package net.justmili.servertweaks.mixin.network;

import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.justmili.corelibs.v1.utils.common.CommandUtil;
import net.justmili.servertweaks.config.Config;
import net.justmili.servertweaks.util.SmpPermsUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameModeArgument;
import net.minecraft.network.protocol.game.ServerboundChangeGameModePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.level.GameType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {

    @ModifyConstant(method = "handleMovePlayer", constant = @Constant(floatValue = 100.0F))
    private float servertweaks$uncapPlayerSpeed(float speed) {
        if (Config.limitPlayerSpeed.get()) return speed;
        return Float.MAX_VALUE;
    }

    @ModifyConstant(method = "handleMovePlayer", constant = @Constant(floatValue = 300.0F))
    private float servertweaks$uncapElytraSpeed(float speed) {
        if (Config.limitElytraSpeed.get()) return speed;
        return Float.MAX_VALUE;
    }

    @ModifyConstant(method = "handleMoveVehicle", constant = @Constant(doubleValue = (double) 100.0F))
    private double servertweaks$uncapVehicleSpeed(double speed) {
        if (Config.limitVehicleSpeed.get()) return speed;
        return Double.MAX_VALUE;
    }

    @Inject(method = "handleChangeGameMode", at = @At("HEAD"), cancellable = true)
    private void servertweaks$restrictDebugGameModeSwitch(ServerboundChangeGameModePacket packet, CallbackInfo ci) {
        var player = ((ServerGamePacketListenerImpl) (Object) this).player;
        if (SmpPermsUtil.isLimitedOperator(player)) {
            var target = packet.mode();
            if (target != GameType.SURVIVAL && target != GameType.SPECTATOR) ci.cancel();
        }
    }

    @Inject(method = "performUnsignedChatCommand", at = @At("HEAD"), cancellable = true)
    private void servertweaks$elevateAbilityCommands(String command, CallbackInfo ci) {
        var player = ((ServerGamePacketListenerImpl) (Object) this).player;
        var server = player.level().getServer();
        var source = server.createCommandSourceStack().withEntity(player).withLevel(player.level());
        var parseResults = server.getCommands().getDispatcher().parse(command, source);

        if (SmpPermsUtil.isLimitedOperator(player)) {
            if (servertweaks$requiresElevatedPermission(parseResults.getContext(), source)) {
                var rootLiteral = servertweaks$getRootLiteral(parseResults.getContext());

                if (rootLiteral == null || !SmpPermsUtil.ALLOWED_FOR_LIMITED_OP.contains(rootLiteral)) {
                    servertweaks$failExec(player, ci);
                    return;
                }

                if (rootLiteral.equals("gamemode")) {
                    try {
                        var builtContext = parseResults.getContext().build(command);
                        var gameType = GameModeArgument.getGameMode(builtContext, "gamemode");
                        if (gameType != GameType.SURVIVAL && gameType != GameType.SPECTATOR) {
                            servertweaks$failExec(player, ci);
                            return;
                        }
                    } catch (CommandSyntaxException e) {
                        servertweaks$failExec(player, ci);
                        return;
                    }
                }
            }
        }

        // for abilities' presets
        if (command.startsWith("abilities applyPreset ") || command.startsWith("abilities dontApplyPreset ")) {
            server.getCommands().performCommand(parseResults, command);
            ci.cancel();
        }
    }

    @Unique
    private static void servertweaks$failExec(ServerPlayer player, CallbackInfo ci) {
        CommandUtil.sendFailTo(player, "You do not have permission to use this command.");
        ci.cancel();
    }

    @Unique
    private static boolean servertweaks$requiresElevatedPermission(CommandContextBuilder<CommandSourceStack> context, CommandSourceStack source) {
        var noPermSource = source.withPermission(LevelBasedPermissionSet.forLevel(PermissionLevel.byId(0)));

        for (var parsedNode : context.getNodes()) {
            if (!parsedNode.getNode().canUse(noPermSource)) return true;
        }

        var child = context.getChild();
        return child != null && servertweaks$requiresElevatedPermission(child, source);
    }

    @Unique
    private static @Nullable String servertweaks$getRootLiteral(CommandContextBuilder<CommandSourceStack> context) {
        var nodes = context.getNodes();
        return nodes.isEmpty() ? null : nodes.get(0).getNode().getName();
    }
}
