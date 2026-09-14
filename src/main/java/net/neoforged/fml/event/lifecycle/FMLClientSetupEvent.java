package net.neoforged.fml.event.lifecycle;

import net.neoforged.bus.api.Event;

import java.util.concurrent.CompletableFuture;

public class FMLClientSetupEvent extends Event {
    public CompletableFuture<Void> enqueueWork(Runnable work) {
        work.run();
        return CompletableFuture.completedFuture(null);
    }
}
