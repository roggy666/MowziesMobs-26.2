package net.neoforged.neoforge.event.entity.living;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;

public class MobEffectEvent extends LivingEvent {
    private final MobEffectInstance effectInstance;

    public MobEffectEvent(LivingEntity entity, MobEffectInstance effectInstance) {
        super(entity);
        this.effectInstance = effectInstance;
    }

    public MobEffectInstance getEffectInstance() {
        return effectInstance;
    }

    public static class Applicable extends MobEffectEvent {
        public enum Result {
            DEFAULT,
            DO_NOT_APPLY,
            APPLY
        }

        private Result result = Result.DEFAULT;

        public Applicable(LivingEntity entity, MobEffectInstance effectInstance) {
            super(entity, effectInstance);
        }

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }
    }

    public static class Added extends MobEffectEvent {
        public Added(LivingEntity entity, MobEffectInstance effectInstance) {
            super(entity, effectInstance);
        }
    }

    public static class Remove extends MobEffectEvent implements ICancellableEvent {
        private boolean canceled;

        public Remove(LivingEntity entity, MobEffectInstance effectInstance) {
            super(entity, effectInstance);
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

    public static class Expired extends MobEffectEvent {
        public Expired(LivingEntity entity, MobEffectInstance effectInstance) {
            super(entity, effectInstance);
        }
    }
}
