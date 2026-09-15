package com.bobmowzie.mowziesmobs.server.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EnchantmentUtils {
    public static int getLevel(@NotNull final ResourceKey<Enchantment> enchantment, @NotNull final LivingEntity entity) {
        return entity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(enchantment).map(reference -> EnchantmentHelper.getEnchantmentLevel(reference, entity)).orElse(0);
    }

    public static int getLevel(@NotNull final ResourceKey<Enchantment> enchantment, @NotNull final Level level, @NotNull final ItemStack stack) {
        return level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(enchantment).map(ref -> EnchantmentHelper.getItemEnchantmentLevel(ref, stack)).orElse(0);
    }
}
