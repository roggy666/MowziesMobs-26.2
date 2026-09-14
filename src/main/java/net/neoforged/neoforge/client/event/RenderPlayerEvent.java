package net.neoforged.neoforge.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class RenderPlayerEvent extends Event {
    public static class Pre<S extends AvatarRenderState> extends RenderPlayerEvent implements ICancellableEvent {
        private final S renderState;
        private final PoseStack poseStack;
        private final SubmitNodeCollector submitNodeCollector;
        private final int packedLight;
        private final float partialTick;
        private boolean canceled;

        public Pre(S renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, float partialTick) {
            this.renderState = renderState;
            this.poseStack = poseStack;
            this.submitNodeCollector = submitNodeCollector;
            this.packedLight = packedLight;
            this.partialTick = partialTick;
        }

        public Pre(S renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight) {
            this(renderState, poseStack, submitNodeCollector, packedLight, 0.0f);
        }

        public S getRenderState() {
            return renderState;
        }

        public PoseStack getPoseStack() {
            return poseStack;
        }

        public SubmitNodeCollector getSubmitNodeCollector() {
            return submitNodeCollector;
        }

        public int getPackedLight() {
            return packedLight;
        }

        public float getPartialTick() {
            return partialTick;
        }

        @Override
        public boolean isCanceled() {
            return canceled;
        }

        @Override
        public void setCanceled(boolean cancel) {
            this.canceled = cancel;
        }
    }

    public static class Post<S extends AvatarRenderState> extends RenderPlayerEvent {
        private final S renderState;
        private final PoseStack poseStack;
        private final SubmitNodeCollector submitNodeCollector;
        private final int packedLight;
        private final float partialTick;

        public Post(S renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, float partialTick) {
            this.renderState = renderState;
            this.poseStack = poseStack;
            this.submitNodeCollector = submitNodeCollector;
            this.packedLight = packedLight;
            this.partialTick = partialTick;
        }

        public Post(S renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight) {
            this(renderState, poseStack, submitNodeCollector, packedLight, 0.0f);
        }

        public S getRenderState() {
            return renderState;
        }

        public PoseStack getPoseStack() {
            return poseStack;
        }

        public SubmitNodeCollector getSubmitNodeCollector() {
            return submitNodeCollector;
        }

        public int getPackedLight() {
            return packedLight;
        }

        public float getPartialTick() {
            return partialTick;
        }
    }
}
