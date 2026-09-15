package com.bobmowzie.mowziesmobs.mixin.client;

import com.bobmowzie.mowziesmobs.client.ClientEventHandler;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void mowziesmobs$onRenderFrameStart(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        ClientEventHandler.onRenderFrameStart();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void mowziesmobs$onRenderFrameEnd(DeltaTracker deltaTracker, boolean advanceGameTime, CallbackInfo ci) {
        ClientEventHandler.onRenderFrameEnd(deltaTracker.getGameTimeDeltaPartialTick(false));
    }
}
