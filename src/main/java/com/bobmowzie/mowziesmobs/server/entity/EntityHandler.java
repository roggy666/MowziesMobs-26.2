package com.bobmowzie.mowziesmobs.server.entity;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.entity.bluff.EntityBluff;
import com.bobmowzie.mowziesmobs.server.entity.effects.*;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.*;
import com.bobmowzie.mowziesmobs.server.entity.elokosa.EntityElokosa;
import com.bobmowzie.mowziesmobs.server.entity.elokosa.EntityElokosaFollowerToHowler;
import com.bobmowzie.mowziesmobs.server.entity.elokosa.EntityElokosaHowler;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityBabyFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrozenController;
import com.bobmowzie.mowziesmobs.server.entity.grottol.EntityGrottol;
import com.bobmowzie.mowziesmobs.server.entity.lantern.EntityLantern;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.*;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.trade.Trade;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.Optional;

public class EntityHandler {
    public static final EntityDataSerializer<Optional<Trade>> OPTIONAL_TRADE = registerSerializer("optional_trade", EntityDataSerializer.forValueType(Trade.STREAM_CODEC.apply(ByteBufCodecs::optional)));
    public static final EntityDataSerializer<Optional<java.util.UUID>> OPTIONAL_UUID = registerSerializer("optional_uuid", EntityDataSerializer.forValueType(ByteBufCodecs.optional(net.minecraft.core.UUIDUtil.STREAM_CODEC)));
    public static final EntityDataSerializer<net.minecraft.nbt.CompoundTag> COMPOUND_TAG = registerSerializer("compound_tag", EntityDataSerializer.forValueType(ByteBufCodecs.TRUSTED_COMPOUND_TAG));

    public static final EntityType<EntityFoliaath> FOLIAATH = register("foliaath", EntityType.Builder.of(EntityFoliaath::new, MobCategory.MONSTER).sized(0.5f, 2.5f).clientTrackingRange(8));
    public static final EntityType<EntityBabyFoliaath> BABY_FOLIAATH = register("baby_foliaath", EntityType.Builder.of(EntityBabyFoliaath::new, MobCategory.MONSTER).clientTrackingRange(8).sized(0.4f, 0.4f).clientTrackingRange(8));
    public static final EntityType<EntityWroughtnaut> WROUGHTNAUT = register("ferrous_wroughtnaut", EntityType.Builder.of(EntityWroughtnaut::new, MobCategory.MONSTER).clientTrackingRange(8).sized(2.5f, 3.5f).clientTrackingRange(8).updateInterval(1));
    private static EntityType.Builder<EntityUmvuthanaFollowerToRaptor> umvuthanaFollowerToRaptorBuilder() {
        return EntityType.Builder.of(EntityUmvuthanaFollowerToRaptor::new, MobCategory.MONSTER);
    }
    public static final EntityType<EntityUmvuthanaFollowerToRaptor> UMVUTHANA_FOLLOWER_TO_RAPTOR = register("umvuthana_follower_raptor", umvuthanaFollowerToRaptorBuilder().sized(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).clientTrackingRange(8).updateInterval(1));
    private static EntityType.Builder<EntityUmvuthanaFollowerToPlayer> umvuthanaFollowerToPlayerBuilder() {
        return EntityType.Builder.of(EntityUmvuthanaFollowerToPlayer::new, MobCategory.MONSTER);
    }
    public static final EntityType<EntityUmvuthanaFollowerToPlayer> UMVUTHANA_FOLLOWER_TO_PLAYER = register("umvuthana_follower_player", umvuthanaFollowerToPlayerBuilder().sized(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).clientTrackingRange(8).updateInterval(1));
    private static EntityType.Builder<EntityUmvuthanaCraneToPlayer> umvuthanaCraneToPlayerBuilder() {
        return EntityType.Builder.of(EntityUmvuthanaCraneToPlayer::new, MobCategory.MONSTER);
    }
    public static final EntityType<EntityUmvuthanaCraneToPlayer> UMVUTHANA_CRANE_TO_PLAYER = register("umvuthana_crane_player", umvuthanaCraneToPlayerBuilder().sized(MaskType.FAITH.entityWidth, MaskType.FAITH.entityHeight).clientTrackingRange(8).updateInterval(1));
    public static final EntityType<EntityUmvuthanaMinion> UMVUTHANA_MINION = register("umvuthana", EntityType.Builder.of(EntityUmvuthanaMinion::new, MobCategory.MONSTER).sized(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).clientTrackingRange(8).updateInterval(1));
    public static final EntityType<EntityUmvuthanaRaptor> UMVUTHANA_RAPTOR = register("umvuthana_raptor", EntityType.Builder.of(EntityUmvuthanaRaptor::new, MobCategory.MONSTER).sized(MaskType.FURY.entityWidth, MaskType.FURY.entityHeight).clientTrackingRange(8).updateInterval(1).notInPeaceful());
    public static final EntityType<EntityUmvuthanaCrane> UMVUTHANA_CRANE = register("umvuthana_crane", EntityType.Builder.of(EntityUmvuthanaCrane::new, MobCategory.MONSTER).sized(MaskType.FEAR.entityWidth, MaskType.FEAR.entityHeight).clientTrackingRange(8).updateInterval(1));
    public static final EntityType<EntityUmvuthi> UMVUTHI = register("umvuthi", EntityType.Builder.of(EntityUmvuthi::new, MobCategory.MONSTER).sized(1.5f, 3.2f).clientTrackingRange(10).updateInterval(1));
    public static final EntityType<EntityFrostmaw> FROSTMAW = register("frostmaw", EntityType.Builder.of(EntityFrostmaw::new, MobCategory.MONSTER).sized(4f, 4f).clientTrackingRange(10).updateInterval(1));
    public static final EntityType<EntityGrottol> GROTTOL = register("grottol", EntityType.Builder.of(EntityGrottol::new, MobCategory.MONSTER).sized(0.9F, 1.2F).clientTrackingRange(8).updateInterval(1));
    public static final EntityType<EntityLantern> LANTERN = register("lantern", EntityType.Builder.of(EntityLantern::new, MobCategory.AMBIENT).sized(1.0f, 1.0f).clientTrackingRange(8).updateInterval(1));
    public static final EntityType<EntityNaga> NAGA = register("naga", EntityType.Builder.of(EntityNaga::new, MobCategory.MONSTER).sized(3.0f, 1.0f).clientTrackingRange(13).canSpawnFarFromPlayer().updateInterval(1));
    public static final EntityType<EntitySculptor> SCULPTOR = register("sculptor", EntityType.Builder.of(EntitySculptor::new, MobCategory.MISC).sized(1.0f, 2.3f).clientTrackingRange(8).updateInterval(1));
    public static final EntityType<EntityBluff> BLUFF = register("bluff", EntityType.Builder.of(EntityBluff::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8).updateInterval(1));
    private static EntityType.Builder<EntityElokosaFollowerToHowler> elokosaFollowerToHowlerBuilder() {
        return EntityType.Builder.of(EntityElokosaFollowerToHowler::new, MobCategory.MONSTER);
    }
    public static final EntityType<EntityElokosaFollowerToHowler> ELOKOSA_FOLLOWER_TO_HOWLER = register("elokosa_follower_howler", elokosaFollowerToHowlerBuilder().sized(0.9F, 1.6F).clientTrackingRange(8).updateInterval(1).notInPeaceful());
    public static final EntityType<EntityElokosaHowler> ELOKOSA_HOWLER = register("elokosa_howler", EntityType.Builder.of(EntityElokosaHowler::new, MobCategory.MONSTER).sized(0.95F, 1.7F).clientTrackingRange(8).updateInterval(1).notInPeaceful());

    private static EntityType.Builder<EntitySunstrike> sunstrikeBuilder() {
        return EntityType.Builder.of(EntitySunstrike::new, MobCategory.MISC);
    }
    public static final EntityType<EntitySunstrike> SUNSTRIKE = register("sunstrike", sunstrikeBuilder().sized(0.1F, 0.1F));
    private static EntityType.Builder<EntitySolarBeam> solarBeamBuilder() {
        return EntityType.Builder.of(EntitySolarBeam::new, MobCategory.MISC);
    }
    public static final EntityType<EntitySolarBeam> SOLAR_BEAM = register("solar_beam", solarBeamBuilder().sized(0.1F, 0.1F).updateInterval(1));
    private static EntityType.Builder<EntityBoulderProjectile> boulderProjectileBuilder() {
        return EntityType.Builder.of(EntityBoulderProjectile::new, MobCategory.MISC);
    }
    public static final EntityType<EntityBoulderProjectile> BOULDER_PROJECTILE = register("boulder_projectile", boulderProjectileBuilder().sized(1, 1).updateInterval(1));
    private static EntityType.Builder<EntityRockSling> rockSlingBuilder() {
        return EntityType.Builder.of(EntityRockSling::new, MobCategory.MISC);
    }
    public static final EntityType<EntityRockSling> ROCK_SLING = register("rock_sling", rockSlingBuilder().sized(0.5f, 0.5f).updateInterval(1));
    private static EntityType.Builder<EntityBoulderSculptor> boulderPlatformBuilder() {
        return EntityType.Builder.of(EntityBoulderSculptor::new, MobCategory.MISC);
    }
    public static final EntityType<EntityBoulderSculptor> BOULDER_SCULPTOR = register("boulder_platform", boulderPlatformBuilder().sized(1, 1).updateInterval(1));
    private static EntityType.Builder<EntityBoulderSculptor.EntityBoulderSculptorCrumbling> boulderPlatformCrumblingBuilder() {
        return EntityType.Builder.of(EntityBoulderSculptor.EntityBoulderSculptorCrumbling::new, MobCategory.MISC);
    }
    public static final EntityType<EntityBoulderSculptor.EntityBoulderSculptorCrumbling> BOULDER_SCULPTOR_CRUMBLING = register("boulder_platform_crumbling", boulderPlatformCrumblingBuilder().sized(1, 1).updateInterval(1));
    private static EntityType.Builder<EntityPillar> pillarBuilder() {
        return EntityType.Builder.of(EntityPillar::new, MobCategory.MISC);
    }
    public static final EntityType<EntityPillar> PILLAR = register("pillar", pillarBuilder().sized(1f, 1f).updateInterval(1));
    private static EntityType.Builder<EntityPillar.EntityPillarSculptor> sculptorPillarBuilder() {
        return EntityType.Builder.of(EntityPillar.EntityPillarSculptor::new, MobCategory.MISC);
    }
    public static final EntityType<EntityPillar.EntityPillarSculptor> PILLAR_SCULPTOR = register("pillar_sculptor", sculptorPillarBuilder().sized(1f, 1f).clientTrackingRange(8).updateInterval(1));
    private static EntityType.Builder<EntityPillarPiece> pillarPieceBuilder() {
        return EntityType.Builder.of(EntityPillarPiece::new, MobCategory.MISC);
    }
    public static final EntityType<EntityPillarPiece> PILLAR_PIECE = register("pillar_piece", pillarPieceBuilder().sized(1f, 1f).updateInterval(1));

    private static EntityType.Builder<EntityAxeAttack> axeAttackBuilder() {
        return EntityType.Builder.of(EntityAxeAttack::new, MobCategory.MISC);
    }
    public static final EntityType<EntityAxeAttack> AXE_ATTACK = register("axe_attack", axeAttackBuilder().sized(1f, 1f).updateInterval(1));
    private static EntityType.Builder<EntityIceBreath> iceBreathBuilder() {
        return EntityType.Builder.of(EntityIceBreath::new, MobCategory.MISC);
    }
    public static final EntityType<EntityIceBreath> ICE_BREATH = register("ice_breath", iceBreathBuilder().sized(0F, 0F).updateInterval(1));
    private static EntityType.Builder<EntityIceBall> iceBallBuilder() {
        return EntityType.Builder.of(EntityIceBall::new, MobCategory.MISC);
    }
    public static final EntityType<EntityIceBall> ICE_BALL = register("ice_ball", iceBallBuilder().sized(0.5F, 0.5F).updateInterval(20));
    private static EntityType.Builder<EntityFrozenController> frozenControllerBuilder() {
        return EntityType.Builder.of(EntityFrozenController::new, MobCategory.MISC);
    }
    public static final EntityType<EntityFrozenController> FROZEN_CONTROLLER = register("frozen_controller", frozenControllerBuilder().noSummon().sized(0, 0));
    private static EntityType.Builder<EntityDart> dartBuilder() {
        return EntityType.Builder.of(EntityDart::new, MobCategory.MISC);
    }
    public static final EntityType<EntityDart> DART = register("dart", dartBuilder().noSummon().sized(0.5F, 0.5F).updateInterval(20));
    private static EntityType.Builder<EntityPoisonBall> poisonBallBuilder() {
        return EntityType.Builder.of(EntityPoisonBall::new, MobCategory.MISC);
    }
    public static final EntityType<EntityPoisonBall> POISON_BALL = register("poison_ball", poisonBallBuilder().sized(0.5F, 0.5F).updateInterval(20));
    private static EntityType.Builder<EntitySuperNova> superNovaBuilder() {
        return EntityType.Builder.of(EntitySuperNova::new, MobCategory.MISC);
    }
    public static final EntityType<EntitySuperNova> SUPER_NOVA = register("super_nova", superNovaBuilder().sized(1, 1).updateInterval(Integer.MAX_VALUE));
    private static EntityType.Builder<EntityFallingBlock> fallingBlockBuilder() {
        return EntityType.Builder.of(EntityFallingBlock::new, MobCategory.MISC);
    }
    public static final EntityType<EntityFallingBlock> FALLING_BLOCK = register("falling_block", fallingBlockBuilder().sized(1, 1));
    private static EntityType.Builder<EntityBlockSwapper> blockSwapperBuilder() {
        return EntityType.Builder.of(EntityBlockSwapper::new, MobCategory.MISC);
    }
    public static final EntityType<EntityBlockSwapper> BLOCK_SWAPPER = register("block_swapper", blockSwapperBuilder().noSummon().sized(1, 1).updateInterval(Integer.MAX_VALUE));
    private static EntityType.Builder<EntityBlockSwapper.EntityBlockSwapperTunneling> blockSwapperTunnelingBuilder() {
        return EntityType.Builder.of(EntityBlockSwapper.EntityBlockSwapperTunneling::new, MobCategory.MISC);
    }
    public static final EntityType<EntityBlockSwapper.EntityBlockSwapperTunneling> BLOCK_SWAPPER_TUNNELING = register("block_swapper_tunneling", blockSwapperTunnelingBuilder().noSummon().sized(1, 1).updateInterval(Integer.MAX_VALUE));
    private static EntityType.Builder<EntityCameraShake> cameraShakeBuilder() {
        return EntityType.Builder.of(EntityCameraShake::new, MobCategory.MISC);
    }
    public static final EntityType<EntityCameraShake> CAMERA_SHAKE = register("camera_shake", cameraShakeBuilder().sized(1, 1).updateInterval(Integer.MAX_VALUE));
    private static EntityType.Builder<EntityFissure> fissureBuilder() {
        return EntityType.Builder.of(EntityFissure::new, MobCategory.MISC);
    }
    public static final EntityType<EntityFissure> FISSURE = register("fissure", fissureBuilder().sized(1f, 1f).updateInterval(1));
    private static EntityType.Builder<EntityFissurePiece> fissurePieceBuilder() {
        return EntityType.Builder.of(EntityFissurePiece::new, MobCategory.MISC);
    }
    public static final EntityType<EntityFissurePiece> FISSURE_PIECE = register("fissure_piece", fissurePieceBuilder().sized(EntityFissurePiece.PIECE_SIZE, 0.1f));
    private static EntityType.Builder<EntityEarthSpike> earthSpikeBuilder() {
        return EntityType.Builder.of(EntityEarthSpike::new, MobCategory.MISC);
    }
    public static final EntityType<EntityEarthSpike> EARTH_SPIKE = register("earth_spike", earthSpikeBuilder().sized(1f, 1f));

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, MMCommon.resource(name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    private static <T> EntityDataSerializer<T> registerSerializer(String name, EntityDataSerializer<T> serializer) {
        FabricEntityDataRegistry.register(MMCommon.resource(name), serializer);
        return serializer;
    }

    public static void register() {
        FabricDefaultAttributeRegistry.register(EntityHandler.FOLIAATH, EntityFoliaath.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.BABY_FOLIAATH, EntityBabyFoliaath.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.WROUGHTNAUT, EntityWroughtnaut.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_RAPTOR, EntityUmvuthanaRaptor.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_MINION, EntityUmvuthana.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_FOLLOWER_TO_PLAYER, EntityUmvuthanaFollowerToPlayer.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_CRANE_TO_PLAYER, EntityUmvuthanaFollowerToPlayer.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_FOLLOWER_TO_RAPTOR, EntityUmvuthana.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHANA_CRANE, EntityUmvuthana.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.UMVUTHI, EntityUmvuthi.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.FROSTMAW, EntityFrostmaw.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.NAGA, EntityNaga.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.LANTERN, EntityLantern.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.GROTTOL, EntityGrottol.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.SCULPTOR, EntitySculptor.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.BLUFF, EntityBluff.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.ELOKOSA_FOLLOWER_TO_HOWLER, EntityElokosa.createAttributes());
        FabricDefaultAttributeRegistry.register(EntityHandler.ELOKOSA_HOWLER, EntityElokosaHowler.createAttributes());
    }
}
