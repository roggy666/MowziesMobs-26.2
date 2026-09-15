package com.bobmowzie.mowziesmobs.server.advancement;

import com.bobmowzie.mowziesmobs.MMCommon;
import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class AdvancementHandler {
    public static final StealIceCrystalTrigger STEAL_ICE_CRYSTAL_TRIGGER = register("steal_ice_crystal", new StealIceCrystalTrigger());
    public static final GrottolKillFortuneTrigger GROTTOL_KILL_FORTUNE_TRIGGER = register("kill_grottol_fortune", new GrottolKillFortuneTrigger());
    public static final GrottolKillSilkTouchTrigger GROTTOL_KILL_SILK_TOUCH_TRIGGER = register("kill_grottol_silk_touch", new GrottolKillSilkTouchTrigger());
    public static final SneakGroveTrigger SNEAK_VILLAGE_TRIGGER = register("sneak_grove", new SneakGroveTrigger());
    public static final SculptorChallengeTrigger SCULPTOR_CHALLENGE_TRIGGER = register("sculptor_challenge", new SculptorChallengeTrigger());
    public static final SculptorFailureTrigger SCULPTOR_FAILURE_TRIGGER = register("sculptor_failure", new SculptorFailureTrigger());

    private static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, MMCommon.resource(name), trigger);
    }

    public static void register() {
    }
}
