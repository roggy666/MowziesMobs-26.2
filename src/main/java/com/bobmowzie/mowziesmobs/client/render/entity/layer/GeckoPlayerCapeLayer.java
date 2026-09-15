package com.bobmowzie.mowziesmobs.client.render.entity.layer;

import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import com.geckolib.util.RenderUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.entity.ClientAvatarState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.equipment.Equippable;
import org.joml.Quaternionf;

import java.util.List;

/**
 * Renders the player's cape and elytra on the {@code Body} bone of the GeckoLib player rig while an ability
 * animation is playing (vanilla's CapeLayer/WingsLayer only follow the vanilla PlayerModel). Uses the same
 * bone-attachment technique as {@link GeckoPlayerArmorLayer}.
 */
public class GeckoPlayerCapeLayer {
    private final ModelPart cape;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final EquipmentAssetManager equipmentAssets;

    public GeckoPlayerCapeLayer(EntityModelSet modelSet, EquipmentAssetManager equipmentAssets) {
        this.cape = modelSet.bakeLayer(ModelLayers.PLAYER_CAPE).getChild("body").getChild("cape");
        ModelPart elytra = modelSet.bakeLayer(ModelLayers.ELYTRA);
        this.leftWing = elytra.getChild("left_wing");
        this.rightWing = elytra.getChild("right_wing");
        this.equipmentAssets = equipmentAssets;
    }

    public void registerListeners(RenderPassInfo<GeoRenderState> renderPassInfo, SubmitNodeCollector renderTasks, AbstractClientPlayer player, float partialTick) {
        if (player.isInvisible()) return;
        ItemStack chest = player.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.CHEST);
        List<EquipmentClientInfo.Layer> wingLayers = getLayers(chest, EquipmentClientInfo.LayerType.WINGS);
        boolean hasChestplate = !getLayers(chest, EquipmentClientInfo.LayerType.HUMANOID).isEmpty();
        PlayerSkin skin = player.getSkin();

        renderPassInfo.model().getBone("Body").ifPresent(bone -> renderPassInfo.renderPosed(() -> {
            PoseStack poseStack = renderPassInfo.poseStack();
            poseStack.pushPose();
            RenderUtil.transformToBone(poseStack, bone);
            bone.translateAwayFromPivotPoint(poseStack);
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            // Same pivot correction as the chest armor piece, see GeckoPlayerArmorLayer#renderBone
            poseStack.translate(-bone.pivotX() / 16.0F, (-bone.pivotY() - GeckoPlayerArmorLayer.BODY_PIVOT_LIFT_SIXTEENTHS) / 16.0F, bone.pivotZ() / 16.0F);
            int lightCoords = renderPassInfo.packedLight();

            if (!wingLayers.isEmpty()) {
                renderElytra(renderTasks, poseStack, player, chest, wingLayers, skin, lightCoords);
            } else if (skin.cape() != null && player.isModelPartShown(PlayerModelPart.CAPE)) {
                renderCape(renderTasks, poseStack, player, skin, hasChestplate, lightCoords, partialTick);
            }

            poseStack.popPose();
        }));
    }

    private void renderCape(SubmitNodeCollector renderTasks, PoseStack poseStack, AbstractClientPlayer player, PlayerSkin skin, boolean hasChestplate, int lightCoords, float partialTick) {
        // Cape sway, mirrors AvatarRenderer#extractCapeState / PlayerCapeModel#setupAnim
        ClientAvatarState clientState = player.avatarState();
        double deltaX = clientState.getInterpolatedCloakX(partialTick) - Mth.lerp(partialTick, player.xo, player.getX());
        double deltaY = clientState.getInterpolatedCloakY(partialTick) - Mth.lerp(partialTick, player.yo, player.getY());
        double deltaZ = clientState.getInterpolatedCloakZ(partialTick) - Mth.lerp(partialTick, player.zo, player.getZ());
        float yBodyRot = Mth.rotLerp(partialTick, player.yBodyRotO, player.yBodyRot);
        double forwardX = Mth.sin(yBodyRot * (float) (Math.PI / 180.0));
        double forwardZ = -Mth.cos(yBodyRot * (float) (Math.PI / 180.0));
        float capeFlap = Mth.clamp((float) deltaY * 10.0F, -6.0F, 32.0F);
        float capeLean = Mth.clamp((float) (deltaX * forwardX + deltaZ * forwardZ) * 100.0F, 0.0F, 150.0F);
        float capeLean2 = Mth.clamp((float) (deltaX * forwardZ - deltaZ * forwardX) * 100.0F, -20.0F, 20.0F);
        capeFlap += Mth.sin(clientState.getInterpolatedWalkDistance(partialTick) * 6.0F) * 32.0F * clientState.getInterpolatedBob(partialTick);

        cape.resetPose();
        cape.rotateBy(new Quaternionf()
                .rotateY((float) -Math.PI)
                .rotateX((6.0F + capeLean / 2.0F + capeFlap) * (float) (Math.PI / 180.0))
                .rotateZ(capeLean2 / 2.0F * (float) (Math.PI / 180.0))
                .rotateY((180.0F - capeLean2 / 2.0F) * (float) (Math.PI / 180.0)));

        poseStack.pushPose();
        if (hasChestplate) {
            poseStack.translate(0.0F, -0.053125F, 0.06875F);
        }
        submitPart(renderTasks, poseStack, cape, RenderTypes.entitySolid(skin.cape().texturePath()), lightCoords, -1);
        poseStack.popPose();
    }

    private void renderElytra(SubmitNodeCollector renderTasks, PoseStack poseStack, AbstractClientPlayer player, ItemStack chest, List<EquipmentClientInfo.Layer> layers, PlayerSkin skin, int lightCoords) {
        float rotX = player.elytraAnimationState.getRotX(1.0F);
        float rotY = player.elytraAnimationState.getRotY(1.0F);
        float rotZ = player.elytraAnimationState.getRotZ(1.0F);
        leftWing.y = rightWing.y = player.isCrouching() ? 3.0F : 0.0F;
        leftWing.xRot = rightWing.xRot = rotX;
        leftWing.zRot = rotZ;
        rightWing.zRot = -rotZ;
        leftWing.yRot = rotY;
        rightWing.yRot = -rotY;

        Identifier playerTexture = skin.elytra() != null ? skin.elytra().texturePath()
                : (skin.cape() != null && player.isModelPartShown(PlayerModelPart.CAPE) ? skin.cape().texturePath() : null);
        int dyeColor = DyedItemColor.getOrDefault(chest, 0);

        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 0.125F);
        for (EquipmentClientInfo.Layer layer : layers) {
            int color = layer.dyeable().map(dyeable -> dyeColor != 0 ? dyeColor : dyeable.colorWhenUndyed().map(c -> 0xFF000000 | c).orElse(0)).orElse(-1);
            if (color == 0) continue;
            Identifier texture = layer.usePlayerTexture() && playerTexture != null ? playerTexture : layer.getTextureLocation(EquipmentClientInfo.LayerType.WINGS);
            RenderType renderType = RenderTypes.entityCutout(texture);
            submitPart(renderTasks, poseStack, leftWing, renderType, lightCoords, color);
            submitPart(renderTasks, poseStack, rightWing, renderType, lightCoords, color);
        }
        poseStack.popPose();
    }

    private static void submitPart(SubmitNodeCollector renderTasks, PoseStack poseStack, ModelPart part, RenderType renderType, int lightCoords, int color) {
        renderTasks.submitCustomGeometry(poseStack, renderType, (pose, buffer) -> {
            PoseStack tempStack = new PoseStack();
            tempStack.last().set(pose);
            part.render(tempStack, buffer, lightCoords, OverlayTexture.NO_OVERLAY, color);
        });
    }

    private List<EquipmentClientInfo.Layer> getLayers(ItemStack stack, EquipmentClientInfo.LayerType layerType) {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable == null || equippable.assetId().isEmpty()) return List.of();
        return equipmentAssets.get(equippable.assetId().get()).getLayers(layerType);
    }
}
