package com.bobmowzie.mowziesmobs.server.block;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Function;

public final class BlockHandler {
    public static final Block PAINTED_ACACIA = registerBlockAndItem("painted_acacia", p -> new Block(p.strength(2.0F, 3.0F).sound(SoundType.WOOD)), Blocks.ACACIA_PLANKS);
    public static final Block PAINTED_ACACIA_SLAB = registerBlockAndItem("painted_acacia_slab", SlabBlock::new, PAINTED_ACACIA);
    public static final Block THATCH = registerBlockAndItem("thatch_block", HayBlock::new, Blocks.HAY_BLOCK);
    public static final Block GONG = registerBlockAndItem("gong", p -> new GongBlock(p.requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.ANVIL).pushReaction(PushReaction.DESTROY)), Blocks.GOLD_BLOCK);
    public static final Block GONG_PART = registerBlock("gong_part", p -> new GongBlock.GongPartBlock(p.requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.ANVIL).pushReaction(PushReaction.DESTROY)), Blocks.GOLD_BLOCK);
    public static final Block RAKED_SAND = registerBlockAndItem("raked_sand", p -> new RakedSandBlock(new ColorRGBA(14406560), p, Blocks.SAND.defaultBlockState()), Blocks.SAND);
    public static final Block RED_RAKED_SAND = registerBlockAndItem("red_raked_sand", p -> new RakedSandBlock(new ColorRGBA(11098145), p, Blocks.RED_SAND.defaultBlockState()), Blocks.RED_SAND);
    public static final Block CLAWED_LOG = registerBlockAndItem("clawed_log", Block::new, Blocks.ACACIA_PLANKS);

    private static Block registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory, Block copyFrom) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, MMCommon.resource(name));
        Block block = factory.apply(BlockBehaviour.Properties.ofFullCopy(copyFrom).setId(key));
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }

    private static Block registerBlockAndItem(String name, Function<BlockBehaviour.Properties, Block> factory, Block copyFrom) {
        Block block = registerBlock(name, factory, copyFrom);
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, MMCommon.resource(name));
        Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(itemKey)));
        return block;
    }

    public static void register() {
        FlammableBlockRegistry flammableRegistry = FlammableBlockRegistry.getDefaultInstance();
        flammableRegistry.add(PAINTED_ACACIA, 20, 5);
        flammableRegistry.add(PAINTED_ACACIA_SLAB, 20, 5);
        flammableRegistry.add(THATCH, 20, 60);
        flammableRegistry.add(CLAWED_LOG, 5, 5);
    }
}
