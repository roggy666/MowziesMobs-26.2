package com.bobmowzie.mowziesmobs.server.world.feature.structure;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.HashSet;
import java.util.Set;

public class StructureTypeHandler {
    public static final StructureType<WroughtnautChamberStructure> WROUGHTNAUT_CHAMBER = registerStructureType("wrought_chamber", () -> WroughtnautChamberStructure.CODEC);
    public static final StructurePieceType WROUGHTNAUT_CHAMBER_PIECE = registerStructurePieceType("wrought_chamber_template", WroughtnautChamberPieces.Piece::new);

    public static final StructureType<UmvuthanaGroveStructure> UMVUTHANA_GROVE = registerStructureType("umvuthana_grove", () -> UmvuthanaGroveStructure.CODEC);
    public static final StructurePieceType UMVUTHANA_GROVE_PIECE = registerStructurePieceType("umvuthana_grove_template", UmvuthanaGrovePieces.Piece::new);
    public static final StructurePieceType UMVUTHANA_FIREPIT = registerStructurePieceType("umvuthana_firepit", UmvuthanaGrovePieces.FirepitPiece::new);

    public static final StructureType<FrostmawStructure> FROSTMAW = registerStructureType("frostmaw_spawn", () -> FrostmawStructure.CODEC);
    public static final StructurePieceType FROSTMAW_PIECE = registerStructurePieceType("frostmaw_template", FrostmawPieces.FrostmawPiece::new);

    public static final StructureType<MonasteryStructure> MONASTERY = registerStructureType("monastery", () -> MonasteryStructure.CODEC);

    public static BiomeChecker FERROUS_WROUGHTNAUT_BIOME_CHECKER;
    public static final Set<Holder<Biome>> FERROUS_WROUGHTNAUT_BIOMES = new HashSet<>();
    public static BiomeChecker UMVUTHI_BIOME_CHECKER;
    public static final Set<Holder<Biome>> UMVUTHI_BIOMES = new HashSet<>();
    public static BiomeChecker FROSTMAW_BIOME_CHECKER;
    public static final Set<Holder<Biome>> FROSTMAW_BIOMES = new HashSet<>();
    public static BiomeChecker SCULPTOR_BIOME_CHECKER;
    public static final Set<Holder<Biome>> SCULPTOR_BIOMES = new HashSet<>();

    private static <T extends Structure> StructureType<T> registerStructureType(String name, StructureType<T> structure) {
        return Registry.register(BuiltInRegistries.STRUCTURE_TYPE, MMCommon.resource(name), structure);
    }

    private static StructurePieceType registerStructurePieceType(String name, StructurePieceType structurePieceType) {
        return Registry.register(BuiltInRegistries.STRUCTURE_PIECE, MMCommon.resource(name), structurePieceType);
    }

    public static void register() {
        BiomeModifications.create(MMCommon.resource("structure_biomes")).add(ModificationPhase.ADDITIONS, ctx -> true, (ctx, modifier) -> addBiomeSpawns(ctx.getBiomeHolder()));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            FERROUS_WROUGHTNAUT_BIOMES.clear();
            UMVUTHI_BIOMES.clear();
            FROSTMAW_BIOMES.clear();
            SCULPTOR_BIOMES.clear();
        });
    }

    public static void addBiomeSpawns(Holder<Biome> biomeKey) {
        if (FERROUS_WROUGHTNAUT_BIOME_CHECKER == null) FERROUS_WROUGHTNAUT_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.FERROUS_WROUGHTNAUT.generationConfig.generationDistance.get() >= 0 && FERROUS_WROUGHTNAUT_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
            //System.out.println("Added Ferrous Wroughtnaut biome: " + biomeName.toString());
            FERROUS_WROUGHTNAUT_BIOMES.add(biomeKey);
        }

        if (UMVUTHI_BIOME_CHECKER == null) UMVUTHI_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.UMVUTHI.generationConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.UMVUTHI.generationConfig.generationDistance.get() >= 0 && UMVUTHI_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
            //System.out.println("Added Barako biome: " + biomeName.toString());
            UMVUTHI_BIOMES.add(biomeKey);
        }

        if (FROSTMAW_BIOME_CHECKER == null) FROSTMAW_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.FROSTMAW.generationConfig.generationDistance.get() >= 0 && FROSTMAW_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
//            System.out.println("Added frostmaw biome: " + biomeKey.toString());
            FROSTMAW_BIOMES.add(biomeKey);
        }

        if (SCULPTOR_BIOME_CHECKER == null) SCULPTOR_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.SCULPTOR.generationConfig.biomeConfig);
        if (ConfigHandler.COMMON.MOBS.SCULPTOR.generationConfig.generationDistance.get() >= 0 && SCULPTOR_BIOME_CHECKER.isBiomeInConfig(biomeKey)) {
            //System.out.println("Added frostmaw biome: " + biomeName.toString());
            SCULPTOR_BIOMES.add(biomeKey);
        }
    }
}
