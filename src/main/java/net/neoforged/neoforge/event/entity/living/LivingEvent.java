package net.neoforged.neoforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;

public class LivingEvent extends Event {
    private final LivingEntity entity;

    public LivingEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public static class LivingJumpEvent extends LivingEvent {
        public LivingJumpEvent(LivingEntity entity) {
            super(entity);
        }
    }

    public static class LivingTickEvent extends LivingEvent {
        public LivingTickEvent(LivingEntity entity) {
            super(entity);
        }
    }
}
