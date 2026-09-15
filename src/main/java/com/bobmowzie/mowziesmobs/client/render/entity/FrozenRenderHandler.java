package com.bobmowzie.mowziesmobs.client.render.entity;

import com.bobmowzie.mowziesmobs.server.capability.FrozenData;
import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.capability.DataHandler;
import com.geckolib.animatable.GeoEntity;
import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.base.GeoRenderer;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.renderer.layer.GeoRenderLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class FrozenRenderHandler {
    private static final Identifier FROZEN_TEXTURE = Identifier.fromNamespaceAndPath(MMCommon.MODID, "textures/entity/frozen.png");

    public static final RenderStateDataKey<Boolean> FROZEN_RENDER_DATA_KEY = RenderStateDataKey.create(() -> "mowziesmobs:frozen");

    public static class LayerFrozen<S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {
        public LayerFrozen(RenderLayerParent<S, M> renderer) {
            super(renderer);
        }

        @Override
        public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
            if (Boolean.TRUE.equals(state.getData(FROZEN_RENDER_DATA_KEY)) && !state.isInvisible) {
                int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, 0.0F);

                submitNodeCollector.submitModel(getParentModel(), state, poseStack, RenderTypes.entityTranslucent(FROZEN_TEXTURE), lightCoords, overlayCoords, -1, null, state.outlineColor, null);
            }
        }
    }

    public static class GeckoLayerFrozen<T extends LivingEntity & GeoEntity, R extends LivingEntityRenderState> extends GeoRenderLayer<T, Void, R> {
        private static final DataTicket<Boolean> FROZEN = DataTicket.create("mowziesmobs_frozen", Boolean.class);

        public GeckoLayerFrozen(GeoRenderer<T, Void, R> entityRendererIn, EntityRendererProvider.Context context) {
            super(entityRendererIn);
        }

        @Override
        public void addRenderData(T animatable, @Nullable Void relatedObject, R renderState, float partialTick) {
            renderState.addGeckolibData(FROZEN, DataHandler.getData(animatable, DataHandler.FROZEN_DATA).getFrozen());
        }

        @Override
        public void submitRenderTask(RenderPassInfo<R> renderPassInfo, SubmitNodeCollector renderTasks) {
            if (Boolean.TRUE.equals(renderPassInfo.getGeckolibData(FROZEN))) {
                RenderType frozenRenderType = RenderTypes.entityTranslucent(FROZEN_TEXTURE);
                int packedLight = renderPassInfo.packedLight();
                int packedOverlay = renderPassInfo.packedOverlay();

                renderTasks.submitCustomGeometry(renderPassInfo.poseStack(), frozenRenderType, (pose, vertexConsumer) -> {
                    PoseStack poseStack = renderPassInfo.poseStack();

                    poseStack.pushPose();
                    poseStack.last().set(pose);
                    renderPassInfo.renderPosed(() -> renderPassInfo.model().render(renderPassInfo, vertexConsumer, packedLight, packedOverlay, -1));
                    poseStack.popPose();
                });
            }
        }
    }

    /** Pins a frozen entity to the rotation and animation it had when it froze, right before its render state is extracted. */
    public static void lockFrozenPose(LivingEntity entity) {
        FrozenData data = DataHandler.getData(entity, DataHandler.FROZEN_DATA);
        if (data.getFrozen() && data.getPrevFrozen()) {
            entity.setYRot(entity.yRotO = data.getFrozenYaw());
            entity.setXRot(entity.xRotO = data.getFrozenPitch());
            entity.yHeadRot = entity.yHeadRotO = data.getFrozenYawHead();
            entity.yBodyRot = entity.yBodyRotO = data.getFrozenRenderYawOffset();
            entity.attackAnim = entity.oAttackAnim = data.getFrozenSwingProgress();
            entity.walkAnimation.setSpeed(0);
            entity.walkAnimation.stop();
            entity.setShiftKeyDown(false);
        }
    }

    /** Returns true if vanilla rendering of this hand must be skipped. */
    public static boolean onRenderHand(InteractionHand hand, ItemStack stack, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, float equipProgress, float swingProgress) {
        Player player = Minecraft.getInstance().player;
        boolean cancel = false;

        if (player != null) {
            if (DataHandler.getData(player, DataHandler.FROZEN_DATA).getFrozen()) {
                boolean isMainHand = hand == InteractionHand.MAIN_HAND;
                if (isMainHand && !player.isInvisible() && stack.isEmpty()) {
                    HumanoidArm enumhandside = isMainHand ? player.getMainArm() : player.getMainArm().getOpposite();
                    poseStack.pushPose();
                    renderArmFirstPersonFrozen(poseStack, submitNodeCollector, packedLight, equipProgress, swingProgress, enumhandside);
                    poseStack.popPose();
                    cancel = true;
                }
            }
        }

        return cancel;
    }

    /**
     * From ItemRenderer#renderArmFirstPerson
     * PORTING NOTE: uses AvatarRenderer#renderRightHand/renderLeftHand (renamed from PlayerRenderer). This method
     * used to draw a real (translucent frozen-textured) copy of the arm ModelPart via immediate MultiBufferSource
     * drawing; that model-part-level path (renderRightArm/renderLeftArm/renderItem/setModelVisibilities below) is
     * UNVERIFIED - see class javadoc. If it doesn't compile/render correctly at runtime, that's the first place to
     * look, pending ModelPlayerAnimated/PlayerModel being finished on the client/model side.
     */
    private static void renderArmFirstPersonFrozen(PoseStack matrixStackIn, SubmitNodeCollector submitNodeCollector, int combinedLightIn, float equippedProgress, float swingProgress, HumanoidArm side) {
        Minecraft mc = Minecraft.getInstance();
        EntityRenderDispatcher renderManager = mc.getEntityRenderDispatcher();
        boolean flag = side != HumanoidArm.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = Mth.sqrt(swingProgress);
        float f2 = -0.3F * Mth.sin(f1 * (float)Math.PI);
        float f3 = 0.4F * Mth.sin(f1 * ((float)Math.PI * 2F));
        float f4 = -0.4F * Mth.sin(swingProgress * (float)Math.PI);
        matrixStackIn.translate(f * (f2 + 0.64000005F), f3 + -0.6F + equippedProgress * -0.6F, f4 + -0.71999997F);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(f * 45.0F));
        float f5 = Mth.sin(swingProgress * swingProgress * (float)Math.PI);
        float f6 = Mth.sin(f1 * (float)Math.PI);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(f * f6 * 70.0F));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(f * f5 * -20.0F));
        matrixStackIn.translate(f * -1.0F, 3.6F, 3.5D);
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(f * 120.0F));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(200.0F));
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(f * -135.0F));
        matrixStackIn.translate(f * 5.6F, 0.0D, 0.0D);
        // FIXME 26.1.2 port :: AvatarRenderer#renderRightHand/renderLeftHand now take (PoseStack, SubmitNodeCollector,
        // int, AvatarRenderState) rather than a live AbstractClientPlayer, and AvatarRenderer no longer exposes a raw
        // "getModel()" ModelPart tree the way the old per-arm ModelPart substitution below assumed - this whole
        // frozen-arm-overlay body is UNVERIFIED pending client/model finishing PlayerModel/ModelPlayerAnimated.
        // Left as-is (will not compile against the exact new AvatarRenderer signature) and clearly flagged rather
        // than guessing a wrong signature silently.
    }
}
