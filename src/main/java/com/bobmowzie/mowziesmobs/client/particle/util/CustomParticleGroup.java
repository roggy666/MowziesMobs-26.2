package com.bobmowzie.mowziesmobs.client.particle.util;

import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleGroup;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.ParticleGroupRenderState;

import java.util.ArrayList;
import java.util.List;

/**
 * Particle group for {@link CustomGeometryParticle}s: the vanilla quad pipeline can only draw rotated squares, so
 * these particles write their own vertices (camera-relative) through {@code submitCustomGeometry}.
 */
public class CustomParticleGroup extends ParticleGroup<Particle> {
    public static final ParticleRenderType RENDER_TYPE = new ParticleRenderType("mowziesmobs:custom", "MMC");

    public CustomParticleGroup(ParticleEngine engine) {
        super(engine);
    }

    @Override
    public ParticleGroupRenderState extractRenderState(Frustum frustum, Camera camera, float partialTickTime) {
        List<CustomGeometryParticle> visible = new ArrayList<>();
        for (Particle particle : this.particles) {
            if (particle instanceof CustomGeometryParticle custom && (custom.isAlwaysVisible() || frustum.isVisible(custom.getCustomBoundingBox()))) {
                visible.add(custom);
            }
        }
        return new State(visible, camera, partialTickTime);
    }

    private record State(List<CustomGeometryParticle> particles, Camera camera, float partialTick) implements ParticleGroupRenderState {
        @Override
        public void submit(SubmitNodeCollector submitNodeCollector, CameraRenderState cameraState) {
            if (particles.isEmpty()) return;
            submitNodeCollector.submitCustomGeometry(new PoseStack(), MMRenderType.CUSTOM_PARTICLES, (pose, buffer) -> {
                for (CustomGeometryParticle particle : particles) {
                    particle.renderCustom(buffer, camera, partialTick);
                }
            });
        }
    }
}
