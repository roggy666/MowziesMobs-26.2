package net.neoforged.neoforge.client.event;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.BossEvent;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class CustomizeGuiOverlayEvent extends Event {
    public static class BossEventProgress extends CustomizeGuiOverlayEvent implements ICancellableEvent {
        private final GuiGraphicsExtractor guiGraphics;
        private final int x;
        private final int y;
        private final BossEvent bossEvent;
        private int increment = 25;
        private boolean canceled;

        public BossEventProgress(GuiGraphicsExtractor guiGraphics, int x, int y, BossEvent bossEvent) {
            this.guiGraphics = guiGraphics;
            this.x = x;
            this.y = y;
            this.bossEvent = bossEvent;
        }

        public GuiGraphicsExtractor getGuiGraphics() {
            return guiGraphics;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public BossEvent getBossEvent() {
            return bossEvent;
        }

        public int getIncrement() {
            return increment;
        }

        public void setIncrement(int increment) {
            this.increment = increment;
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

    public static class Chat extends CustomizeGuiOverlayEvent {
    }
}
