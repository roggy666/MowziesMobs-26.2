package net.neoforged.neoforge.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EventHooks {
    public static void onPlayerDestroyItem(Player player, ItemStack stack, InteractionHand hand) {
    }

    public static int onArrowLoose(ItemStack stack, Level level, Player player, int charge, boolean hasAmmo) {
        return charge;
    }

    public static boolean onLivingFall(LivingEntity entity, double distance, float damageMultiplier) {
        return false;
    }

    public static boolean checkMobDespawn(net.minecraft.world.entity.Mob mob) {
        return false;
    }
}
