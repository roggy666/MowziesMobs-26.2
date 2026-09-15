package com.bobmowzie.mowziesmobs.server.creativetab;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class CreativeTabHandler {
    public static final CreativeModeTab CREATIVE_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, MMCommon.resource("mowziesmobs_tab"), FabricCreativeModeTab.builder()
            .icon(() -> ItemHandler.LOGO.getDefaultInstance())
            .title(Component.translatable("itemGroup.mowziesmobs.creativeTab"))
            .displayItems((displayParams, output) -> {
                for (Item item : BuiltInRegistries.ITEM) {
                    if (item == ItemHandler.LOGO) continue;
                    if (BuiltInRegistries.ITEM.getKey(item).getNamespace().equals(MMCommon.MODID)) {
                        output.accept(item);
                    }
                }
            })
            .build());

    public static void register() {
    }
}
