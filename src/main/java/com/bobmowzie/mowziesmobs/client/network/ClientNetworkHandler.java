package com.bobmowzie.mowziesmobs.client.network;

import com.bobmowzie.mowziesmobs.server.message.*;
import com.ilexiconn.llibrary.server.network.AnimationMessage;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public class ClientNetworkHandler {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(MessageUseAbility.TYPE, (payload, context) -> MessageUseAbility.handleClient(payload, context.player()));
        ClientPlayNetworking.registerGlobalReceiver(MessageUpdateBossBar.TYPE, (payload, context) -> MessageUpdateBossBar.handleClient(payload, context.player()));
        ClientPlayNetworking.registerGlobalReceiver(MessageSunblockEffect.TYPE, (payload, context) -> MessageSunblockEffect.handleClient(payload, context.player()));
        ClientPlayNetworking.registerGlobalReceiver(MessageLinkEntities.TYPE, (payload, context) -> MessageLinkEntities.handleClient(payload, context.player()));
        ClientPlayNetworking.registerGlobalReceiver(MessageInterruptAbility.TYPE, (payload, context) -> MessageInterruptAbility.handleClient(payload, context.player()));
        ClientPlayNetworking.registerGlobalReceiver(MessageFreezeEffect.TYPE, (payload, context) -> MessageFreezeEffect.handleClient(payload, context.player()));
        ClientPlayNetworking.registerGlobalReceiver(MessageBlackPinkInYourArea.TYPE, (payload, context) -> MessageBlackPinkInYourArea.handleClient(payload, context.player()));
        ClientPlayNetworking.registerGlobalReceiver(MessageJumpToAbilitySectionServerToClient.TYPE, (payload, context) -> MessageJumpToAbilitySectionServerToClient.handleClient(payload, context.player()));
        ClientPlayNetworking.registerGlobalReceiver(AnimationMessage.TYPE, (payload, context) -> AnimationMessage.handleClient(payload, context.player()));
        ClientPlayNetworking.registerGlobalReceiver(MessageAddInProgressCooldown.TYPE, (payload, context) -> MessageAddInProgressCooldown.handleClient(payload, context.player()));
    }

    public static void sendToServer(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }
}
