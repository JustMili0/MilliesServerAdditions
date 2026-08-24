package net.justmili.servertweaks.network.packets;

import net.justmili.servertweaks.ServerTweaks;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public class ClientboundModCheckPacket implements CustomPacketPayload {
    // Only use of this is just checking if client has the mod, since this is a primarily server-side mod
    public static final Identifier PACKET_ID = ServerTweaks.asId("clientbound_mod_check_packet");
    public static final CustomPacketPayload.Type<ClientboundModCheckPacket> TYPE = new CustomPacketPayload.Type<>(PACKET_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundModCheckPacket> CODEC = StreamCodec.unit(new ClientboundModCheckPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
