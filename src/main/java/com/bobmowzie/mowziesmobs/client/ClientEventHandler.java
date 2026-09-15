package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.gui.CustomBossBar;
import com.bobmowzie.mowziesmobs.client.render.block.SculptorBlockHighlightRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoFirstPersonRenderer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoRenderPlayer;
import com.bobmowzie.mowziesmobs.client.sound.BossMusicPlayer;
import com.bobmowzie.mowziesmobs.server.ServerEventHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilityClientEventHandler;
import com.bobmowzie.mowziesmobs.server.capability.AbilityData;
import com.bobmowzie.mowziesmobs.server.capability.DataHandler;
import com.bobmowzie.mowziesmobs.server.capability.FrozenData;
import com.bobmowzie.mowziesmobs.server.capability.PlayerData;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.effects.EntityCameraShake;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrozenController;
import com.bobmowzie.mowziesmobs.server.item.ItemBlowgun;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.GeoRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Client-side hooks. Fabric API callbacks are registered in {@link #register()}; hooks that have no Fabric API
 * equivalent are invoked from the mixins in {@code com.bobmowzie.mowziesmobs.mixin.client}.
 */
public class ClientEventHandler {
    private static final Identifier FROZEN_BLUR = Identifier.withDefaultNamespace("textures/misc/powder_snow_outline.png");
    private static final Identifier FROZEN_OVERLAY_ELEMENT = MMCommon.resource("frozen_overlay");
    private static boolean renderingCustomHands;

    public static void register() {
        ClientEntityEvents.ENTITY_LOAD.register((entity, level) -> ServerEventHandler.onJoinWorld(entity));
        ClientTickEvents.END_LEVEL_TICK.register(ClientEventHandler::onLevelTick);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            LocalPlayer player = client.player;
            if (player != null && player.isPassenger() && player.getVehicle() instanceof EntityFrozenController) {
                client.gui.hud.setOverlayMessage(Component.empty(), false);
            }
        });
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS, FROZEN_OVERLAY_ELEMENT, (graphics, deltaTracker) -> renderFrozenOverlay(graphics));
        SculptorBlockHighlightRenderer.register();
    }

    /**
     * Called from {@code ItemInHandRendererMixin} for each first person hand. Returns true if vanilla hand rendering
     * must be skipped.
     */
    public static boolean onHandRender(InteractionHand hand, ItemStack stack, float partialTick, float interpolatedPitch, float swingProgress, float equipProgress, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight) {
        // GeckoFirstPersonRenderer renders the held item through ItemInHandRenderer#submitArmWithItem itself
        if (renderingCustomHands) return false;
        if (!ConfigHandler.CLIENT.customPlayerAnims.get()) return false;

        Player player = Minecraft.getInstance().player;
        if (player == null) return false;

        AbilityData abilityData = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        if (abilityData == null || abilityData.getActiveAbility() == null) return false;

        GeckoPlayer.GeckoPlayerFirstPerson geckoPlayerFirstPerson = GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON;
        if (geckoPlayerFirstPerson == null) return false;
        GeoRenderer<GeckoPlayer, Void, GeoRenderState> renderer = geckoPlayerFirstPerson.getPlayerRenderer();
        if (!(renderer instanceof GeckoFirstPersonRenderer firstPersonRenderer)) return false;

        // renderHands covers both hands in one call, so it is only invoked once per frame
        if (hand == InteractionHand.MAIN_HAND) {
            CameraRenderState cameraState = Minecraft.getInstance().gameRenderer.gameRenderState().levelRenderState.cameraRenderState;
            renderingCustomHands = true;
            try {
                firstPersonRenderer.renderHands((AbstractClientPlayer) player, geckoPlayerFirstPerson, interpolatedPitch, partialTick, poseStack, submitNodeCollector, cameraState, packedLight);
            } finally {
                renderingCustomHands = false;
            }
        }
        return true;
    }

    /**
     * Called from {@code LivingEntityRendererMixin} before a player is rendered. Returns true if vanilla player
     * rendering must be skipped.
     */
    public static boolean onRenderPlayer(AvatarRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        if (!ConfigHandler.CLIENT.customPlayerAnims.get()) return false;

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return false;

        if (!(level.getEntity(renderState.id) instanceof Player player)) return false;

        if (ConfigHandler.CLIENT.hidePlayerAnimsInFirstPerson.get()
                && player == Minecraft.getInstance().player
                && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            return false;
        }

        AbilityData abilityData = DataHandler.getData(player, DataHandler.ABILITY_DATA);
        if (abilityData == null || abilityData.getActiveAbility() == null) return false;

        GeckoPlayer geckoPlayer = DataHandler.getData(player, DataHandler.PLAYER_DATA).getGeckoPlayer();
        if (geckoPlayer == null) return false;
        GeoRenderer<GeckoPlayer, Void, GeoRenderState> renderer = geckoPlayer.getPlayerRenderer();
        if (!(renderer instanceof GeckoRenderPlayer geckoRenderPlayer)) return false;

        CameraRenderState cameraState = Minecraft.getInstance().gameRenderer.gameRenderState().levelRenderState.cameraRenderState;
        float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
        geckoRenderPlayer.render((AbstractClientPlayer) player, geckoPlayer, poseStack, submitNodeCollector, cameraState, renderState.lightCoords, partialTick);
        return true;
    }

    /** Called at the end of every player tick on the client. */
    public static void onPlayerTick(Player player) {
        GeckoPlayer geckoPlayer = DataHandler.getData(player, DataHandler.PLAYER_DATA).getGeckoPlayer();
        if (geckoPlayer != null) geckoPlayer.tick();
        if (player == Minecraft.getInstance().player && GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON != null) GeckoFirstPersonRenderer.GECKO_PLAYER_FIRST_PERSON.tick();
    }

    /** Called from {@code GameRendererMixin} before a frame is rendered. */
    public static void onRenderFrameStart() {
        Player player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        FrozenData data = DataHandler.getData(player, DataHandler.FROZEN_DATA);
        if (data.getFrozen() && data.getPrevFrozen()) {
            player.setYRot(data.getFrozenYaw());
            player.setXRot(data.getFrozenPitch());
            player.yHeadRot = data.getFrozenYawHead();
            player.yRotO = player.getYRot();
            player.xRotO = player.getXRot();
            player.yHeadRotO = player.yHeadRot;
        }
    }

    /** Called from {@code GameRendererMixin} after a frame has been rendered. */
    public static void onRenderFrameEnd(float partialTick) {
        AbilityClientEventHandler.onRenderTick(partialTick);
    }

    private static void renderFrozenOverlay(GuiGraphicsExtractor graphics) {
        if (Minecraft.getInstance().player != null) {
            FrozenData data = DataHandler.getData(Minecraft.getInstance().player, DataHandler.FROZEN_DATA);

            if (data.getFrozen() && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, FROZEN_BLUR, 0, 0, 0.0F, 0.0F, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight(), -1);
            }
        }
    }

    /** Called from {@code AbstractClientPlayerMixin}. Returns the modified FOV modifier. */
    public static float updateFOV(Player player, float fovModifier) {
        if (player.isUsingItem() && player.getUseItem().getItem() instanceof ItemBlowgun) {
            int i = player.getTicksUsingItem();
            float f1 = (float)i / 5.0F;
            if (f1 > 1.0F) {
                f1 = 1.0F;
            } else {
                f1 = f1 * f1;
            }

            return 1.0F - f1 * 0.15F;
        }
        return fovModifier;
    }

    /** Camera shake offsets, {@code [yaw, pitch]} in degrees. Called from {@code CameraMixin}. */
    public static float[] getCameraShake() {
        Player player = Minecraft.getInstance().player;
        if (player == null || !ConfigHandler.CLIENT.doCameraShakes.get() || Minecraft.getInstance().isPaused()) return null;
        float delta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
        float ticksExistedDelta = player.tickCount + delta;

        float shakeAmplitude = 0;
        for (EntityCameraShake cameraShake : player.level().getEntitiesOfClass(EntityCameraShake.class, player.getBoundingBox().inflate(20, 20, 20))) {
            if (cameraShake.distanceTo(player) < cameraShake.getRadius()) {
                shakeAmplitude += cameraShake.getShakeAmount(player, delta);
            }
        }
        if (shakeAmplitude <= 0) return null;
        if (shakeAmplitude > 1.0f) shakeAmplitude = 1.0f;
        float yaw = (float) (shakeAmplitude * Math.cos(ticksExistedDelta * 5 + 1) * 25);
        float pitch = (float) (shakeAmplitude * Math.cos(ticksExistedDelta * 3 + 2) * 25);
        return new float[]{yaw, pitch};
    }

    /**
     * Called from {@code BossHealthOverlayMixin}. Returns the vertical increment for the next bar if the bar was
     * rendered by a custom boss bar, or -1 if vanilla rendering should proceed.
     */
    public static int onRenderBossBar(GuiGraphicsExtractor graphics, int x, int y, LerpingBossEvent bossEvent) {
        if (!ConfigHandler.CLIENT.customBossBars.get()) return -1;
        Identifier bossRegistryName = ClientProxy.bossBarRegistryNames.getOrDefault(bossEvent.getId(), null);
        if (bossRegistryName == null && bossEvent.getName().getContents() instanceof TranslatableContents translatable) {
            String key = translatable.getKey();
            if ("entity.mowziesmobs.ferrous_wroughtnaut".equals(key)) {
                bossRegistryName = BuiltInRegistries.ENTITY_TYPE.getKey(EntityHandler.WROUGHTNAUT);
            } else if ("entity.mowziesmobs.frostmaw".equals(key)) {
                bossRegistryName = BuiltInRegistries.ENTITY_TYPE.getKey(EntityHandler.FROSTMAW);
            } else if ("entity.mowziesmobs.umvuthi".equals(key)) {
                bossRegistryName = BuiltInRegistries.ENTITY_TYPE.getKey(EntityHandler.UMVUTHI);
            }
        }
        if (bossRegistryName == null) return -1;
        CustomBossBar customBossBar = CustomBossBar.customBossBars.getOrDefault(bossRegistryName, null);
        if (customBossBar == null) return -1;

        customBossBar.renderBossBar(graphics, x, y, bossEvent);
        return customBossBar.getVerticalIncrement();
    }

    private static void onLevelTick(ClientLevel level) {
        MMCommon.PROXY.updateMarkedBlocks();
        BossMusicPlayer.tick();
    }

    /** Called from {@code MinecraftMixin} whenever the attack or use key is triggered. */
    public static void onInteractionKeyMappingTriggered(KeyMapping keyMapping) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        if (keyMapping == Minecraft.getInstance().options.keyAttack) {
            PlayerData data = DataHandler.getData(player, DataHandler.PLAYER_DATA);
            if (data != null) {
                data.pressedAttackKey(player);
            }
        } else if (keyMapping == Minecraft.getInstance().options.keyUse) {
            PlayerData data = DataHandler.getData(player, DataHandler.PLAYER_DATA);
            if (data != null) {
                data.pressedUseKey(player);
            }
        }
    }
}
