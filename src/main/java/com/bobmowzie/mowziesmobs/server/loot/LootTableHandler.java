package com.bobmowzie.mowziesmobs.server.loot;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootTable;

public class LootTableHandler {
    // Mob drops
    public static final ResourceKey<LootTable> FERROUS_WROUGHTNAUT = register("entities/ferrous_wroughtnaut");
    public static final ResourceKey<LootTable> LANTERN = register("entities/lantern");
    public static final ResourceKey<LootTable> NAGA = register("entities/naga");
    public static final ResourceKey<LootTable> FOLIAATH = register("entities/foliaath");
    public static final ResourceKey<LootTable> GROTTOL = register("entities/grottol");
    public static final ResourceKey<LootTable> FROSTMAW = register("entities/frostmaw");
    public static final ResourceKey<LootTable> UMVUTHANA_FURY = register("entities/umvuthana_fury");
    public static final ResourceKey<LootTable> UMVUTHANA_MISERY = register("entities/umvuthana_misery");
    public static final ResourceKey<LootTable> UMVUTHANA_BLISS = register("entities/umvuthana_bliss");
    public static final ResourceKey<LootTable> UMVUTHANA_RAGE = register("entities/umvuthana_rage");
    public static final ResourceKey<LootTable> UMVUTHANA_FEAR = register("entities/umvuthana_fear");
    public static final ResourceKey<LootTable> UMVUTHANA_FAITH = register("entities/umvuthana_faith");
    public static final ResourceKey<LootTable> UMVUTHI = register("entities/umvuthi");
    public static final ResourceKey<LootTable> UMVUTHANA_GROVE_CHEST = register("chests/umvuthana_grove_chest");
    public static final ResourceKey<LootTable> MONASTERY_CHEST = register("chests/monastery_chest");
    public static final ResourceKey<LootTable> SCULPTOR = register("entities/sculptor");
    public static final ResourceKey<LootTable> SCULPTOR_TEST = register("entities/sculptor_test");
    public static final ResourceKey<LootTable> BLUFF = register("entities/bluff");
    public static final ResourceKey<LootTable> ELOKOSA = register("entities/elokosa");

    public static final MapCodec<LootFunctionGrottolDeathType> GROTTOL_DEATH_TYPE = Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, MMCommon.resource("grottol_death_type"), LootFunctionGrottolDeathType.CODEC);
    public static final MapCodec<LootConditionFrostmawHasCrystal> FROSTMAW_HAS_CRYSTAL = Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, MMCommon.resource("has_crystal"), LootConditionFrostmawHasCrystal.CODEC);
    public static final MapCodec<LootConditionElokosaNightForm> ELOKOSA_NIGHT_FORM = Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, MMCommon.resource("night_form"), LootConditionElokosaNightForm.CODEC);
    public static final MapCodec<LootConditionMoonPhase> MOON_PHASE = Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, MMCommon.resource("moon_phase"), LootConditionMoonPhase.CODEC);

    public static void register() {
    }

    private static ResourceKey<LootTable> register(String id) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(MMCommon.MODID, id));
    }
}
