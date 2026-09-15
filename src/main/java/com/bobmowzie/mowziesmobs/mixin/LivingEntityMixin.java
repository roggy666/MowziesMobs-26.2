package com.bobmowzie.mowziesmobs.mixin;

import com.bobmowzie.mowziesmobs.server.ServerEventHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract ItemStack getItemInHand(InteractionHand hand);

    @Inject(method = "tick", at = @At("TAIL"))
    private void mowziesmobs$onLivingTick(CallbackInfo ci) {
        ServerEventHandler.onLivingTick((LivingEntity) (Object) this);
    }

    @Inject(method = "jumpFromGround", at = @At("TAIL"))
    private void mowziesmobs$onLivingJump(CallbackInfo ci) {
        ServerEventHandler.onLivingJump((LivingEntity) (Object) this);
    }

    @ModifyVariable(method = "causeFallDamage", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float mowziesmobs$onLivingFall(float damageMultiplier, double fallDistance) {
        return ServerEventHandler.onLivingFall((LivingEntity) (Object) this, fallDistance, damageMultiplier);
    }

    // Runs after armor/magic absorption have been applied, before health is modified
    @ModifyVariable(method = "actuallyHurt", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterMagicAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"), argsOnly = true)
    private float mowziesmobs$onActuallyHurt(float damage, ServerLevel level, DamageSource source) {
        return ServerEventHandler.onLivingHurtPre((LivingEntity) (Object) this, source, damage);
    }

    @Inject(method = "onEffectAdded", at = @At("TAIL"))
    private void mowziesmobs$onEffectAdded(MobEffectInstance effect, Entity source, CallbackInfo ci) {
        ServerEventHandler.onAddPotionEffect((LivingEntity) (Object) this, effect);
    }

    @Inject(method = "onEffectsRemoved", at = @At("TAIL"))
    private void mowziesmobs$onEffectsRemoved(Collection<MobEffectInstance> effects, CallbackInfo ci) {
        for (MobEffectInstance effect : effects) {
            ServerEventHandler.onRemovePotionEffect((LivingEntity) (Object) this, effect);
        }
    }

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void mowziesmobs$canBeAffected(MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if (!ServerEventHandler.onPotionEffectApplicable((LivingEntity) (Object) this, effect)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "startUsingItem", at = @At("HEAD"), cancellable = true)
    private void mowziesmobs$onStartUsingItem(InteractionHand hand, CallbackInfo ci) {
        if (ServerEventHandler.onUseItem((LivingEntity) (Object) this, this.getItemInHand(hand))) {
            ci.cancel();
        }
    }
}
