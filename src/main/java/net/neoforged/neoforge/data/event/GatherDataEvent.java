package net.neoforged.neoforge.data.event;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.Event;

import java.util.concurrent.CompletableFuture;

public abstract class GatherDataEvent extends Event {
    private final DataGenerator generator;
    private final CompletableFuture<HolderLookup.Provider> lookupProvider;

    public GatherDataEvent(DataGenerator generator, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.generator = generator;
        this.lookupProvider = lookupProvider;
    }

    public DataGenerator getGenerator() {
        return generator;
    }

    public CompletableFuture<HolderLookup.Provider> getLookupProvider() {
        return lookupProvider;
    }

    public static class Client extends GatherDataEvent {
        public Client(DataGenerator generator, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(generator, lookupProvider);
        }
    }

    public static class Server extends GatherDataEvent {
        public Server(DataGenerator generator, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(generator, lookupProvider);
        }
    }
}
