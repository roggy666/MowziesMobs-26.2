package com.bobmowzie.mowziesmobs.server.block.entity;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public final class BlockEntityHandler {
    public static final BlockEntityType<GongBlockEntity> GONG_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, MMCommon.resource("gong_entity"), new BlockEntityType<>(GongBlockEntity::new, Set.of(BlockHandler.GONG)));

    public static void register() {
    }
}
