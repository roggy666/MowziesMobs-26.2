package net.neoforged.neoforge.network.registration;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.util.concurrent.CompletableFuture;

public class PayloadRegistrar {
    private final String version;

    public PayloadRegistrar(String version) {
        this.version = version;
    }

    @SuppressWarnings("unchecked")
    public <B, T extends CustomPacketPayload> PayloadRegistrar playToClient(
            CustomPacketPayload.Type<T> type,
            StreamCodec<B, T> codec,
            IPayloadHandler<T> handler) {
        PayloadTypeRegistry.clientboundPlay().register(type, (StreamCodec<? super RegistryFriendlyByteBuf, T>) codec);
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            ClientReceiverHelper.registerClient(type, handler);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public <B, T extends CustomPacketPayload> PayloadRegistrar playToServer(
            CustomPacketPayload.Type<T> type,
            StreamCodec<B, T> codec,
            IPayloadHandler<T> handler) {
        PayloadTypeRegistry.serverboundPlay().register(type, (StreamCodec<? super RegistryFriendlyByteBuf, T>) codec);
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
            IPayloadContext ctx = new IPayloadContext() {
                @Override
                public Player player() {
                    return context.player();
                }

                @Override
                public CompletableFuture<Void> enqueueWork(Runnable runnable) {
                    return CompletableFuture.runAsync(runnable, context.server());
                }
            };
            handler.handle(payload, ctx);
        });
        return this;
    }

    public <B, T extends CustomPacketPayload> PayloadRegistrar playBidirectional(
            CustomPacketPayload.Type<T> type,
            StreamCodec<B, T> codec,
            IPayloadHandler<T> handler) {
        playToClient(type, codec, handler);
        playToServer(type, codec, handler);
        return this;
    }

    private static class ClientReceiverHelper {
        private static <T extends CustomPacketPayload> void registerClient(CustomPacketPayload.Type<T> type, IPayloadHandler<T> handler) {
            ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> {
                IPayloadContext ctx = new IPayloadContext() {
                    @Override
                    public Player player() {
                        return context.player();
                    }

                    @Override
                    public CompletableFuture<Void> enqueueWork(Runnable runnable) {
                        return CompletableFuture.runAsync(runnable, context.client());
                    }
                };
                handler.handle(payload, ctx);
            });
        }
    }
}
