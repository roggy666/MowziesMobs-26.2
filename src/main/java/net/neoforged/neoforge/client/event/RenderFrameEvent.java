package net.neoforged.neoforge.client.event;

import net.neoforged.bus.api.Event;

public class RenderFrameEvent extends Event {
    private final float partialTick;

    public RenderFrameEvent(float partialTick) {
        this.partialTick = partialTick;
    }

    public float getPartialTick() {
        return partialTick;
    }

    public static class Pre extends RenderFrameEvent {
        public Pre(float partialTick) {
            super(partialTick);
        }
    }

    public static class Post extends RenderFrameEvent {
        public Post(float partialTick) {
            super(partialTick);
        }
    }
}
