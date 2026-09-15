package com.bobmowzie.mowziesmobs.server.inventory;

import com.bobmowzie.mowziesmobs.MMCommon;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public class ContainerHandler {
    public static final MenuType<ContainerUmvuthanaTrade> UMVUTHANA_TRADE = register("umvuthana_trade", new MenuType<>(ContainerUmvuthanaTrade::new, FeatureFlags.VANILLA_SET));
    public static final MenuType<ContainerUmvuthiTrade> UMVUTHI_TRADE = register("umvuthi_trade", new MenuType<>(ContainerUmvuthiTrade::new, FeatureFlags.VANILLA_SET));
    public static final MenuType<ContainerSculptorTrade> SCULPTOR_TRADE = register("sculptor_trade", new MenuType<>(ContainerSculptorTrade::new, FeatureFlags.VANILLA_SET));

    private static <T extends MenuType<?>> T register(String name, T type) {
        return Registry.register(BuiltInRegistries.MENU, MMCommon.resource(name), type);
    }

    public static void register() {
    }
}
