package net.neoforged.neoforge.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.neoforged.bus.api.Event;

public class RenderLevelStageEvent extends Event {
    private final LevelRenderState levelRenderState;
    private final PoseStack poseStack;

    public RenderLevelStageEvent(LevelRenderState levelRenderState, PoseStack poseStack) {
        this.levelRenderState = levelRenderState;
        this.poseStack = poseStack;
    }

    public LevelRenderState getLevelRenderState() {
        return levelRenderState;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }

    public static class AfterOpaqueFeatures extends RenderLevelStageEvent {
        public AfterOpaqueFeatures(LevelRenderState levelRenderState, PoseStack poseStack) {
            super(levelRenderState, poseStack);
        }
    }
}
