package com.bobmowzie.mowziesmobs.fabric;

import com.bobmowzie.mowziesmobs.MMClient;
import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.ClientEventBusSubscriber;
import com.bobmowzie.mowziesmobs.client.ClientEventHandler;
import com.bobmowzie.mowziesmobs.client.model.LayerHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import net.fabricmc.api.ClientModInitializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.common.NeoForge;

public class MowziesMobsClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        IEventBus modBus = IEventBus.create();
        ModContainer container = new ModContainer(MMCommon.MODID);
        new MMClient(modBus, container);

        // Client extensions
        modBus.post(new RegisterClientExtensionsEvent());

        // Entity and block entity renderers
        ClientEventBusSubscriber.onClientSetup(new FMLClientSetupEvent());

        // Screen menus
        ClientEventBusSubscriber.onRegisterMenuScreens(new RegisterMenuScreensEvent());

        // Particles
        ParticleHandler.registerParticles(new RegisterParticleProvidersEvent());

        // Model layers
        LayerHandler.registerLayers(new EntityRenderersEvent.RegisterLayerDefinitions());

        // Client event bus subscriber
        NeoForge.EVENT_BUS.register(ClientEventHandler.class);

        // Load complete
        modBus.post(new FMLLoadCompleteEvent());
    }
}
