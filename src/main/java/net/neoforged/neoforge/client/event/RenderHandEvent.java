package net.neoforged.neoforge.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.InteractionHand;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class RenderHandEvent extends Event implements ICancellableEvent {
    private final InteractionHand hand;
    private final PoseStack poseStack;
    private final SubmitNodeCollector submitNodeCollector;
    private final int packedLight;
    private final float partialTick;
    private final float interpolatedPitch;
    private final float swingProgress;
    private final float equipProgress;
    private final net.minecraft.world.item.ItemStack itemStack;
    private boolean canceled;

    public RenderHandEvent(InteractionHand hand, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, float partialTick, float interpolatedPitch, float swingProgress, float equipProgress, net.minecraft.world.item.ItemStack itemStack) {
        this.hand = hand;
        this.poseStack = poseStack;
        this.submitNodeCollector = submitNodeCollector;
        this.packedLight = packedLight;
        this.partialTick = partialTick;
        this.interpolatedPitch = interpolatedPitch;
        this.swingProgress = swingProgress;
        this.equipProgress = equipProgress;
        this.itemStack = itemStack;
    }

    public RenderHandEvent(InteractionHand hand, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, float partialTick, float interpolatedPitch, float swingProgress, float equipProgress) {
        this(hand, poseStack, submitNodeCollector, packedLight, partialTick, interpolatedPitch, swingProgress, equipProgress, net.minecraft.world.item.ItemStack.EMPTY);
    }

    public InteractionHand getHand() {
        return hand;
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

    public float getInterpolatedPitch() {
        return interpolatedPitch;
    }

    public float getSwingProgress() {
        return swingProgress;
    }

    public float getEquipProgress() {
        return equipProgress;
    }

    public net.minecraft.world.item.ItemStack getItemStack() {
        return itemStack;
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
