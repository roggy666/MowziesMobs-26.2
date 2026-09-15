package com.bobmowzie.mowziesmobs.mixin.client;

import com.bobmowzie.mowziesmobs.client.ClientEventHandler;
import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Inject(method = "submitArmWithItem", at = @At("HEAD"), cancellable = true)
    private void mowziesmobs$onRenderHand(AbstractClientPlayer player, float partialTick, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equipProgress, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, CallbackInfo ci) {
        if (ClientEventHandler.onHandRender(hand, stack, partialTick, pitch, swingProgress, equipProgress, poseStack, submitNodeCollector, packedLight)) {
            ci.cancel();
            return;
        }
        if (FrozenRenderHandler.onRenderHand(hand, stack, poseStack, submitNodeCollector, packedLight, equipProgress, swingProgress)) {
            ci.cancel();
        }
    }
}
