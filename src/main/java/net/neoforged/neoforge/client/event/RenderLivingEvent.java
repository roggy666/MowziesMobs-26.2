package net.neoforged.neoforge.client.event;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class RenderLivingEvent extends Event {
    public static class Pre<T, S extends LivingEntityRenderState, M> extends RenderLivingEvent implements ICancellableEvent {
        private boolean canceled;

        @Override
        public boolean isCanceled() {
            return canceled;
        }

        @Override
        public void setCanceled(boolean cancel) {
            this.canceled = cancel;
        }
    }

    public static class Post<T, S extends LivingEntityRenderState, M> extends RenderLivingEvent {
    }
}
