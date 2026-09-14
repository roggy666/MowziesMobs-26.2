package net.neoforged.neoforge.attachment;

import java.util.function.Supplier;

public class AttachmentType<T> {
    private final Supplier<T> factory;

    public AttachmentType(Supplier<T> factory) {
        this.factory = factory;
    }

    public T create() {
        return factory != null ? factory.get() : null;
    }

    public static <T> Builder<T> serializable(Supplier<T> factory) {
        return new Builder<>(factory);
    }

    public static class Builder<T> {
        private final Supplier<T> factory;

        public Builder(Supplier<T> factory) {
            this.factory = factory;
        }

        public AttachmentType<T> build() {
            return new AttachmentType<>(factory);
        }
    }
}
