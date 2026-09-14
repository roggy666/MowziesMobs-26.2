package net.neoforged.neoforge.client.extensions.common;

import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.Event;

public class RegisterClientExtensionsEvent extends Event {
    public void registerItem(IClientItemExtensions extension, ItemLike... items) {
        for (ItemLike item : items) {
            IClientItemExtensions.REGISTRY.put(item, extension);
        }
    }
}
