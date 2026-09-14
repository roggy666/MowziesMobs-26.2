package net.neoforged.neoforge.client.event;

import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class InputEvent extends Event {
    public static class InteractionKeyMappingTriggered extends InputEvent implements ICancellableEvent {
        private final KeyMapping keyMapping;
        private boolean canceled;

        public InteractionKeyMappingTriggered(KeyMapping keyMapping) {
            this.keyMapping = keyMapping;
        }

        public KeyMapping getKeyMapping() {
            return keyMapping;
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

    public static class Key extends InputEvent {
        private final int key;
        private final int scanCode;
        private final int action;
        private final int modifiers;

        public Key(int key, int scanCode, int action, int modifiers) {
            this.key = key;
            this.scanCode = scanCode;
            this.action = action;
            this.modifiers = modifiers;
        }

        public int getKey() {
            return key;
        }

        public int getScanCode() {
            return scanCode;
        }

        public int getAction() {
            return action;
        }

        public int getModifiers() {
            return modifiers;
        }
    }

    public static class MouseButton extends InputEvent {
        private final int button;
        private final int action;
        private final int mods;

        public MouseButton(int button, int action, int mods) {
            this.button = button;
            this.action = action;
            this.mods = mods;
        }

        public int getButton() {
            return button;
        }

        public int getAction() {
            return action;
        }

        public int getMods() {
            return mods;
        }

        public static class Pre extends MouseButton implements ICancellableEvent {
            private boolean canceled;

            public Pre(int button, int action, int mods) {
                super(button, action, mods);
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

        public static class Post extends MouseButton {
            public Post(int button, int action, int mods) {
                super(button, action, mods);
            }
        }
    }
}
