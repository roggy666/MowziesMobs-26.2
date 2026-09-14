package net.neoforged.neoforge.registries;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DeferredHolder<R, T extends R> implements Holder<R>, Supplier<T>, ItemLike {
    private final ResourceKey<Registry<R>> registryKey;
    private final ResourceKey<R> key;
    private final Identifier id;
    private final Supplier<T> supplier;
    private T value;

    public DeferredHolder(ResourceKey<Registry<R>> registryKey, ResourceKey<R> key, Identifier id, Supplier<T> supplier) {
        this.registryKey = registryKey;
        this.key = key;
        this.id = id;
        this.supplier = supplier;
    }

    public static <R, T extends R> DeferredHolder<R, T> create(ResourceKey<R> key) {
        return new DeferredHolder<>(null, key, key.identifier(), null);
    }

    public static <R, T extends R> DeferredHolder<R, T> create(ResourceKey<Registry<R>> registryKey, Identifier id) {
        ResourceKey<R> key = ResourceKey.create(registryKey, id);
        return new DeferredHolder<>(registryKey, key, id, null);
    }

    public Identifier getId() {
        return id;
    }

    public ResourceKey<R> getKey() {
        return key;
    }

    @Override
    public T get() {
        if (value == null && supplier != null) {
            value = supplier.get();
        }
        return value;
    }

    @Override
    public R value() {
        return get();
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public Item asItem() {
        T v = get();
        if (v instanceof ItemLike itemLike) {
            return itemLike.asItem();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public Holder<R> asHolder() {
        if (registryKey != null && key != null) {
            Registry<R> registry = (Registry<R>) BuiltInRegistries.REGISTRY.getValue(registryKey.identifier());
            if (registry != null && registry.containsKey(key)) {
                return registry.getOrThrow(key);
            }
        }
        return Holder.direct(get());
    }

    @Override
    public boolean isBound() {
        return asHolder().isBound();
    }

    @Override
    public boolean areComponentsBound() {
        return asHolder().areComponentsBound();
    }

    @Override
    public boolean is(Identifier id) {
        return (this.id != null && this.id.equals(id)) || asHolder().is(id);
    }

    @Override
    public net.minecraft.core.component.DataComponentMap components() {
        return asHolder().components();
    }

    @Override
    public boolean is(TagKey<R> tag) {
        return asHolder().is(tag);
    }

    @Override
    public boolean is(Holder<R> holder) {
        return asHolder().is(holder);
    }

    @Override
    public boolean is(ResourceKey<R> key) {
        return asHolder().is(key);
    }

    @Override
    public boolean is(Predicate<ResourceKey<R>> predicate) {
        return asHolder().is(predicate);
    }

    @Override
    public Stream<TagKey<R>> tags() {
        return asHolder().tags();
    }

    @Override
    public Either<ResourceKey<R>, R> unwrap() {
        return asHolder().unwrap();
    }

    @Override
    public Optional<ResourceKey<R>> unwrapKey() {
        return key != null ? Optional.of(key) : asHolder().unwrapKey();
    }

    @Override
    public Kind kind() {
        return asHolder().kind();
    }

    @Override
    public boolean canSerializeIn(HolderOwner<R> owner) {
        return asHolder().canSerializeIn(owner);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Holder<?> other) {
            return this.asHolder().equals(other) || (this.key != null && other.unwrapKey().map(k -> k.equals(this.key)).orElse(false));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : (get() != null ? get().hashCode() : 0);
    }
}
