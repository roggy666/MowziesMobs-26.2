package com.bobmowzie.mowziesmobs.mixin.client;

import com.bobmowzie.mowziesmobs.client.ClientEventHandler;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.SunblockLayer;
import com.bobmowzie.mowziesmobs.server.capability.DataHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void mowziesmobs$extractRenderState(LivingEntity entity, LivingEntityRenderState state, float partialTick, CallbackInfo ci) {
        state.setData(FrozenRenderHandler.FROZEN_RENDER_DATA_KEY, DataHandler.getData(entity, DataHandler.FROZEN_DATA).getFrozen());
        state.setData(SunblockLayer.SUNBLOCK_RENDER_DATA_KEY, DataHandler.getData(entity, DataHandler.LIVING_DATA).getHasSunblock());
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("HEAD"), cancellable = true)
    private void mowziesmobs$onRenderPlayer(LivingEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        if (state instanceof AvatarRenderState avatarState && ClientEventHandler.onRenderPlayer(avatarState, poseStack, submitNodeCollector)) {
            ci.cancel();
        }
    }
}
