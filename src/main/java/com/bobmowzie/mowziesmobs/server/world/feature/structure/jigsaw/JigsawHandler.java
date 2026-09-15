package com.bobmowzie.mowziesmobs.server.world.feature.structure.jigsaw;

import com.bobmowzie.mowziesmobs.MMCommon;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType;

public class JigsawHandler {
    public static final StructurePoolElementType<MowziePoolElement> MOWZIE_ELEMENT = register("mowzie_element", () -> MowziePoolElement.CODEC);
    public static final StructurePoolElementType<FallbackPoolElement> FALLBACK_ELEMENT = register("fallback_element", () -> FallbackPoolElement.CODEC);

    private static <T extends StructurePoolElementType<?>> T register(String name, T type) {
        return Registry.register(BuiltInRegistries.STRUCTURE_POOL_ELEMENT, MMCommon.resource(name), type);
    }

    public static void register() {
    }
}
