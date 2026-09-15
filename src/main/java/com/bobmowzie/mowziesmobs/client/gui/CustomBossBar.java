package com.bobmowzie.mowziesmobs.client.gui;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.BossEvent;

import java.util.HashMap;
import java.util.Map;

public class CustomBossBar {
    public static Map<Identifier, CustomBossBar> customBossBars = new HashMap<>();
    static {
        customBossBars.put(BuiltInRegistries.ENTITY_TYPE.getKey(EntityHandler.UMVUTHI), new CustomBossBar(
                MMCommon.resource("textures/gui/boss_bar/umvuthi_bar_base.png"),
                MMCommon.resource("textures/gui/boss_bar/umvuthi_bar_overlay.png"),
                4, 8, 5, -5, -6, 256, 16, 25, ChatFormatting.GOLD));
        customBossBars.put(BuiltInRegistries.ENTITY_TYPE.getKey(EntityHandler.FROSTMAW), new CustomBossBar(
                MMCommon.resource("textures/gui/boss_bar/frostmaw_bar_base.png"),
                MMCommon.resource("textures/gui/boss_bar/frostmaw_bar_overlay.png"),
                10, 32, 2, -4, -3, 256, 32, 25, ChatFormatting.WHITE));
        customBossBars.put(BuiltInRegistries.ENTITY_TYPE.getKey(EntityHandler.WROUGHTNAUT), new CustomBossBar(
                MMCommon.resource("textures/gui/boss_bar/wroughtnaut_bar_base.png"),
                MMCommon.resource("textures/gui/boss_bar/wroughtnaut_bar_overlay.png"),
                4, 8, 5, -5, -6, 256, 16, 25, ChatFormatting.RED));
    }

    private final Identifier baseTexture;
    private final Identifier overlayTexture;
    private final boolean hasOverlay;

    private final int baseHeight;
    private final int baseTextureHeight;
    private final int baseOffsetY;
    private final int overlayOffsetX;
    private final int overlayOffsetY;
    private final int overlayWidth;
    private final int overlayHeight;

    private final int verticalIncrement;

    private final ChatFormatting textColor;

    public CustomBossBar(Identifier baseTexture, Identifier overlayTexture, int baseHeight, int baseTextureHeight, int baseOffsetY, int overlayOffsetX, int overlayOffsetY, int overlayWidth, int overlayHeight, int verticalIncrement, ChatFormatting textColor) {
        this.baseTexture = baseTexture;
        this.overlayTexture = overlayTexture;
        this.hasOverlay = overlayTexture != null;
        this.baseHeight = baseHeight;
        this.baseTextureHeight = baseTextureHeight;
        this.baseOffsetY = baseOffsetY;
        this.overlayOffsetX = overlayOffsetX;
        this.overlayOffsetY = overlayOffsetY;
        this.overlayWidth = overlayWidth;
        this.overlayHeight = overlayHeight;
        this.verticalIncrement = verticalIncrement;
        this.textColor = textColor;
    }

    public Identifier getBaseTexture() {
        return baseTexture;
    }

    public Identifier getOverlayTexture() {
        return overlayTexture;
    }

    public boolean hasOverlay() {
        return hasOverlay;
    }

    public int getBaseHeight() {
        return baseHeight;
    }

    public int getBaseTextureHeight() {
        return baseTextureHeight;
    }

    public int getBaseOffsetY() {
        return baseOffsetY;
    }

    public int getOverlayOffsetX() {
        return overlayOffsetX;
    }

    public int getOverlayOffsetY() {
        return overlayOffsetY;
    }

    public int getOverlayWidth() {
        return overlayWidth;
    }

    public int getOverlayHeight() {
        return overlayHeight;
    }

    public int getVerticalIncrement() {
        return verticalIncrement;
    }

    public ChatFormatting getTextColor() {
        return textColor;
    }

    public void renderBossBar(GuiGraphicsExtractor guiGraphics, int x, int y, BossEvent bossEvent) {
        int baseYOffset = getBaseOffsetY();

        int i = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int j = y - 9;
        int k = i / 2 - 91;
        net.minecraft.util.profiling.Profiler.get().push("customBossBarBase");

        drawBar(guiGraphics, x + 1, y + baseYOffset, bossEvent);
        Component component = bossEvent.getName().copy().withStyle(getTextColor());
        net.minecraft.util.profiling.Profiler.get().pop();

        int l = Minecraft.getInstance().font.width(component);
        int i1 = i / 2 - l / 2;
        int j1 = j;
        guiGraphics.text(Minecraft.getInstance().font, component, i1, j1, -1);

        if (hasOverlay()) {
            net.minecraft.util.profiling.Profiler.get().push("customBossBarOverlay");
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, getOverlayTexture(), x + 1 + getOverlayOffsetX(), y + getOverlayOffsetY() + baseYOffset, 0.0F, 0.0F, getOverlayWidth(), getOverlayHeight(), getOverlayWidth(), getOverlayHeight());
            net.minecraft.util.profiling.Profiler.get().pop();
        }
    }

    private void drawBar(GuiGraphicsExtractor guiGraphics, int x, int y, BossEvent event) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, getBaseTexture(), x, y, 0.0F, 0.0F, 182, getBaseHeight(), 256, getBaseTextureHeight());
        int i = (int)(event.getProgress() * 183.0F);
        if (i > 0) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, getBaseTexture(), x, y, 0.0F, (float) getBaseHeight(), i, getBaseHeight(), 256, getBaseTextureHeight());
        }
    }
}
