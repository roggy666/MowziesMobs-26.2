package net.neoforged.neoforge.common;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class Tags {
    public static class Blocks {
        public static final TagKey<Block> SANDS_COLORLESS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "sands/colorless"));
        public static final TagKey<Block> SANDS_RED = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "sands/red"));
        public static final TagKey<Block> ORES_IN_GROUND_DEEPSLATE = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores_in_ground/deepslate"));
        public static final TagKey<Block> ORES_IN_GROUND_NETHERRACK = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores_in_ground/netherrack"));
        public static final TagKey<Block> ORES_IN_GROUND_STONE = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores_in_ground/stone"));
        public static final TagKey<Block> STONES = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "stones"));
        public static final TagKey<Block> SANDSTONE_SLABS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "sandstone_slabs"));
        public static final TagKey<Block> SANDSTONE_BLOCKS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "sandstone_blocks"));
        public static final TagKey<Block> SANDS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "sands"));
        public static final TagKey<Block> ORES = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "ores"));
        public static final TagKey<Block> OBSIDIANS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "obsidians"));
        public static final TagKey<Block> NETHERRACKS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "netherracks"));
        public static final TagKey<Block> GRAVELS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "gravels"));
        public static final TagKey<Block> END_STONES = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "end_stones"));
        public static final TagKey<Block> COBBLESTONES = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "cobblestones"));
        public static final TagKey<Block> GLAZED_TERRACOTTAS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "glazed_terracottas"));
        public static final TagKey<Block> CONCRETES = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", "concretes"));
    }

    public static class Items {
        public static final TagKey<Item> RODS_WOODEN = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "rods/wooden"));
        public static final TagKey<Item> FEATHERS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "feathers"));
        public static final TagKey<Item> STRINGS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "strings"));
        public static final TagKey<Item> INGOTS_IRON = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "ingots/iron"));
        public static final TagKey<Item> TOOLS_TRIDENT = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "tools/trident"));
        public static final TagKey<Item> MUSIC_DISCS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "music_discs"));
        public static final TagKey<Item> SEEDS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "seeds"));
        public static final TagKey<Item> SLIME_BALLS = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "slime_balls"));
        public static final TagKey<Item> FOODS_RAW_MEAT = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "foods/raw_meat"));
        public static final TagKey<Item> FOODS_COOKED_MEAT = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "foods/cooked_meat"));
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> BOSSES = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("c", "bosses"));
        public static final TagKey<EntityType<?>> CAPTURING_NOT_SUPPORTED = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("c", "capturing_not_supported"));
        public static final TagKey<EntityType<?>> TELEPORTING_NOT_SUPPORTED = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("c", "teleporting_not_supported"));
    }

    public static class Biomes {
        public static final TagKey<Biome> IS_OVERWORLD = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_overworld"));
        public static final TagKey<Biome> IS_SAVANNA = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_savanna"));
        public static final TagKey<Biome> IS_OCEAN = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_ocean"));
        public static final TagKey<Biome> IS_RIVER = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_river"));
        public static final TagKey<Biome> IS_BEACH = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_beach"));
        public static final TagKey<Biome> IS_FOREST = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_forest"));
        public static final TagKey<Biome> IS_TAIGA = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_taiga"));
        public static final TagKey<Biome> IS_SNOWY = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_snowy"));
        public static final TagKey<Biome> IS_MOUNTAIN_PEAK = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_mountain_peak"));
        public static final TagKey<Biome> IS_JUNGLE = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_jungle"));
        public static final TagKey<Biome> IS_MOUNTAIN = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_mountain"));
        public static final TagKey<Biome> IS_MUSHROOM = TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath("c", "is_mushroom"));
    }
}
