package net.neoforged.neoforge.client.event;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class RenderGuiLayerEvent extends Event {
    private final Identifier name;
    private final GuiGraphicsExtractor guiGraphics;
    private final float partialTick;

    public RenderGuiLayerEvent(Identifier name, GuiGraphicsExtractor guiGraphics, float partialTick) {
        this.name = name;
        this.guiGraphics = guiGraphics;
        this.partialTick = partialTick;
    }

    public Identifier getName() {
        return name;
    }

    public GuiGraphicsExtractor getGuiGraphics() {
        return guiGraphics;
    }

    public float getPartialTick() {
        return partialTick;
    }

    public static class Pre extends RenderGuiLayerEvent implements ICancellableEvent {
        private boolean canceled;

        public Pre(Identifier name, GuiGraphicsExtractor guiGraphics, float partialTick) {
            super(name, guiGraphics, partialTick);
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

    public static class Post extends RenderGuiLayerEvent {
        public Post(Identifier name, GuiGraphicsExtractor guiGraphics, float partialTick) {
            super(name, guiGraphics, partialTick);
        }
    }
}
