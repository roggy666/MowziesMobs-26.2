package com.bobmowzie.mowziesmobs.mixin.client;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemWroughtHelm;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> {
    @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
    private void mm$onRenderArmorPiece(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, ItemStack itemStack, EquipmentSlot equipmentSlot, int lightCoords, S humanoidRenderState, CallbackInfo ci) {
        if (itemStack.is(ItemHandler.WROUGHT_HELMET.get()) && equipmentSlot == EquipmentSlot.HEAD) {
            Model model = ItemWroughtHelm.ArmorRender.getArmorModel();
            if (model != null) {
                Identifier texture = Identifier.fromNamespaceAndPath(MMCommon.MODID, "textures/item/wrought_helmet.png");
                submitNodeCollector.order(1).submitModel(
                    model, humanoidRenderState, poseStack, RenderTypes.armorCutoutNoCull(texture),
                    lightCoords, OverlayTexture.NO_OVERLAY, -1, null, humanoidRenderState.outlineColor, null
                );
                if (itemStack.hasFoil()) {
                    submitNodeCollector.order(2).submitModel(
                        model, humanoidRenderState, poseStack, RenderTypes.armorEntityGlint(),
                        lightCoords, OverlayTexture.NO_OVERLAY, -1, null, humanoidRenderState.outlineColor, null
                    );
                }
                ci.cancel();
            }
        }
    }
}
