package com.bobmowzie.mowziesmobs.client.render.block;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.ClientProxy;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Draws a scrolling glow over the blocks that obstruct the Sculptor's test (see {@link ClientProxy#sculptorMarkBlock}).
 */
public final class SculptorBlockHighlightRenderer {
    private static final Identifier SCULPTOR_BLOCK_GLOW = MMCommon.resource("textures/entity/sculptor_highlight.png");
    private static final float INFLATE = 0.004F;

    private SculptorBlockHighlightRenderer() {}

    public static void register() {
        LevelRenderEvents.AFTER_SOLID_FEATURES.register(SculptorBlockHighlightRenderer::render);
    }

    private static void render(LevelRenderContext context) {
        if (ClientProxy.sculptorMarkedBlocks.isEmpty()) return;
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;

        Vec3 cameraPos = context.levelState().cameraRenderState.pos;
        float tick = level.getGameTime() % 24000 + Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);

        for (Long2ObjectMap.Entry<SculptorBlockMarking> entry : ClientProxy.sculptorMarkedBlocks.long2ObjectEntrySet()) {
            BlockPos pos = BlockPos.of(entry.getLongKey());
            BlockState state = level.getBlockState(pos);
            VoxelShape shape = state.getShape(level, pos);
            if (shape.isEmpty()) continue;
            AABB box = shape.bounds().inflate(INFLATE);

            float blockOffset = (pos.getX() + pos.getY() + pos.getZ()) * 0.25f;
            PoseStack poseStack = context.poseStack();
            poseStack.pushPose();
            poseStack.translate(pos.getX() - cameraPos.x(), pos.getY() - cameraPos.y(), pos.getZ() - cameraPos.z());
            int light = LightCoordsUtil.FULL_BRIGHT;
            context.submitNodeCollector().submitCustomGeometry(poseStack, RenderTypes.energySwirl(SCULPTOR_BLOCK_GLOW, tick * 0.02f + blockOffset, tick * 0.01f + blockOffset), (pose, buffer) -> {
                for (Direction direction : Direction.values()) {
                    drawFace(buffer, pose, box, direction, light);
                }
            });
            poseStack.popPose();
        }
    }

    private static void drawFace(VertexConsumer buffer, PoseStack.Pose pose, AABB box, Direction direction, int light) {
        float x0 = (float) box.minX, y0 = (float) box.minY, z0 = (float) box.minZ;
        float x1 = (float) box.maxX, y1 = (float) box.maxY, z1 = (float) box.maxZ;
        float[][] corners = switch (direction) {
            case DOWN -> new float[][]{{x0, y0, z1}, {x0, y0, z0}, {x1, y0, z0}, {x1, y0, z1}};
            case UP -> new float[][]{{x0, y1, z0}, {x0, y1, z1}, {x1, y1, z1}, {x1, y1, z0}};
            case NORTH -> new float[][]{{x1, y1, z0}, {x1, y0, z0}, {x0, y0, z0}, {x0, y1, z0}};
            case SOUTH -> new float[][]{{x0, y1, z1}, {x0, y0, z1}, {x1, y0, z1}, {x1, y1, z1}};
            case WEST -> new float[][]{{x0, y1, z0}, {x0, y0, z0}, {x0, y0, z1}, {x0, y1, z1}};
            case EAST -> new float[][]{{x1, y1, z1}, {x1, y0, z1}, {x1, y0, z0}, {x1, y1, z0}};
        };
        float[][] uvs = {{0, 0}, {0, 1}, {1, 1}, {1, 0}};
        for (int i = 0; i < 4; i++) {
            buffer.addVertex(pose, corners[i][0], corners[i][1], corners[i][2])
                    .setColor(255, 255, 255, 255)
                    .setUv(uvs[i][0], uvs[i][1])
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(pose, direction.getStepX(), direction.getStepY(), direction.getStepZ());
        }
    }
}
