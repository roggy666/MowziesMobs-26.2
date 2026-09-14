package net.neoforged.neoforge.event.entity.living;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;

public class LivingDamageEvent extends LivingEvent {
    private final DamageSource source;

    public LivingDamageEvent(LivingEntity entity, DamageSource source) {
        super(entity);
        this.source = source;
    }

    public DamageSource getSource() {
        return source;
    }

    public static class Pre extends LivingDamageEvent implements ICancellableEvent {
        private float newDamage;
        private final float originalDamage;
        private boolean canceled;

        public Pre(LivingEntity entity, DamageSource source, float damage) {
            super(entity, source);
            this.newDamage = damage;
            this.originalDamage = damage;
        }

        public float getNewDamage() {
            return newDamage;
        }

        public void setNewDamage(float newDamage) {
            this.newDamage = newDamage;
        }

        public float getOriginalDamage() {
            return originalDamage;
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

    public static class Post extends LivingDamageEvent {
        private final float inflictedDamage;

        public Post(LivingEntity entity, DamageSource source, float inflictedDamage) {
            super(entity, source);
            this.inflictedDamage = inflictedDamage;
        }

        public float getInflictedDamage() {
            return inflictedDamage;
        }

        public float getNewDamage() {
            return inflictedDamage;
        }

        public float getHealthDamage() {
            return inflictedDamage;
        }
    }
}
