package com.bobmowzie.mowziesmobs.server.world.spawn;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.world.BiomeChecker;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.HashMap;
import java.util.Map;

public class SpawnHandler {
    public static final Map<EntityType<?>, ConfigHandler.SpawnConfig> SPAWN_CONFIGS = new HashMap<>();

    public static BiomeChecker FOLIAATH_BIOME_CHECKER;
    public static BiomeChecker UMVUTHANA_RAPTOR_BIOME_CHECKER;
    public static BiomeChecker GROTTOL_BIOME_CHECKER;
    public static BiomeChecker LANTERN_BIOME_CHECKER;
    public static BiomeChecker NAGA_BIOME_CHECKER;
    public static BiomeChecker BLUFF_BIOME_CHECKER;
    public static BiomeChecker ELOKOSA_HOWLER_BIOME_CHECKER;

    private static final SpawnPlacementType MM_SPAWN = (level, position, type) -> {
        BlockState below = level.getBlockState(position.below());

        if (type == null || below.is(Blocks.BEDROCK) || below.is(Blocks.BARRIER) || !below.blocksMotion()) {
            return false;
        }

        BlockState state = level.getBlockState(position);

        if (!NaturalSpawner.isValidEmptySpawnBlock(level, position, state, state.getFluidState(), type)) {
            return false;
        }

        BlockState up = level.getBlockState(position.above());

        return NaturalSpawner.isValidEmptySpawnBlock(level, position.above(), up, up.getFluidState(), type);
    };

    static {
        SPAWN_CONFIGS.put(EntityHandler.FOLIAATH.get(), ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig);
        SPAWN_CONFIGS.put(EntityHandler.UMVUTHANA_RAPTOR.get(), ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig);
        SPAWN_CONFIGS.put(EntityHandler.LANTERN.get(), ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig);
        SPAWN_CONFIGS.put(EntityHandler.NAGA.get(), ConfigHandler.COMMON.MOBS.NAGA.spawnConfig);
        SPAWN_CONFIGS.put(EntityHandler.GROTTOL.get(), ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig);
        SPAWN_CONFIGS.put(EntityHandler.BLUFF.get(), ConfigHandler.COMMON.MOBS.BLUFF.spawnConfig);
        SPAWN_CONFIGS.put(EntityHandler.ELOKOSA_HOWLER.get(), ConfigHandler.COMMON.MOBS.ELOKOSA.spawnConfig);
    }

    public static void registerSpawnPlacementTypes() {
        SpawnPlacements.register(EntityHandler.FOLIAATH.get(), MM_SPAWN, Heightmap.Types.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
        SpawnPlacements.register(EntityHandler.LANTERN.get(), MM_SPAWN, Heightmap.Types.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
        SpawnPlacements.register(EntityHandler.UMVUTHANA_RAPTOR.get(), MM_SPAWN, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
        SpawnPlacements.register(EntityHandler.NAGA.get(), MM_SPAWN, Heightmap.Types.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
        SpawnPlacements.register(EntityHandler.GROTTOL.get(), MM_SPAWN, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
        SpawnPlacements.register(EntityHandler.UMVUTHANA_CRANE.get(), MM_SPAWN, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
        SpawnPlacements.register(EntityHandler.BLUFF.get(), MM_SPAWN, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MowzieEntity::spawnPredicate);
        SpawnPlacements.register(EntityHandler.ELOKOSA_HOWLER.get(), MM_SPAWN, Heightmap.Types.MOTION_BLOCKING, MowzieEntity::spawnPredicate);
    }

    public static void initBiomeSpawns() {
        if (FOLIAATH_BIOME_CHECKER == null) FOLIAATH_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig.biomeConfig);
        registerEntityWorldSpawn(EntityHandler.FOLIAATH.get(), ConfigHandler.COMMON.MOBS.FOLIAATH.spawnConfig, FOLIAATH_BIOME_CHECKER, MobCategory.MONSTER);

        if (UMVUTHANA_RAPTOR_BIOME_CHECKER == null) UMVUTHANA_RAPTOR_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig.biomeConfig);
        registerEntityWorldSpawn(EntityHandler.UMVUTHANA_RAPTOR.get(), ConfigHandler.COMMON.MOBS.UMVUTHANA.spawnConfig, UMVUTHANA_RAPTOR_BIOME_CHECKER, MobCategory.MONSTER);

        if (GROTTOL_BIOME_CHECKER == null) GROTTOL_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig.biomeConfig);
        registerEntityWorldSpawn(EntityHandler.GROTTOL.get(), ConfigHandler.COMMON.MOBS.GROTTOL.spawnConfig, GROTTOL_BIOME_CHECKER, MobCategory.MONSTER);

        if (LANTERN_BIOME_CHECKER == null) LANTERN_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig.biomeConfig);
        registerEntityWorldSpawn(EntityHandler.LANTERN.get(), ConfigHandler.COMMON.MOBS.LANTERN.spawnConfig, LANTERN_BIOME_CHECKER, MobCategory.AMBIENT);

        if (NAGA_BIOME_CHECKER == null) NAGA_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.NAGA.spawnConfig.biomeConfig);
        registerEntityWorldSpawn(EntityHandler.NAGA.get(), ConfigHandler.COMMON.MOBS.NAGA.spawnConfig, NAGA_BIOME_CHECKER, MobCategory.MONSTER);

        if (BLUFF_BIOME_CHECKER == null) BLUFF_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.BLUFF.spawnConfig.biomeConfig);
        registerEntityWorldSpawn(EntityHandler.BLUFF.get(), ConfigHandler.COMMON.MOBS.BLUFF.spawnConfig, BLUFF_BIOME_CHECKER, MobCategory.MONSTER);

        if (ELOKOSA_HOWLER_BIOME_CHECKER == null) ELOKOSA_HOWLER_BIOME_CHECKER = new BiomeChecker(ConfigHandler.COMMON.MOBS.ELOKOSA.spawnConfig.biomeConfig);
        registerEntityWorldSpawn(EntityHandler.ELOKOSA_HOWLER.get(), ConfigHandler.COMMON.MOBS.ELOKOSA.spawnConfig, ELOKOSA_HOWLER_BIOME_CHECKER, MobCategory.MONSTER);
    }

    private static void registerEntityWorldSpawn(EntityType<?> entity, ConfigHandler.SpawnConfig spawnConfig, BiomeChecker checker, MobCategory classification) {
        if (spawnConfig.spawnRate.get() <= 0) return;
        BiomeModifications.addSpawn(
                ctx -> checker.isBiomeInConfig(ctx.getBiomeHolder()),
                classification,
                entity,
                spawnConfig.spawnRate.get(),
                spawnConfig.minGroupSize.get(),
                spawnConfig.maxGroupSize.get()
        );
    }
}