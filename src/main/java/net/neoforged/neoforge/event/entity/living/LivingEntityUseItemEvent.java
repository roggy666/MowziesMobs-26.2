package net.neoforged.neoforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.ICancellableEvent;

public class LivingEntityUseItemEvent extends LivingEvent {
    private final ItemStack item;
    private int duration;

    public LivingEntityUseItemEvent(LivingEntity entity, ItemStack item, int duration) {
        super(entity);
        this.item = item;
        this.duration = duration;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public static class Start extends LivingEntityUseItemEvent implements ICancellableEvent {
        private boolean canceled;

        public Start(LivingEntity entity, ItemStack item, int duration) {
            super(entity, item, duration);
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

    public static class Tick extends LivingEntityUseItemEvent implements ICancellableEvent {
        private boolean canceled;

        public Tick(LivingEntity entity, ItemStack item, int duration) {
            super(entity, item, duration);
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

    public static class Stop extends LivingEntityUseItemEvent implements ICancellableEvent {
        private boolean canceled;

        public Stop(LivingEntity entity, ItemStack item, int duration) {
            super(entity, item, duration);
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

    public static class Finish extends LivingEntityUseItemEvent {
        private ItemStack result;

        public Finish(LivingEntity entity, ItemStack item, int duration, ItemStack result) {
            super(entity, item, duration);
            this.result = result;
        }

        public ItemStack getResult() {
            return result;
        }

        public void setResult(ItemStack result) {
            this.result = result;
        }
    }
}
