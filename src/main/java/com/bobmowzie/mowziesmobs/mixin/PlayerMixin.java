package com.bobmowzie.mowziesmobs.mixin;

import com.bobmowzie.mowziesmobs.server.ServerEventHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Unique private float mowziesmobs$critMultiplier = 1.5F;

    // Third boolean local of attack(): fullStrengthAttack, knockbackAttack, criticalAttack
    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 2)
    private boolean mowziesmobs$onCriticalHit(boolean criticalAttack, Entity target) {
        ServerEventHandler.CriticalHit result = ServerEventHandler.onCriticalHit((Player) (Object) this, target, 1.5F, criticalAttack);
        this.mowziesmobs$critMultiplier = result.damageMultiplier();
        return result.critical();
    }

    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5F))
    private float mowziesmobs$critDamageMultiplier(float original) {
        return this.mowziesmobs$critMultiplier;
    }
}
