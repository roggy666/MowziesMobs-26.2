package com.bobmowzie.mowziesmobs.client.particle;

import com.bobmowzie.mowziesmobs.client.particle.util.CustomParticleGroup;
import net.fabricmc.fabric.api.client.particle.v1.ParticleGroupRegistry;
import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.particle.types.AdvancedParticleType;
import com.bobmowzie.mowziesmobs.client.particle.types.DecalParticleType;
import com.bobmowzie.mowziesmobs.client.particle.types.RibbonParticleType;
import com.bobmowzie.mowziesmobs.client.particle.types.TerrainParticleType;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public class ParticleHandler {
    private static <T extends ParticleType<?>> T register(String name, T type) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, MMCommon.resource(name), type);
    }

    public static void register() {
    }

    public static final SimpleParticleType SPARKLE = register("sparkle", new SimpleParticleType(false));

    public static final ParticleType<ParticleVanillaCloudExtended.Data> VANILLA_CLOUD_EXTENDED = register("vanilla_cloud_extended", new ParticleType<>(false) {
        @Override
        public @NotNull MapCodec<ParticleVanillaCloudExtended.Data> codec() {
            return ParticleVanillaCloudExtended.Data.CODEC;
        }

        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, ParticleVanillaCloudExtended.Data> streamCodec() {
            return ParticleVanillaCloudExtended.Data.STREAM_CODEC;
        }
    });

    public static final ParticleType<ParticleSnowFlake.Data> SNOWFLAKE = register("snowflake", new ParticleType<>(false) {
        @Override
        public @NotNull MapCodec<ParticleSnowFlake.Data> codec() {
            return ParticleSnowFlake.Data.CODEC;
        }

        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, ParticleSnowFlake.Data> streamCodec() {
            return ParticleSnowFlake.Data.STREAM_CODEC;
        }
    });

    public static final ParticleType<ParticleCloud.Data> CLOUD = register("cloud_soft", new ParticleType<>(false) {
        @Override
        public @NotNull MapCodec<ParticleCloud.Data> codec() {
            return ParticleCloud.Data.CODEC;
        }

        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, ParticleCloud.Data> streamCodec() {
            return ParticleCloud.Data.STREAM_CODEC;
        }
    });

    public static final ParticleType<ParticleOrb.Data> ORB = register("orb_0", new ParticleType<>(false) {
        @Override
        public @NotNull MapCodec<ParticleOrb.Data> codec() {
            return ParticleOrb.Data.CODEC;
        }

        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, ParticleOrb.Data> streamCodec() {
            return ParticleOrb.Data.STREAM_CODEC;
        }
    });

    public static final ParticleType<ParticleRing.Data> RING = register("ring_0", new ParticleType<>(false) {
        @Override
        public @NotNull MapCodec<ParticleRing.Data> codec() {
            return ParticleRing.Data.CODEC;
        }

        @Override
        public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, ParticleRing.Data> streamCodec() {
            return ParticleRing.Data.STREAM_CODEC;
        }
    });

    public static final ParticleType<AdvancedParticleType> RING2 = registerAdvanced("ring");
    public static final ParticleType<AdvancedParticleType> RING_BIG = registerAdvanced("ring_big");
    public static final ParticleType<AdvancedParticleType> PIXEL = registerAdvanced("pixel");
    public static final ParticleType<AdvancedParticleType> ORB2 = registerAdvanced("orb");
    public static final ParticleType<AdvancedParticleType> EYE = registerAdvanced("eye");
    public static final ParticleType<AdvancedParticleType> BUBBLE = registerAdvanced("bubble");
    public static final ParticleType<AdvancedParticleType> SUN = registerAdvanced("sun");
    public static final ParticleType<AdvancedParticleType> SUN_NOVA = registerAdvanced("sun_nova");
    public static final ParticleType<AdvancedParticleType> FLARE = registerAdvanced("flare");
    public static final ParticleType<AdvancedParticleType> FLARE_RADIAL = registerAdvanced("flare_radial");
    public static final ParticleType<AdvancedParticleType> BURST_IN = registerAdvanced("ring1");
    public static final ParticleType<AdvancedParticleType> BURST_MESSY = registerAdvanced("burst_messy");
    public static final ParticleType<AdvancedParticleType> RING_SPARKS = registerAdvanced("sparks_ring");
    public static final ParticleType<AdvancedParticleType> BURST_OUT = registerAdvanced("ring2");
    public static final ParticleType<AdvancedParticleType> GLOW = registerAdvanced("glow");
    public static final ParticleType<AdvancedParticleType> ARROW_HEAD = registerAdvanced("arrow_head");
    public static final ParticleType<AdvancedParticleType> LEAF = registerAdvanced("leaf");
    public static final ParticleType<AdvancedParticleType> MOON_FULL = registerAdvanced("moon_full");
    public static final ParticleType<AdvancedParticleType> MOON_GIBBOUS = registerAdvanced("moon_gibbous");
    public static final ParticleType<AdvancedParticleType> MOON_HALF = registerAdvanced("moon_half");
    public static final ParticleType<AdvancedParticleType> MOON_CRESCENT = registerAdvanced("moon_crescent");
    public static final ParticleType<AdvancedParticleType> MOON_NEW = registerAdvanced("moon_new");

    public static final ParticleType<TerrainParticleType> TERRAIN = registerTerrain("terrain");
    public static final ParticleType<DecalParticleType> PLAYER_FOOTPRINT = registerDecal("player_footprint");

    public static final ParticleType<DecalParticleType> STRIX_FOOTPRINT = registerDecal("strix_footprint");
    public static final ParticleType<DecalParticleType> GROUND_CRACK = registerDecal("crack");

    public static final ParticleType<RibbonParticleType> RIBBON_FLAT = registerRibbon("ribbon_flat");
    public static final ParticleType<RibbonParticleType> RIBBON_STREAKS = registerRibbon("ribbon_streaks");
    public static final ParticleType<RibbonParticleType> RIBBON_GLOW = registerRibbon("ribbon_glow");
    public static final ParticleType<RibbonParticleType> RIBBON_SQUIGGLE = registerRibbon("ribbon_squiggle");

    public static void registerParticles() {
        ParticleGroupRegistry.register(CustomParticleGroup.RENDER_TYPE, CustomParticleGroup::new);
        ParticleProviderRegistry event = ParticleProviderRegistry.getInstance();
        event.register(ParticleHandler.SPARKLE, ParticleSparkle.Provider::new);
        event.register(ParticleHandler.VANILLA_CLOUD_EXTENDED, ParticleVanillaCloudExtended.Provider::new);
        event.register(ParticleHandler.SNOWFLAKE, ParticleSnowFlake.Provider::new);
        event.register(ParticleHandler.CLOUD, ParticleCloud.Provider::new);
        event.register(ParticleHandler.ORB, ParticleOrb.Provider::new);
        event.register(ParticleHandler.RING, ParticleRing.Provider::new);

        event.register(ParticleHandler.RING2, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.RING_BIG, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.PIXEL, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.ORB2, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.EYE, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.BUBBLE, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.SUN, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.SUN_NOVA, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.FLARE, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.FLARE_RADIAL, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.BURST_IN, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.BURST_MESSY, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.RING_SPARKS, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.BURST_OUT, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.GLOW, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.ARROW_HEAD, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.LEAF, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.TERRAIN, AdvancedTerrainParticle.Factory::new);
        event.register(ParticleHandler.MOON_FULL, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.MOON_GIBBOUS, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.MOON_HALF, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.MOON_CRESCENT, AdvancedParticleBase.Factory::new);
        event.register(ParticleHandler.MOON_NEW, AdvancedParticleBase.Factory::new);

        event.register(ParticleHandler.STRIX_FOOTPRINT, ParticleDecal.Provider::new);
        event.register(ParticleHandler.GROUND_CRACK, ParticleDecal.Provider::new);
        event.register(ParticleHandler.PLAYER_FOOTPRINT, ParticleDecal.Provider::new);

        event.register(ParticleHandler.RIBBON_FLAT, ParticleRibbon.Provider::new);
        event.register(ParticleHandler.RIBBON_STREAKS, ParticleRibbon.Provider::new);
        event.register(ParticleHandler.RIBBON_GLOW, ParticleRibbon.Provider::new);
        event.register(ParticleHandler.RIBBON_SQUIGGLE, ParticleRibbon.Provider::new);
    }

    private static ParticleType<AdvancedParticleType> registerAdvanced(String key) {
        return register(key, new ParticleType<>(false) {
            @Override
            public @NotNull MapCodec<AdvancedParticleType> codec() {
                return AdvancedParticleType.CODEC;
            }

            @Override
            public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, AdvancedParticleType> streamCodec() {
                return AdvancedParticleType.STREAM_CODEC;
            }
        });
    }

    private static ParticleType<DecalParticleType> registerDecal(String key) {
        return register(key, new ParticleType<>(false) {
            @Override
            public @NotNull MapCodec<DecalParticleType> codec() {
                return DecalParticleType.CODEC;
            }

            @Override
            public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, DecalParticleType> streamCodec() {
                return DecalParticleType.STREAM_CODEC;
            }
        });
    }

    private static ParticleType<RibbonParticleType> registerRibbon(String key) {
        return register(key, new ParticleType<>(false) {
            @Override
            public @NotNull MapCodec<RibbonParticleType> codec() {
                return RibbonParticleType.CODEC;
            }

            @Override
            public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, RibbonParticleType> streamCodec() {
                return RibbonParticleType.STREAM_CODEC;
            }
        });
    }

    private static ParticleType<TerrainParticleType> registerTerrain(String key) {
        return register(key, new ParticleType<>(false) {
            @Override
            public @NotNull MapCodec<TerrainParticleType> codec() {
                return TerrainParticleType.CODEC;
            }

            @Override
            public @NotNull StreamCodec<? super RegistryFriendlyByteBuf, TerrainParticleType> streamCodec() {
                return TerrainParticleType.STREAM_CODEC;
            }
        });
    }
}
