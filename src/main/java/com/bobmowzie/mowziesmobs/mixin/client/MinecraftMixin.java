package com.bobmowzie.mowziesmobs.mixin.client;

import com.bobmowzie.mowziesmobs.client.ClientEventHandler;
import com.bobmowzie.mowziesmobs.server.ServerEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.jetbrains.annotations.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow @Final public Options options;
    @Shadow public @Nullable LocalPlayer player;
    @Shadow public @Nullable HitResult hitResult;
    @Shadow public @Nullable MultiPlayerGameMode gameMode;

    @Inject(method = "startAttack", at = @At("HEAD"))
    private void mowziesmobs$onStartAttack(CallbackInfoReturnable<Boolean> cir) {
        ClientEventHandler.onInteractionKeyMappingTriggered(this.options.keyAttack);
    }

    // Left click that hit nothing (the MISS branch of startAttack)
    @Inject(method = "startAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;resetAttackStrengthTicker()V"))
    private void mowziesmobs$onLeftClickEmpty(CallbackInfoReturnable<Boolean> cir) {
        if (this.player != null) {
            ServerEventHandler.onPlayerLeftClickEmpty(this.player);
        }
    }

    @Inject(method = "startUseItem", at = @At("HEAD"))
    private void mowziesmobs$onStartUseItem(CallbackInfo ci) {
        ClientEventHandler.onInteractionKeyMappingTriggered(this.options.keyUse);
    }

    // Reached only when no hand successfully used anything: right click with an empty hand into the air
    @Inject(method = "startUseItem", at = @At("TAIL"))
    private void mowziesmobs$onRightClickEmpty(CallbackInfo ci) {
        if (this.player == null || this.gameMode == null || this.gameMode.isDestroying() || this.player.isHandsBusy()) return;
        if (this.hitResult != null && this.hitResult.getType() != HitResult.Type.MISS) return;
        for (InteractionHand hand : InteractionHand.values()) {
            if (this.player.getItemInHand(hand).isEmpty()) {
                ServerEventHandler.onPlayerRightClickEmpty(this.player, hand);
            }
        }
    }
}
