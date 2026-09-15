package com.bobmowzie.mowziesmobs.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class DataGenerators implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();
        MMBlockTags blockTags = pack.addProvider(MMBlockTags::new);
        pack.addProvider((output, registries) -> new MMItemTags(output, registries, blockTags));
        pack.addProvider(MMEntityTypeTags::new);
        pack.addProvider(MMBiomeTags::new);
        pack.addProvider(MMRecipes::new);
        pack.addProvider(RegistryDataGenerator::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.STRUCTURE, StructureHandler::bootstrap);
        registryBuilder.add(Registries.STRUCTURE_SET, StructureSetHandler::bootstrap);
    }
}
