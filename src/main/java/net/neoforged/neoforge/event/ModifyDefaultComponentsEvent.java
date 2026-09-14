package net.neoforged.neoforge.event;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.Event;

import java.util.function.Consumer;

public class ModifyDefaultComponentsEvent extends Event {
    public void modify(Item item, Consumer<DataComponentMap.Builder> consumer) {
        // Stub for default component modifications
    }
}
