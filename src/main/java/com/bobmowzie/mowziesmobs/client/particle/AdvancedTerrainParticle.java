package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.particle.types.AdvancedParticleType;
import com.bobmowzie.mowziesmobs.client.particle.types.TerrainParticleType;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.client.render.MMRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AdvancedTerrainParticle extends AdvancedParticleBase {
    private final BlockPos pos;
    private final float uo;
    private final float vo;

    protected AdvancedTerrainParticle(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double motionX, double motionY, double motionZ, double scale, double drag, double duration, boolean canCollide, BlockState state, BlockPos pos, ParticleComponent[] components) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, motionX, motionY, motionZ, new ParticleRotation.FaceCamera(0), scale, 1.0, 1.0, 1.0, 1.0, drag, duration, false, canCollide, components);
        this.pos = pos;
        this.setSprite(Minecraft.getInstance().getModelManager().getBlockStateModelSet().getParticleMaterial(state).sprite());
        this.red = 0.6F;
        this.green = 0.6F;
        this.blue = 0.6F;

        // The NeoForge hook this used to go through (IClientBlockExtensions#areBreakingParticlesTinted) was
        // removed; vanilla's own TerrainParticle now looks up tinting the same way below, so this mirrors that.
        BlockTintSource tintSource = Minecraft.getInstance().getBlockColors().getTintSource(state, 0);
        if (tintSource != null) {
            int i = tintSource.colorAsTerrainParticle(state, worldIn, pos);
            this.red *= (float)(i >> 16 & 255) / 255.0F;
            this.green *= (float)(i >> 8 & 255) / 255.0F;
            this.blue *= (float)(i & 255) / 255.0F;
        }

        this.quadSize /= 2.0F;
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
        this.layer = SingleQuadParticle.Layer.bySprite(this.sprite);
    }

    public Particle updateSprite(BlockState state, BlockPos pos) {
        if (state != null) {
            this.setSprite(Minecraft.getInstance().getModelManager().getBlockStateModelSet().getParticleMaterial(state).sprite());
            this.layer = SingleQuadParticle.Layer.bySprite(this.sprite);
        }
        return this;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return this.layer != null ? this.layer : SingleQuadParticle.Layer.bySprite(this.sprite);
    }

    @Override
    protected float getU0() {
        return this.sprite.getU((this.uo + 1.0F) / 4.0F);
    }

    @Override
    protected float getU1() {
        return this.sprite.getU(this.uo / 4.0F);
    }

    @Override
    protected float getV0() {
        return this.sprite.getV(this.vo / 4.0F);
    }

    @Override
    protected float getV1() {
        return this.sprite.getV((this.vo + 1.0F) / 4.0F);
    }

    @Override
    public int getLightCoords(float p_108291_) {
        int i = super.getLightCoords(p_108291_);
        return i == 0 && this.level.hasChunkAt(this.pos) ? net.minecraft.util.LightCoordsUtil.getLightCoords(this.level, this.pos) : i;
    }

    public static class Factory implements ParticleProvider<TerrainParticleType> {
        private final SpriteSet spriteSet;

        public Factory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public Particle createParticle(TerrainParticleType typeIn, @NotNull ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            BlockState blockstate = typeIn.state();

            if (blockstate.isAir() || blockstate.is(Blocks.MOVING_PISTON)) {
                return null;
            }

            AdvancedTerrainParticle particle = new AdvancedTerrainParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.scale(), typeIn.airDrag(), typeIn.duration(), typeIn.canCollide(), typeIn.state(), BlockPos.ZERO, typeIn.components());
            particle.setColor(typeIn.red(), typeIn.green(), typeIn.blue());
            particle.updateSprite(blockstate, typeIn.position());
            return particle;
        }
    }

    public static void spawnTerrainParticle(Level world, ParticleType<?> particle, double x, double y, double z, double motionX, double motionY, double motionZ, double rotation, double scale, double drag, double duration, BlockState state, ParticleComponent[] components) {
        AdvancedParticleType base = new AdvancedParticleType(particle, new ParticleRotation.FaceCamera((float) rotation), components, 0.6f, 0.6f, 0.6f, 1, (float) scale, (float) duration, (float) drag, false, false);
        world.addParticle(new TerrainParticleType(base, state), x, y, z, motionX, motionY, motionZ);
    }
}
