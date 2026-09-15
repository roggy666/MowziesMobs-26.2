package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.gui.GuiSculptorTrade;
import com.bobmowzie.mowziesmobs.client.gui.GuiUmvuthanaTrade;
import com.bobmowzie.mowziesmobs.client.gui.GuiUmvuthiTrade;
import com.bobmowzie.mowziesmobs.client.render.block.GongRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.*;
import com.bobmowzie.mowziesmobs.server.block.entity.BlockEntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.inventory.ContainerHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemWroughtHelm;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;

public class ClientEventBusSubscriber {

    public static void registerRenderers() {
        EntityRenderers.register(EntityHandler.BABY_FOLIAATH, RenderFoliaathBaby::new);
        EntityRenderers.register(EntityHandler.FOLIAATH, RenderFoliaath::new);
        EntityRenderers.register(EntityHandler.WROUGHTNAUT, RenderWroughtnaut::new);
        EntityRenderers.register(EntityHandler.UMVUTHI, RenderUmvuthi::new);
        EntityRenderers.register(EntityHandler.UMVUTHANA_RAPTOR, RenderUmvuthana::new);
        EntityRenderers.register(EntityHandler.UMVUTHANA_FOLLOWER_TO_RAPTOR, RenderUmvuthana::new);
        EntityRenderers.register(EntityHandler.UMVUTHANA_MINION, RenderUmvuthana::new);
        EntityRenderers.register(EntityHandler.UMVUTHANA_FOLLOWER_TO_PLAYER, RenderUmvuthana::new);
        EntityRenderers.register(EntityHandler.UMVUTHANA_CRANE_TO_PLAYER, RenderUmvuthana::new);
        EntityRenderers.register(EntityHandler.UMVUTHANA_CRANE, RenderUmvuthana::new);
        EntityRenderers.register(EntityHandler.FROSTMAW, RenderFrostmaw::new);
        EntityRenderers.register(EntityHandler.GROTTOL, RenderGrottol::new);
        EntityRenderers.register(EntityHandler.LANTERN, RenderLantern::new);
        EntityRenderers.register(EntityHandler.NAGA, RenderNaga::new);
        EntityRenderers.register(EntityHandler.SCULPTOR, RenderSculptor::new);
        EntityRenderers.register(EntityHandler.BLUFF, RenderBluff::new);
        EntityRenderers.register(EntityHandler.ELOKOSA_FOLLOWER_TO_HOWLER, RenderElokosa::new);
        EntityRenderers.register(EntityHandler.ELOKOSA_HOWLER, RenderElokosa::new);

        EntityRenderers.register(EntityHandler.DART, RenderDart::new);
        EntityRenderers.register(EntityHandler.SUNSTRIKE, RenderSunstrike::new);
        EntityRenderers.register(EntityHandler.SOLAR_BEAM, RenderSolarBeam::new);
        EntityRenderers.register(EntityHandler.BOULDER_PROJECTILE, RenderBoulder::new);
        EntityRenderers.register(EntityHandler.BOULDER_SCULPTOR, RenderBoulder::new);
        EntityRenderers.register(EntityHandler.BOULDER_SCULPTOR_CRUMBLING, RenderBoulder::new);
        EntityRenderers.register(EntityHandler.PILLAR, RenderPillar::new);
        EntityRenderers.register(EntityHandler.PILLAR_SCULPTOR, RenderPillar::new);
        EntityRenderers.register(EntityHandler.PILLAR_PIECE, RenderNothing::new);
        EntityRenderers.register(EntityHandler.AXE_ATTACK, RenderAxeAttack::new);
        EntityRenderers.register(EntityHandler.POISON_BALL, RenderPoisonBall::new);
        EntityRenderers.register(EntityHandler.ICE_BALL, RenderIceBall::new);
        EntityRenderers.register(EntityHandler.ICE_BREATH, RenderNothing::new);
        EntityRenderers.register(EntityHandler.FROZEN_CONTROLLER, RenderNothing::new);
        EntityRenderers.register(EntityHandler.SUPER_NOVA, RenderSuperNova::new);
        EntityRenderers.register(EntityHandler.FALLING_BLOCK, RenderFallingBlock::new);
        EntityRenderers.register(EntityHandler.BLOCK_SWAPPER, RenderNothing::new);
        EntityRenderers.register(EntityHandler.BLOCK_SWAPPER_TUNNELING, RenderNothing::new);
        EntityRenderers.register(EntityHandler.CAMERA_SHAKE, RenderNothing::new);
        EntityRenderers.register(EntityHandler.ROCK_SLING, RenderRockSling::new);
        EntityRenderers.register(EntityHandler.FISSURE, RenderNothing::new);
        EntityRenderers.register(EntityHandler.FISSURE_PIECE, RenderFissurePiece::new);
        EntityRenderers.register(EntityHandler.EARTH_SPIKE, RenderEarthSpike::new);

        BlockEntityRenderers.register(BlockEntityHandler.GONG_BLOCK_ENTITY, GongRenderer::new);

        MenuScreens.register(ContainerHandler.UMVUTHANA_TRADE, GuiUmvuthanaTrade::new);
        MenuScreens.register(ContainerHandler.UMVUTHI_TRADE, GuiUmvuthiTrade::new);
        MenuScreens.register(ContainerHandler.SCULPTOR_TRADE, GuiSculptorTrade::new);

        ArmorRenderer.register((poseStack, submitNodeCollector, stack, renderState, slot, light, contextModel) -> {
            if (slot != EquipmentSlot.HEAD) return;
            Model model = ItemWroughtHelm.ArmorRender.getArmorModel();
            if (model == null) return;
            Identifier texture = Identifier.fromNamespaceAndPath(MMCommon.MODID, "textures/item/wrought_helmet.png");
            submitNodeCollector.order(1).submitModel(model, renderState, poseStack, RenderTypes.armorCutoutNoCull(texture),
                    light, OverlayTexture.NO_OVERLAY, -1, null, renderState.outlineColor, null);
            if (stack.hasFoil()) {
                submitNodeCollector.order(2).submitModel(model, renderState, poseStack, RenderTypes.armorEntityGlint(),
                        light, OverlayTexture.NO_OVERLAY, -1, null, renderState.outlineColor, null);
            }
        }, ItemHandler.WROUGHT_HELMET);
    }
}