package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

/**
 * Created by BobMowzie on 1/10/2019.
 */
public final class PotionTypeHandler {
    private PotionTypeHandler() {}

    public static final Holder<Potion> POISON_RESIST = register("poison_resist", new Potion("poison_resist", new MobEffectInstance(EffectHandler.POISON_RESIST, 3600)));
    public static final Holder<Potion> LONG_POISON_RESIST = register("long_poison_resist", new Potion("long_poison_resist", new MobEffectInstance(EffectHandler.POISON_RESIST, 9600)));

    private static Holder<Potion> register(String name, Potion potion) {
        return Registry.registerForHolder(BuiltInRegistries.POTION, MMCommon.resource(name), potion);
    }

    public static void register() {
        FabricPotionBrewingBuilder.BUILD.register(builder -> {
            builder.addMix(Potions.AWKWARD, ItemHandler.NAGA_FANG, POISON_RESIST);
            builder.addMix(POISON_RESIST, Items.REDSTONE, LONG_POISON_RESIST);
        });
    }
}
