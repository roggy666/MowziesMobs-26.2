package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MMCommon;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public final class EffectHandler {
    public static final Holder<MobEffect> SUNS_BLESSING = register("suns_blessing", new EffectSunsBlessing());
    public static final Holder<MobEffect> GEOMANCY = register("geomancy", new EffectGeomancy());
    public static final Holder<MobEffect> FROZEN = register("frozen", new EffectFrozen());
    public static final Holder<MobEffect> POISON_RESIST = register("poison_resist", new EffectPoisonResist());
    public static final Holder<MobEffect> SUNBLOCK = register("sunblock", new EffectSunblock());
    public static final Holder<MobEffect> MOONS_CURSE = register("moons_curse", new EffectMoonsCurse());
    public static final Holder<MobEffect> FRAGILITY = register("fragility", new EffectFragility());
    public static final Holder<MobEffect> ECLIPSED = register("eclipsed", new EffectEclipsed());

    private static Holder<MobEffect> register(String name, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, MMCommon.resource(name), effect);
    }

    public static void register() {
    }

    public static void addOrCombineEffect(LivingEntity entity, Holder<MobEffect> effect, int duration, int amplifier, boolean ambient, boolean showParticles) {
        if (effect == null) return;
        MobEffectInstance effectInst = entity.getEffect(effect);
        MobEffectInstance newEffect = new MobEffectInstance(effect, duration, amplifier, ambient, showParticles);
        if (effectInst != null) effectInst.update(newEffect);
        else entity.addEffect(newEffect);
    }
}
