package net.justmili.servertweaks.registries;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.justmili.servertweaks.network.packets.ClientboundModCheckPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class PacketRegistry {
    public static void init() {
        registerClient(ClientboundModCheckPacket.TYPE, ClientboundModCheckPacket.CODEC);
    }

    private static <T extends CustomPacketPayload> void registerClient(CustomPacketPayload.Type<T> packetType, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        PayloadTypeRegistry.clientboundPlay().register(packetType, codec);
    }

    private static <T extends CustomPacketPayload> void registerServer(CustomPacketPayload.Type<T> packetType, StreamCodec<RegistryFriendlyByteBuf, T> codec) {
        PayloadTypeRegistry.serverboundPlay().register(packetType, codec);
    }
}