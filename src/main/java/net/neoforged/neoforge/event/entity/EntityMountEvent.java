package net.neoforged.neoforge.event.entity;

import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class EntityMountEvent extends Event implements ICancellableEvent {
    private final Entity entityMounting;
    private final Entity entityBeingMounted;
    private final boolean isMounting;
    private boolean canceled = false;

    public EntityMountEvent(Entity entityMounting, Entity entityBeingMounted, boolean isMounting) {
        this.entityMounting = entityMounting;
        this.entityBeingMounted = entityBeingMounted;
        this.isMounting = isMounting;
    }

    public Entity getEntityMounting() {
        return entityMounting;
    }

    public Entity getEntityBeingMounted() {
        return entityBeingMounted;
    }

    public boolean isMounting() {
        return isMounting;
    }

    public boolean isDismounting() {
        return !isMounting;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        this.canceled = cancel;
    }
}
