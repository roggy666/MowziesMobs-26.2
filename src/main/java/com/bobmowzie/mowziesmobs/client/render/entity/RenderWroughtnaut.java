package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.model.entity.ModelWroughtnaut;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.ItemLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.WroughtnautEyesLayer;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;

public class RenderWroughtnaut extends MowzieLLibraryRenderer<EntityWroughtnaut, RenderWroughtnaut.WroughtnautRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(MMCommon.MODID, "textures/entity/wroughtnaut.png");

    private final ModelWroughtnaut<EntityWroughtnaut> model = new ModelWroughtnaut<>();
    private final WroughtnautEyesLayer<EntityWroughtnaut> eyesLayer = new WroughtnautEyesLayer<>();
    private final ItemLayer<WroughtnautRenderState, EntityWroughtnaut> swordLayer = new ItemLayer<>(
            state -> state.entity, model.sword, () -> Items.DIAMOND_SWORD.getDefaultInstance(), ItemDisplayContext.GROUND);

    public RenderWroughtnaut(EntityRendererProvider.Context mgr) {
        super(mgr);
    }

    @Override
    public WroughtnautRenderState createRenderState() {
        return new WroughtnautRenderState();
    }

    @Override
    public void extractRenderState(EntityWroughtnaut entity, WroughtnautRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

    }

    @Override
    public void submit(WroughtnautRenderState state, PoseStack poseStack, SubmitNodeCollector renderTasks, CameraRenderState cameraState) {
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

        renderTasks.submitCustomGeometry(poseStack, WroughtnautEyesLayer.renderType(TEXTURE), (pose, vertexConsumer) -> {
            poseStack.pushPose();
            poseStack.last().set(pose);
            eyesLayer.render(poseStack, vertexConsumer, state.lightCoords, state.overlay, state.entity, state.limbSwing, state.limbSwingAmount, state.partialTick, state.ageInTicks, state.headYaw, state.headPitch);
            poseStack.popPose();
        });

        swordLayer.submit(poseStack, renderTasks, state.lightCoords, state);

        poseStack.popPose();

        super.submit(state, poseStack, renderTasks, cameraState);
    }

    public static class WroughtnautRenderState extends MowzieLLibraryRenderer.State<EntityWroughtnaut> {
    }
}
