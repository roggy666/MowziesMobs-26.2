package com.bobmowzie.mowziesmobs.server.world.feature.structure.processor;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;

public class ProcessorHandler {
    public static final MapCodec<? extends StructureProcessor> BASE_PROCESSOR = register("base_processor", BaseProcessor.CODEC);
    public static final MapCodec<? extends StructureProcessor> BLOCK_SWAP_PROCESSOR = register("block_swap_processor", BlockSwapProcessor.CODEC);
    public static final MapCodec<? extends StructureProcessor> ROOTS_PROCESSOR = register("roots_processor", RootsProcessor.CODEC);
    public static final MapCodec<? extends StructureProcessor> BASE_DECO_PROCESSOR = register("base_deco_processor", BaseDecoProcessor.CODEC);
    public static final MapCodec<? extends StructureProcessor> CHEST_PROCESSOR = register("chest_processor", ChestProcessor.CODEC);
    public static final MapCodec<? extends StructureProcessor> STAIRS_PROCESSOR = register("stairs_processor", MonasteryStairsProcessor.CODEC);

    private static <P extends StructureProcessor> MapCodec<P> register(String name, MapCodec<P> codec) {
        return Registry.register(BuiltInRegistries.STRUCTURE_PROCESSOR, MMCommon.resource(name), codec);
    }

    public static void register() {
    }
}
