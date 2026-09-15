package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.server.entity.MowzieLLibraryEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;

/**
 * Base renderer for the LLibrary-model mobs. Extracts the same per-frame animation inputs vanilla's
 * {@code LivingEntityRenderer} feeds its models (body/head rotation, walk animation, hurt overlay, scale) since
 * LLibrary models are not {@code EntityModel}s and cannot use that renderer directly.
 */
public abstract class MowzieLLibraryRenderer<T extends MowzieLLibraryEntity, S extends MowzieLLibraryRenderer.State<T>> extends EntityRenderer<T, S> {
    protected MowzieLLibraryRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void extractRenderState(T entity, S state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.entity = entity;
        state.partialTick = partialTicks;
        float headRot = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);
        state.yRot = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        state.headYaw = Mth.wrapDegrees(headRot - state.yRot);
        state.headPitch = entity.getXRot(partialTicks);
        if (!entity.isPassenger() && entity.isAlive()) {
            state.limbSwing = entity.walkAnimation.position(partialTicks);
            state.limbSwingAmount = Math.min(entity.walkAnimation.speed(partialTicks), 1.0F);
        } else {
            state.limbSwing = 0.0F;
            state.limbSwingAmount = 0.0F;
        }
        state.scale = entity.getScale();
        state.overlay = OverlayTexture.pack(OverlayTexture.NO_WHITE_U, OverlayTexture.v(entity.hurtTime > 0 || entity.deathTime > 0));
    }

    /** Applies the body rotation and entity scale, mirroring {@code LivingEntityRenderer#setupRotations}. */
    protected void setupRotations(PoseStack poseStack, S state) {
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - state.yRot));
        if (state.scale != 1.0F) {
            poseStack.scale(state.scale, state.scale, state.scale);
        }
    }

    public static class State<T extends MowzieLLibraryEntity> extends EntityRenderState {
        public T entity;
        public float partialTick;
        /** Interpolated body rotation. */
        public float yRot;
        public float headYaw;
        public float headPitch;
        public float limbSwing;
        public float limbSwingAmount;
        public float scale = 1.0F;
        public int overlay = OverlayTexture.NO_OVERLAY;
    }
}
