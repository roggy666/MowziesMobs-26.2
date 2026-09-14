package com.bobmowzie.mowziesmobs.server.block;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class BlockHandler {

    public static final DeferredRegister<Block> REG = DeferredRegister.create(Registries.BLOCK, MMCommon.MODID);

    public static final DeferredHolder<Block, Block> PAINTED_ACACIA = registerBlockAndItem("painted_acacia", () -> new Block(Block.Properties.ofFullCopy(Blocks.ACACIA_PLANKS).setId(ResourceKey.create(Registries.BLOCK, MMCommon.resource("painted_acacia"))).strength(2.0F, 3.0F).sound(SoundType.WOOD)));
    public static final DeferredHolder<Block, Block> PAINTED_ACACIA_SLAB = registerBlockAndItem("painted_acacia_slab", () -> new SlabBlock(Block.Properties.ofFullCopy(PAINTED_ACACIA.get()).setId(ResourceKey.create(Registries.BLOCK, MMCommon.resource("painted_acacia_slab")))));
    public static final DeferredHolder<Block, Block> THATCH = registerBlockAndItem("thatch_block", () -> new HayBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HAY_BLOCK).setId(ResourceKey.create(Registries.BLOCK, MMCommon.resource("thatch_block")))));
    public static final DeferredHolder<Block, Block> GONG = registerBlockAndItem("gong", () -> new GongBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).setId(ResourceKey.create(Registries.BLOCK, MMCommon.resource("gong"))).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.ANVIL).pushReaction(PushReaction.DESTROY)));
    public static final DeferredHolder<Block, Block> GONG_PART = REG.register("gong_part", () -> new GongBlock.GongPartBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).setId(ResourceKey.create(Registries.BLOCK, MMCommon.resource("gong_part"))).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.ANVIL).pushReaction(PushReaction.DESTROY)));
    public static final DeferredHolder<Block, Block> RAKED_SAND = registerBlockAndItem("raked_sand", () -> new RakedSandBlock(new ColorRGBA(14406560), BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).setId(ResourceKey.create(Registries.BLOCK, MMCommon.resource("raked_sand"))), Blocks.SAND.defaultBlockState()));
    public static final DeferredHolder<Block, Block> RED_RAKED_SAND = registerBlockAndItem("red_raked_sand", () -> new RakedSandBlock(new ColorRGBA(11098145), BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SAND).setId(ResourceKey.create(Registries.BLOCK, MMCommon.resource("red_raked_sand"))), Blocks.RED_SAND.defaultBlockState()));
    public static final DeferredHolder<Block, Block> CLAWED_LOG = registerBlockAndItem("clawed_log", () -> new Block(Block.Properties.ofFullCopy(Blocks.ACACIA_PLANKS).setId(ResourceKey.create(Registries.BLOCK, MMCommon.resource("clawed_log")))));

    public static DeferredHolder<Block, Block> registerBlockAndItem(String name, Supplier<Block> block){
        DeferredHolder<Block, Block> blockObj = REG.register(name, block);
        ItemHandler.REG.register(name, () -> new BlockItem(blockObj.get(), new Item.Properties().setId(net.minecraft.resources.ResourceKey.create(Registries.ITEM, MMCommon.resource(name)))));
        return blockObj;
    }

    public static void init() {
        var flammableRegistry = net.fabricmc.fabric.api.registry.FlammableBlockRegistry.getDefaultInstance();
        flammableRegistry.add(PAINTED_ACACIA.get(), 20, 5);
        flammableRegistry.add(PAINTED_ACACIA_SLAB.get(), 20, 5);
        flammableRegistry.add(THATCH.get(), 20, 60);
        flammableRegistry.add(CLAWED_LOG.get(), 5, 5);
    }
}