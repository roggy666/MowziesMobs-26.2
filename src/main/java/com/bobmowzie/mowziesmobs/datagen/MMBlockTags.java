package com.bobmowzie.mowziesmobs.datagen;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MMBlockTags extends FabricTagsProvider.BlockTagsProvider {
    public static final TagKey<Block> GEOMANCY_USEABLE = key("geomancy_useable");
    public static final TagKey<Block> CAN_GROTTOL_DIG = key("can_grottol_dig");
    public static final TagKey<Block> GEOMANCY_TUNNELABLE = key("geomancy_tunnelable");

    public MMBlockTags(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    public static class MMTagAppender implements net.minecraft.data.tags.TagAppender<Block> {
        private final net.minecraft.data.tags.TagAppender<Block> appender;

        public MMTagAppender(net.minecraft.data.tags.TagAppender<Block> appender) {
            this.appender = appender;
        }

        public MMTagAppender add(Block block) {
            this.appender.add(block.builtInRegistryHolder().key());
            return this;
        }

        public MMTagAppender add(Block... blocks) {
            for (Block block : blocks) {
                this.appender.add(block.builtInRegistryHolder().key());
            }
            return this;
        }

        @Override
        public MMTagAppender add(net.minecraft.resources.ResourceKey<Block> element) {
            this.appender.add(element);
            return this;
        }

        @Override
        public MMTagAppender addOptional(net.minecraft.resources.ResourceKey<Block> element) {
            this.appender.addOptional(element);
            return this;
        }

        @Override
        public MMTagAppender addTag(TagKey<Block> tag) {
            this.appender.addTag(tag);
            return this;
        }

        @Override
        public MMTagAppender addOptionalTag(TagKey<Block> tag) {
            this.appender.addOptionalTag(tag);
            return this;
        }
    }

    protected MMTagAppender tag(TagKey<Block> tag) {
        return new MMTagAppender(builder(tag));
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        addToVanillaTags();
        addToCommonTags();

        // These blocks are considered bricks
        TagKey<Block> bricks = key("bricks");
        tagBricks(bricks);

        // These blocks are considered concrete
        TagKey<Block> concrete = key("concrete");
        tagConcrete(concrete);

        // These blocks are considered concrete
        TagKey<Block> concretePowder = key("concrete_powder");
        tagConcretePowder(concretePowder);

        // These blocks can be used with Geomancy abilities
        // Blocks that copy their properties from these tagged blocks will also be useable
        tag(GEOMANCY_USEABLE).addTag(bricks).addTag(concrete).addTag(concretePowder);
        tagGeomancyUsable();

        // These blocks allow grottols to dig and disappear
        // Blocks that copy their properties from these tagged blocks will also be allowed
        tag(CAN_GROTTOL_DIG).addTag(GEOMANCY_USEABLE);

        // These blocks can be used with Geomancy abilities. Blocks that copy their properties from these tagged blocks will also be useable.
        tag(GEOMANCY_TUNNELABLE)
                .addTag(GEOMANCY_USEABLE)
                .add(Blocks.SNOW)
                .add(Blocks.SNOW_BLOCK)
                .add(Blocks.PACKED_ICE);
    }

    private void addToVanillaTags() {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BlockHandler.GONG)
                .add(BlockHandler.GONG_PART);

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(BlockHandler.RAKED_SAND)
                .add(BlockHandler.RED_RAKED_SAND);

        tag(BlockTags.AZALEA_GROWS_ON)
                .add(BlockHandler.RAKED_SAND)
                .add(BlockHandler.RED_RAKED_SAND);

        tag(BlockTags.AZALEA_GROWS_ON)
                .add(BlockHandler.RAKED_SAND)
                .add(BlockHandler.RED_RAKED_SAND);

        tag(BlockTags.SUPPORTS_BAMBOO)
                .add(BlockHandler.RAKED_SAND)
                .add(BlockHandler.RED_RAKED_SAND);

        tag(BlockTags.LUSH_GROUND_REPLACEABLE)
                .add(BlockHandler.RAKED_SAND)
                .add(BlockHandler.RED_RAKED_SAND);

        tag(BlockTags.PLANKS).add(BlockHandler.PAINTED_ACACIA);
        tag(BlockTags.JUNGLE_LOGS).add(BlockHandler.CLAWED_LOG);
        tag(BlockTags.WOODEN_SLABS).add(BlockHandler.PAINTED_ACACIA_SLAB);
    }

    private void addToCommonTags() {
        tag(ConventionalBlockTags.COLORLESS_SANDS).add(BlockHandler.RAKED_SAND);
        tag(ConventionalBlockTags.RED_SANDS).add(BlockHandler.RED_RAKED_SAND);
    }

    private void tagGeomancyUsable() {
        tag(GEOMANCY_USEABLE)
                .addTag(BlockTags.DIRT)
                .addTag(BlockTags.TERRACOTTA)
                .addTag(BlockTags.NYLIUM)
                .addTag(ConventionalBlockTags.GLAZED_TERRACOTTAS)
                .addTag(ConventionalBlockTags.COBBLESTONES)
                .addTag(ConventionalBlockTags.END_STONES)
                .addTag(ConventionalBlockTags.GRAVELS)
                .addTag(ConventionalBlockTags.NETHERRACKS)
                .addTag(ConventionalBlockTags.OBSIDIANS)
                .addTag(ConventionalBlockTags.ORES)
                .addTag(ConventionalBlockTags.SANDS)
                .addTag(ConventionalBlockTags.SANDSTONE_BLOCKS)
                .addTag(ConventionalBlockTags.SANDSTONE_SLABS)
                .addTag(ConventionalBlockTags.STONES)
                .add(Blocks.BLACKSTONE)
                .add(Blocks.BASALT)
                .add(Blocks.SMOOTH_BASALT)
                .add(Blocks.SOUL_SAND)
                .add(Blocks.SOUL_SOIL)
                .add(Blocks.SMOOTH_STONE)
                .add(Blocks.QUARTZ_BLOCK)
                .add(Blocks.CHISELED_QUARTZ_BLOCK)
                .add(Blocks.QUARTZ_PILLAR)
                .add(Blocks.SMOOTH_QUARTZ)
                .add(Blocks.PURPUR_BLOCK)
                .add(Blocks.PURPUR_PILLAR)
                .add(Blocks.PRISMARINE)
                .add(Blocks.DARK_PRISMARINE)
                .add(Blocks.SUSPICIOUS_SAND)
                .add(Blocks.SUSPICIOUS_GRAVEL)
                .add(Blocks.MAGMA_BLOCK)
                .add(Blocks.DRIPSTONE_BLOCK)
                .add(Blocks.CLAY)
                .add(Blocks.DEEPSLATE_TILES)
                .add(Blocks.POLISHED_BASALT)
                .add(Blocks.DIRT_PATH)
                .add(Blocks.FARMLAND)
                .add(Blocks.CHISELED_POLISHED_BLACKSTONE)
                .add(Blocks.CRACKED_DEEPSLATE_BRICKS)
                .add(Blocks.STONE_SLAB)
                .add(Blocks.COBBLESTONE_SLAB)
                .add(Blocks.SMOOTH_STONE_SLAB)
                .add(Blocks.STONE_BRICK_SLAB)
                .add(Blocks.DEEPSLATE_TILE_SLAB)
                .add(Blocks.CUT_RED_SANDSTONE_SLAB)
                .add(Blocks.BRICK_SLAB)
                .add(Blocks.PRISMARINE_SLAB)
                .add(Blocks.PRISMARINE_BRICK_SLAB)
                .add(Blocks.DARK_PRISMARINE_SLAB)
                .add(Blocks.MUD_BRICK_SLAB)
                .add(Blocks.SANDSTONE_SLAB)
                .add(Blocks.NETHER_BRICK_SLAB)
                .add(Blocks.QUARTZ_SLAB)
                .add(Blocks.POLISHED_BLACKSTONE_SLAB)
                .add(Blocks.PURPUR_SLAB)
                .add(Blocks.NETHER_BRICK_FENCE)
                .add(Blocks.POLISHED_BLACKSTONE_STAIRS)
                .add(Blocks.POLISHED_BLACKSTONE_WALL);
    }

    private void tagConcretePowder(TagKey<Block> key) {
        Blocks.CONCRETE_POWDER.asList().forEach(block -> tag(key).add(block));
    }

    private void tagConcrete(TagKey<Block> key) {
        tag(key).addTag(ConventionalBlockTags.CONCRETES);
    }

    private void tagBricks(TagKey<Block> key) {
        tag(key)
                .addTag(BlockTags.STONE_BRICKS)
                .add(Blocks.BRICKS)
                .add(Blocks.DEEPSLATE_BRICKS)
                .add(Blocks.CRACKED_DEEPSLATE_BRICKS)
                .add(Blocks.MUD_BRICKS)
                .add(Blocks.PRISMARINE_BRICKS)
                .add(Blocks.NETHER_BRICKS)
                .add(Blocks.CRACKED_NETHER_BRICKS)
                .add(Blocks.CHISELED_NETHER_BRICKS)
                .add(Blocks.RED_NETHER_BRICKS)
                .add(Blocks.POLISHED_BLACKSTONE_BRICKS)
                .add(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS)
                .add(Blocks.END_STONE_BRICKS)
                .add(Blocks.QUARTZ_BRICKS)
                .add(Blocks.INFESTED_STONE_BRICKS)
                .add(Blocks.INFESTED_MOSSY_STONE_BRICKS)
                .add(Blocks.INFESTED_CRACKED_STONE_BRICKS)
                .add(Blocks.INFESTED_CHISELED_STONE_BRICKS);
    }

    private static TagKey<Block> key(String path) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MMCommon.MODID, path));
    }
}
