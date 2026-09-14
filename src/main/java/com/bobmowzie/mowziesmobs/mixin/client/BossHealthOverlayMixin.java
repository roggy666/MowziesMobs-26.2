package com.bobmowzie.mowziesmobs.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.BossEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(BossHealthOverlay.class)
public abstract class BossHealthOverlayMixin {
    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private Map<UUID, LerpingBossEvent> events;
    @Shadow protected abstract void extractBar(GuiGraphicsExtractor graphics, int x, int y, BossEvent event);

    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void mm$onExtractRenderState(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        if (this.events.isEmpty()) {
            return;
        }

        graphics.nextStratum();
        ProfilerFiller profiler = Profiler.get();
        profiler.push("bossHealth");
        int guiWidth = graphics.guiWidth();
        int y = 12;

        for (LerpingBossEvent event : this.events.values()) {
            int x = guiWidth / 2 - 91;
            CustomizeGuiOverlayEvent.BossEventProgress customEvent = new CustomizeGuiOverlayEvent.BossEventProgress(graphics, x, y, event);
            customEvent.setIncrement(19);
            NeoForge.EVENT_BUS.post(customEvent);

            if (!customEvent.isCanceled()) {
                this.extractBar(graphics, x, y, event);
                Component name = event.getName();
                int nameWidth = this.minecraft.font.width(name);
                int nameX = guiWidth / 2 - nameWidth / 2;
                int nameY = y - 9;
                graphics.text(this.minecraft.font, name, nameX, nameY, -1);
            }
            y += customEvent.getIncrement();
        }

        profiler.pop();
        ci.cancel();
    }
}
