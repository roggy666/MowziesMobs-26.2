package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;

public class SunblockLayer<S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {
    private static final Identifier SUNBLOCK_ARMOR = Identifier.fromNamespaceAndPath(MMCommon.MODID, "textures/entity/sunblock_glow.png");

    public static final RenderStateDataKey<Boolean> SUNBLOCK_RENDER_DATA_KEY = RenderStateDataKey.create(() -> "mowziesmobs:sunblock");

    public SunblockLayer(RenderLayerParent<S, M> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
        if (Boolean.TRUE.equals(state.getData(SUNBLOCK_RENDER_DATA_KEY))) {
            float f = state.ageInTicks;
            int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, 0.0F);
            int tintColor = ARGB.colorFromFloat(1, 1, 1, 0.1f);

            submitNodeCollector.submitModel(getParentModel(), state, poseStack, RenderTypes.energySwirl(getTextureLocation(), xOffset(f), f * 0.01F),
                    lightCoords, overlayCoords, tintColor, null, state.outlineColor, null);
        }
    }

    protected float xOffset(float p_225634_1_) {
        return p_225634_1_ * 0.02F;
    }

    protected Identifier getTextureLocation() {
        return SUNBLOCK_ARMOR;
    }
}
