package net.justmili.servertweaks.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.justmili.servertweaks.network.packets.ClientboundModCheckPacket;

public class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ClientboundModCheckPacket.TYPE, (_, _) -> {});
    }
}
