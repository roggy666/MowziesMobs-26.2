package net.neoforged.neoforge.event.entity.player;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;

public class CriticalHitEvent extends Event {
    private final Player entity;
    private final Entity target;
    private float damageMultiplier;
    private final float oldDamageMultiplier;
    private boolean isCriticalHit;

    public CriticalHitEvent(Player entity, Entity target, float damageMultiplier, boolean isCriticalHit) {
        this.entity = entity;
        this.target = target;
        this.damageMultiplier = damageMultiplier;
        this.oldDamageMultiplier = damageMultiplier;
        this.isCriticalHit = isCriticalHit;
    }

    public Player getEntity() {
        return entity;
    }

    public Entity getTarget() {
        return target;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    public float getOldDamageMultiplier() {
        return oldDamageMultiplier;
    }

    public boolean isCriticalHit() {
        return isCriticalHit;
    }

    public void setCriticalHit(boolean criticalHit) {
        isCriticalHit = criticalHit;
    }
}
