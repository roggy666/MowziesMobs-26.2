package net.neoforged.neoforge.common;

import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class CommonHooks {
    public static boolean onPlayerAttackTarget(Player player, Entity target) {
        return true;
    }

    @SuppressWarnings("unchecked")
    public static <T> HolderLookup.RegistryLookup<T> resolveLookup(ResourceKey<? extends Registry<? extends T>> key) {
        try {
            var mc = Minecraft.getInstance();
            if (mc != null && mc.level != null) {
                return (HolderLookup.RegistryLookup<T>) mc.level.registryAccess().lookup((ResourceKey) key).orElse(null);
            }
        } catch (Throwable ignored) {}
        return null;
    }
}
