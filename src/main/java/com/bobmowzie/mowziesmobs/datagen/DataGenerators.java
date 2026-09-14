package com.bobmowzie.mowziesmobs.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class DataGenerators {

    // PORTING NOTE (1.21.1 -> 26.1.2): GatherDataEvent lost includeServer()/includeClient() and is now abstract,
    // fired as one of two concrete subtypes (GatherDataEvent.Client / GatherDataEvent.Server) instead - confirmed
    // via javap against the real 26.1.2.95 neoforge jar. Mirrors the confirmed-working pattern from a real,
    // already-building 26.1.2 mod (Silent Gear's net.silentchaos512.gear.data.DataGenerators#gatherData): subscribe
    // to GatherDataEvent.Client (the variant NeoForge's runData task fires for a normal full data run covering both
    // client and server providers) and pass a literal `true` to DataGenerator#addProvider(boolean, T) - that
    // boolean's old "does this run's --server/--client flag include this provider" meaning is now handled entirely
    // by which event subtype fired, not a per-call flag.
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        DataGenerator.PackGenerator pack = generator.getVanillaPack(true);
        PackOutput output = new PackOutput(java.nio.file.Path.of("src/generated/resources"));
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        MMBlockTags blockTags = new MMBlockTags(output, provider);
        pack.addProvider(out -> blockTags);
        pack.addProvider(out -> new MMItemTags(output, provider));
        pack.addProvider(out -> new MMEntityTypeTags(output, provider));
        pack.addProvider(out -> new MMBiomeTags(output, provider));
        pack.addProvider(out -> new MMRecipes.Runner(output, provider));
        pack.addProvider(out -> new RegistryDataGenerator(output, provider));
    }
}
