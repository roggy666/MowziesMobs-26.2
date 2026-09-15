package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelNaga;
import com.bobmowzie.mowziesmobs.client.render.MowzieRenderUtils;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RenderNaga extends MowzieLLibraryRenderer<EntityNaga, RenderNaga.NagaRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MMCommon.MODID, "textures/entity/naga.png");

    private final ModelNaga<EntityNaga> model = new ModelNaga<>();

    public RenderNaga(EntityRendererProvider.Context mgr) {
        super(mgr);
    }

    @Override
    protected AABB getBoundingBoxForCulling(EntityNaga entity) {
        return super.getBoundingBoxForCulling(entity).inflate(12.0D);
    }

    @Override
    public NagaRenderState createRenderState() {
        return new NagaRenderState();
    }

    @Override
    public void extractRenderState(EntityNaga entity, NagaRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

    }

    @Override
    public void submit(NagaRenderState state, PoseStack poseStack, SubmitNodeCollector renderTasks, CameraRenderState cameraState) {
        EntityNaga entity = state.entity;

        poseStack.pushPose();
        setupRotations(poseStack, state);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);

        renderTasks.submitCustomGeometry(poseStack, model.renderType(TEXTURE), (pose, vertexConsumer) -> {
            poseStack.pushPose();
            poseStack.last().set(pose);
            model.setupAnim(entity, state.limbSwing, state.limbSwingAmount, state.ageInTicks, state.headYaw, state.headPitch);
            model.renderToBuffer(poseStack, vertexConsumer, state.lightCoords, state.overlay, -1);
            poseStack.popPose();
        });

        poseStack.popPose();

        super.submit(state, poseStack, renderTasks, cameraState);

        if (entity.getAnimation() == EntityNaga.SPIT_ANIMATION && entity.mouthPos != null && entity.mouthPos.length > 0) {
            // NOTE: re-pose the shared model synchronously so this read reflects the current frame - see
            // RenderFrostmaw.java's javadoc for the full reasoning (same technique).
            model.setupAnim(entity, state.limbSwing, state.limbSwingAmount, state.ageInTicks, state.headYaw, state.headPitch);
            entity.mouthPos[0] = MowzieRenderUtils.getWorldPosFromModel(entity, state.yRot, model.mouthSocket);
        }
    }

    public static class NagaRenderState extends MowzieLLibraryRenderer.State<EntityNaga> {
    }
}
