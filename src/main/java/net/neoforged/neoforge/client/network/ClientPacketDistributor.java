package net.neoforged.neoforge.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class ClientPacketDistributor {
    public static void sendToServer(CustomPacketPayload payload) {
        if (payload != null) {
            ClientPlayNetworking.send(payload);
        }
    }
}
