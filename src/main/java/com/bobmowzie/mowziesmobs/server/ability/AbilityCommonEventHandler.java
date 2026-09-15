package com.bobmowzie.mowziesmobs.server.ability;

import com.bobmowzie.mowziesmobs.server.capability.AbilityData;
import com.bobmowzie.mowziesmobs.server.capability.DataHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Forwards player interaction hooks to the abilities of the acting entity. All methods are invoked from
 * {@link com.bobmowzie.mowziesmobs.server.ServerEventHandler}.
 */
public final class AbilityCommonEventHandler {
    private AbilityCommonEventHandler() {}

    public static void register() {
    }

    public static void onPlayerRightClickEmpty(Player player, InteractionHand hand) {
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onRightClickEmpty(player, hand);
            }
        }
    }

    public static void onPlayerRightClickBlock(Player player, InteractionHand hand, BlockHitResult hitResult) {
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onRightClickBlock(player, hand, hitResult);
            }
        }
    }

    public static void onPlayerRightClickItem(Player player, InteractionHand hand) {
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onRightClickWithItem(player, hand);
            }
        }
    }

    public static void onPlayerRightClickEntity(Player player, InteractionHand hand, Entity target) {
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onRightClickEntity(player, hand, target);
            }
        }
    }

    public static void onPlayerLeftClickEmpty(Player player) {
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onLeftClickEmpty(player);
            }
        }
    }

    public static void onPlayerLeftClickBlock(Player player, BlockPos pos, Direction direction) {
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onLeftClickBlock(player, pos, direction);
            }
        }
    }

    public static void onLeftClickEntity(Player player, Entity target) {
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onLeftClickEntity(player, target);
            }
        }
    }

    public static void onTakeDamage(LivingEntity entity, DamageSource source, float damage) {
        AbilityData data = DataHandler.getData(entity, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            ability.onTakeDamage(source, damage);
        }
    }

    public static void onJump(LivingEntity entity) {
        if (!(entity instanceof Player player)) return;
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                ((PlayerAbility) ability).onJump(player);
            }
        }
    }

    /** Returns the modified fall damage multiplier. */
    public static float onFall(LivingEntity entity, double distance, float damageMultiplier) {
        if (!(entity instanceof Player player)) return damageMultiplier;
        AbilityData data = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        for (Ability<?> ability : data.getAbilities()) {
            if (ability instanceof PlayerAbility) {
                damageMultiplier = ((PlayerAbility) ability).onFall(player, distance, damageMultiplier);
            }
        }
        return damageMultiplier;
    }
}
