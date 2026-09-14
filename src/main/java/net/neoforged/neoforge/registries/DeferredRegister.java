package net.neoforged.neoforge.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class DeferredRegister<T> {
    protected final ResourceKey<Registry<T>> registryKey;
    protected final String namespace;
    protected final List<DeferredHolder<T, ? extends T>> entries = new ArrayList<>();

    public DeferredRegister(ResourceKey<Registry<T>> registryKey, String namespace) {
        this.registryKey = registryKey;
        this.namespace = namespace;
    }

    public static <T> DeferredRegister<T> create(ResourceKey<Registry<T>> registryKey, String namespace) {
        return new DeferredRegister<>(registryKey, namespace);
    }

    @SuppressWarnings("unchecked")
    public static <T> DeferredRegister<T> create(Registry<T> registry, String namespace) {
        return new DeferredRegister<>((ResourceKey<Registry<T>>) (ResourceKey<?>) registry.key(), namespace);
    }

    public static Items createItems(String namespace) {
        return new Items(namespace);
    }

    public static Blocks createBlocks(String namespace) {
        return new Blocks(namespace);
    }

    public <I extends T> DeferredHolder<T, I> register(String name, Supplier<? extends I> supplier) {
        Identifier id = Identifier.fromNamespaceAndPath(namespace, name);
        ResourceKey<T> key = ResourceKey.create(registryKey, id);
        @SuppressWarnings("unchecked")
        DeferredHolder<T, I> holder = new DeferredHolder<>(registryKey, key, id, (Supplier<I>) supplier);
        entries.add(holder);
        return holder;
    }

    public <I extends T> DeferredHolder<T, I> register(String name, Function<Identifier, ? extends I> func) {
        Identifier id = Identifier.fromNamespaceAndPath(namespace, name);
        ResourceKey<T> key = ResourceKey.create(registryKey, id);
        @SuppressWarnings("unchecked")
        DeferredHolder<T, I> holder = new DeferredHolder<>(registryKey, key, id, () -> func.apply(id));
        entries.add(holder);
        return holder;
    }

    @SuppressWarnings("unchecked")
    public void register(IEventBus bus) {
        Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.getValue(registryKey.identifier());
        for (DeferredHolder<T, ? extends T> holder : entries) {
            T value = holder.get();
            if (registry != null) {
                Registry.register(registry, holder.getId(), value);
            }
        }
    }

    public Collection<DeferredHolder<T, ? extends T>> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    public static class Items extends DeferredRegister<Item> {
        public Items(String namespace) {
            super(Registries.ITEM, namespace);
        }

        @SuppressWarnings("unchecked")
        public <I extends Item> DeferredHolder<Item, I> registerSimpleBlockItem(DeferredHolder<Block, ? extends Block> blockHolder) {
            return (DeferredHolder<Item, I>) (Object) register(blockHolder.getId().getPath(), () -> new BlockItem(blockHolder.get(), new Item.Properties()));
        }

        @SuppressWarnings("unchecked")
        public <I extends Item> DeferredHolder<Item, I> registerSimpleBlockItem(DeferredHolder<Block, ? extends Block> blockHolder, Item.Properties properties) {
            return (DeferredHolder<Item, I>) (Object) register(blockHolder.getId().getPath(), () -> new BlockItem(blockHolder.get(), properties));
        }
    }

    public static class Blocks extends DeferredRegister<Block> {
        public Blocks(String namespace) {
            super(Registries.BLOCK, namespace);
        }
    }
}
