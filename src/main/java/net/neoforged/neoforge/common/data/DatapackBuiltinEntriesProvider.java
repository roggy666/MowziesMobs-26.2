package net.neoforged.neoforge.common.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistriesDatapackGenerator;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DatapackBuiltinEntriesProvider implements DataProvider {
    private final RegistriesDatapackGenerator generator;

    public DatapackBuiltinEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder datapackEntriesBuilder, Set<String> modIds) {
        this.generator = new RegistriesDatapackGenerator(output, registries);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return this.generator.run(output);
    }

    @Override
    public String getName() {
        return "Registries";
    }
}
