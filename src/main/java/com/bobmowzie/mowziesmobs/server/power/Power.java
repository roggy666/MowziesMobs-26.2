package com.bobmowzie.mowziesmobs.server.power;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import com.bobmowzie.mowziesmobs.server.capability.PlayerData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public abstract class Power {

    private final PlayerData data;

    public Power(PlayerData data) {
        this.data = data;
    }

    public void tick(Player player) {

    }

    public void onRightClickEmpty(Player player, InteractionHand hand) {

    }

    public void onRightClickBlock(Player player, InteractionHand hand, BlockHitResult hitResult) {

    }

    public void onRightClickWithItem(Player player, InteractionHand hand) {

    }

    public void onRightClickEntity(Player player, InteractionHand hand, Entity target) {

    }

    public void onLeftClickEmpty(Player player) {

    }

    public void onLeftClickBlock(Player player, BlockPos pos, Direction direction) {

    }

    public void onLeftClickEntity(Player player, Entity target) {

    }

    public void onTakeDamage(Player player, DamageSource source, float damage) {

    }

    public void onJump(Player player) {

    }

    public void onRightMouseDown(Player player) {

    }

    public void onLeftMouseDown(Player player) {

    }

    public void onRightMouseUp(Player player) {

    }

    public void onLeftMouseUp(Player player) {

    }

    public void onSneakDown(Player player) {

    }

    public void onSneakUp(Player player) {

    }

    public boolean canUse(Player player) {
        return true;
    }

    public PlayerData getProperties() {
        return data;
    }

    public List<LivingEntity> getEntityLivingBaseNearby(LivingEntity player, double distanceX, double distanceY, double distanceZ, double radius) {
        return getEntitiesNearby(player, LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(LivingEntity player, Class<T> entityClass, double r) {
        return player.level().getEntitiesOfClass(entityClass, player.getBoundingBox().inflate(r, r, r), e -> e != player && player.distanceTo(e) <= r);
    }

    public <T extends Entity> List<T> getEntitiesNearby(LivingEntity player, Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return player.level().getEntitiesOfClass(entityClass, player.getBoundingBox().inflate(dX, dY, dZ), e -> e != player && player.distanceTo(e) <= r);
    }
}
