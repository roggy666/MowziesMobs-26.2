package com.bobmowzie.mowziesmobs;

import com.bobmowzie.mowziesmobs.client.ClientEventBusSubscriber;
import com.bobmowzie.mowziesmobs.client.ClientEventHandler;
import com.bobmowzie.mowziesmobs.client.ClientLayerRegistry;
import com.bobmowzie.mowziesmobs.client.model.LayerHandler;
import com.bobmowzie.mowziesmobs.client.network.ClientNetworkHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.neoforged.fml.config.ModConfig;

public class MMClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ConfigRegistry.INSTANCE.register(MMCommon.MODID, ModConfig.Type.CLIENT, ConfigHandler.CLIENT_CONFIG);

        ClientNetworkHandler.register();
        ClientEventBusSubscriber.registerRenderers();
        LayerHandler.registerLayers();
        ClientLayerRegistry.register();
        ParticleHandler.registerParticles();
        ClientEventHandler.register();
    }
}
