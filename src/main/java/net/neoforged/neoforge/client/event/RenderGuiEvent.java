package net.neoforged.neoforge.client.event;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class RenderGuiEvent extends Event {
    private final GuiGraphicsExtractor guiGraphics;
    private final float partialTick;

    public RenderGuiEvent(GuiGraphicsExtractor guiGraphics, float partialTick) {
        this.guiGraphics = guiGraphics;
        this.partialTick = partialTick;
    }

    public GuiGraphicsExtractor getGuiGraphics() {
        return guiGraphics;
    }

    public float getPartialTick() {
        return partialTick;
    }

    public static class Pre extends RenderGuiEvent implements ICancellableEvent {
        private boolean canceled;

        public Pre(GuiGraphicsExtractor guiGraphics, float partialTick) {
            super(guiGraphics, partialTick);
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

    public static class Post extends RenderGuiEvent {
        public Post(GuiGraphicsExtractor guiGraphics, float partialTick) {
            super(guiGraphics, partialTick);
        }
    }
}
