package com.bobmowzie.mowziesmobs.client.particle.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.AABB;

/**
 * A particle that draws arbitrary geometry (ribbons, screen-space quads) instead of a single camera-facing
 * square. Rendered by {@link CustomParticleGroup}; vertices are camera-relative and use the particle vertex format
 * (position, uv, color, light).
 */
public interface CustomGeometryParticle {
    void renderCustom(VertexConsumer buffer, Camera camera, float partialTicks);

    AABB getCustomBoundingBox();

    /** Whether the particle is drawn regardless of frustum culling. */
    default boolean isAlwaysVisible() {
        return false;
    }
}
