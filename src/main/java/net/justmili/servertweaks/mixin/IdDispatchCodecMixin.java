package net.justmili.servertweaks.mixin;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.network.codec.IdDispatchCodec;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.function.Function;

@Mixin(IdDispatchCodec.class)
public abstract class IdDispatchCodecMixin {
    // Workaround for MC-271325 until Mojang fixes it
    @Final
    @Shadow
    private Function<Object, ?> typeGetter;

    @Final
    @Shadow
    private Object2IntMap<Object> toId;

    @Inject(method = "encode(Lio/netty/buffer/ByteBuf;Ljava/lang/Object;)V", at = @At("HEAD"), cancellable = true)
    private void encodeMixin(ByteBuf output, Object value, CallbackInfo info) {
        var packetId = this.typeGetter.apply(value);
        if (this.toId.containsKey(packetId)) return;
        if (Objects.equals(String.valueOf(packetId), "clientbound/minecraft:disconnect")) info.cancel();
    }
}