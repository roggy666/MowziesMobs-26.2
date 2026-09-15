package com.bobmowzie.mowziesmobs.mixin.client;

import com.bobmowzie.mowziesmobs.client.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

/**
 * Replaces vanilla boss bar extraction so that bosses with a {@link com.bobmowzie.mowziesmobs.client.gui.CustomBossBar}
 * render their own bar (and advance the stacking offset by their own height).
 */
@Mixin(BossHealthOverlay.class)
public abstract class BossHealthOverlayMixin {
    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private Map<UUID, LerpingBossEvent> events;
    @Shadow protected abstract void extractBar(GuiGraphicsExtractor graphics, int x, int y, BossEvent event);

    @Inject(method = "extractRenderState", at = @At("HEAD"), cancellable = true)
    private void mowziesmobs$onExtractRenderState(GuiGraphicsExtractor graphics, CallbackInfo ci) {
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
            int increment = ClientEventHandler.onRenderBossBar(graphics, x, y, event);

            if (increment < 0) {
                increment = 19;
                this.extractBar(graphics, x, y, event);
                Component name = event.getName();
                int nameWidth = this.minecraft.font.width(name);
                int nameX = guiWidth / 2 - nameWidth / 2;
                int nameY = y - 9;
                graphics.text(this.minecraft.font, name, nameX, nameY, -1);
            }
            y += increment;
            if (y >= graphics.guiHeight() / 3) {
                break;
            }
        }

        profiler.pop();
        ci.cancel();
    }
}
