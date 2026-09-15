package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelFoliaathBaby;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class RenderFoliaathBaby extends MowzieLLibraryRenderer<EntityBabyFoliaath, RenderFoliaathBaby.FoliaathBabyRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MMCommon.MODID, "textures/entity/foliaath_baby.png");

    private final ModelFoliaathBaby<EntityBabyFoliaath> model = new ModelFoliaathBaby<>();

    public RenderFoliaathBaby(EntityRendererProvider.Context mgr) {
        super(mgr);
    }

    @Override
    public FoliaathBabyRenderState createRenderState() {
        return new FoliaathBabyRenderState();
    }

    @Override
    public void extractRenderState(EntityBabyFoliaath entity, FoliaathBabyRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

    }

    @Override
    public void submit(FoliaathBabyRenderState state, PoseStack poseStack, SubmitNodeCollector renderTasks, CameraRenderState cameraState) {
        poseStack.pushPose();
        setupRotations(poseStack, state);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);

        renderTasks.submitCustomGeometry(poseStack, model.renderType(TEXTURE), (pose, vertexConsumer) -> {
            poseStack.pushPose();
            poseStack.last().set(pose);
            model.setupAnim(state.entity, state.limbSwing, state.limbSwingAmount, state.ageInTicks, state.headYaw, state.headPitch);
            model.renderToBuffer(poseStack, vertexConsumer, state.lightCoords, state.overlay, -1);
            poseStack.popPose();
        });

        poseStack.popPose();

        super.submit(state, poseStack, renderTasks, cameraState);
    }

    public static class FoliaathBabyRenderState extends MowzieLLibraryRenderer.State<EntityBabyFoliaath> {
    }
}
