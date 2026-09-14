package com.bobmowzie.mowziesmobs.fabric;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.message.NetworkHandler;
import com.bobmowzie.mowziesmobs.server.world.spawn.SpawnHandler;
import net.fabricmc.api.ModInitializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public class MowziesMobsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        IEventBus modBus = IEventBus.create();
        ModContainer container = new ModContainer(MMCommon.MODID);
        new MMCommon(modBus, container);

        // Network
        NetworkHandler.register(new RegisterPayloadHandlersEvent());

        // Attributes
        EntityHandler.onCreateAttributes(new EntityAttributeCreationEvent());

        // Lifecycle
        modBus.post(new FMLCommonSetupEvent());
        modBus.post(new FMLLoadCompleteEvent());

        // Mob spawns in biomes
        SpawnHandler.initBiomeSpawns();
    }
}
