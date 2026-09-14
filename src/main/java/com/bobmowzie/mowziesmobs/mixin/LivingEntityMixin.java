package com.bobmowzie.mowziesmobs.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract ItemStack getItemInHand(InteractionHand hand);
    @Shadow public abstract DamageSource getLastDamageSource();

    @Inject(method = "tick", at = @At("TAIL"))
    private void mm$onLivingTick(CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;
        NeoForge.EVENT_BUS.post(new EntityTickEvent.Post(living));
        NeoForge.EVENT_BUS.post(new LivingEvent.LivingTickEvent(living));
        if (living instanceof Player player) {
            NeoForge.EVENT_BUS.post(new PlayerTickEvent.Post(player));
        }
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void mm$onLivingDeath(DamageSource damageSource, CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;
        NeoForge.EVENT_BUS.post(new LivingDeathEvent(living, damageSource));
    }

    @Inject(method = "jumpFromGround", at = @At("TAIL"))
    private void mm$onLivingJump(CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;
        NeoForge.EVENT_BUS.post(new LivingEvent.LivingJumpEvent(living));
    }

    @Inject(method = "causeFallDamage", at = @At("HEAD"))
    private void mm$onLivingFall(double fallDistance, float multiplier, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity living = (LivingEntity) (Object) this;
        NeoForge.EVENT_BUS.post(new LivingFallEvent(living, (float) fallDistance, multiplier));
    }

    @Inject(method = "actuallyHurt", at = @At("HEAD"), cancellable = true)
    private void mm$onActuallyHurtPre(ServerLevel serverLevel, DamageSource damageSource, float damage, CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;
        LivingDamageEvent.Pre pre = new LivingDamageEvent.Pre(living, damageSource, damage);
        NeoForge.EVENT_BUS.post(pre);
        if (pre.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = "actuallyHurt", at = @At("TAIL"))
    private void mm$onActuallyHurtPost(ServerLevel serverLevel, DamageSource damageSource, float damage, CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;
        NeoForge.EVENT_BUS.post(new LivingDamageEvent.Post(living, damageSource, damage));
    }

    @Inject(method = "animateHurt", at = @At("TAIL"))
    private void mm$onAnimateHurt(float yaw, CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;
        if (living.level().isClientSide()) {
            DamageSource lastSource = this.getLastDamageSource();
            if (lastSource != null) {
                NeoForge.EVENT_BUS.post(new LivingDamageEvent.Post(living, lastSource, 0.0f));
            }
        }
    }

    @Inject(method = "onEffectAdded", at = @At("TAIL"))
    private void mm$onEffectAdded(MobEffectInstance effect, Entity source, CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;
        NeoForge.EVENT_BUS.post(new MobEffectEvent.Added(living, effect));
    }

    @Inject(method = "onEffectsRemoved", at = @At("TAIL"))
    private void mm$onEffectsRemoved(Collection<MobEffectInstance> effects, CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;
        for (MobEffectInstance effect : effects) {
            NeoForge.EVENT_BUS.post(new MobEffectEvent.Remove(living, effect));
        }
    }

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void mm$canBeAffected(MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity living = (LivingEntity) (Object) this;
        MobEffectEvent.Applicable event = new MobEffectEvent.Applicable(living, effect);
        NeoForge.EVENT_BUS.post(event);
        if (event.getResult() == MobEffectEvent.Applicable.Result.DO_NOT_APPLY) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "startUsingItem", at = @At("HEAD"), cancellable = true)
    private void mm$onStartUsingItem(InteractionHand hand, CallbackInfo ci) {
        LivingEntity living = (LivingEntity) (Object) this;
        ItemStack stack = this.getItemInHand(hand);
        LivingEntityUseItemEvent.Start event = new LivingEntityUseItemEvent.Start(living, stack, stack.getUseDuration(living));
        NeoForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
