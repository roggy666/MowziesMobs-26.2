package com.bobmowzie.mowziesmobs.server;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.client.particle.ParticleHandler;
import com.bobmowzie.mowziesmobs.client.particle.ParticleVanillaCloudExtended;
import com.bobmowzie.mowziesmobs.client.particle.util.AdvancedParticleBase;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleComponent;
import com.bobmowzie.mowziesmobs.client.particle.util.ParticleRotation;
import com.bobmowzie.mowziesmobs.datagen.MMItemTags;
import com.bobmowzie.mowziesmobs.server.ability.AbilityCommonEventHandler;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.advancement.AdvancementHandler;
import com.bobmowzie.mowziesmobs.server.ai.AvoidEntityIfNotTamedGoal;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.capability.DataHandler;
import com.bobmowzie.mowziesmobs.server.capability.LivingData;
import com.bobmowzie.mowziesmobs.server.capability.PlayerData;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.LeaderSunstrikeImmune;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityBoulderProjectile;
import com.bobmowzie.mowziesmobs.server.entity.effects.geomancy.EntityGeomancyBase;
import com.bobmowzie.mowziesmobs.server.entity.foliaath.EntityFoliaath;
import com.bobmowzie.mowziesmobs.server.entity.frostmaw.EntityFrostmaw;
import com.bobmowzie.mowziesmobs.server.entity.naga.EntityNaga;
import com.bobmowzie.mowziesmobs.server.entity.sculptor.EntitySculptor;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.*;
import com.bobmowzie.mowziesmobs.server.entity.wroughtnaut.EntityWroughtnaut;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemNagaFangDagger;
import com.bobmowzie.mowziesmobs.server.item.ItemSpear;
import com.bobmowzie.mowziesmobs.server.item.ItemUmvuthanaMask;
import com.bobmowzie.mowziesmobs.server.message.MessageFreezeEffect;
import com.bobmowzie.mowziesmobs.server.message.MessageSunblockEffect;
import com.bobmowzie.mowziesmobs.server.message.NetworkHandler;
import com.bobmowzie.mowziesmobs.server.potion.EffectGeomancy;
import com.bobmowzie.mowziesmobs.server.potion.EffectHandler;
import com.bobmowzie.mowziesmobs.server.power.Power;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Common-side game logic hooks. Fabric API callbacks are registered in {@link #register()}; hooks that have no
 * Fabric API equivalent are invoked from the mixins in {@code com.bobmowzie.mowziesmobs.mixin}.
 */
public final class ServerEventHandler {
    private ServerEventHandler() {}

    public static void register() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> onJoinWorld(entity));
        ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> onLivingHurtPost(entity, source, damageTaken));
        ServerLivingEntityEvents.AFTER_DEATH.register(ServerEventHandler::onLivingDeath);
        ServerEntityEvents.EQUIPMENT_CHANGE.register((entity, slot, previous, next) -> onEquipmentChanged(entity, slot));
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> onPlayerRespawn(newPlayer));
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) -> !onBreakBlock(player, state));

        UseItemCallback.EVENT.register((player, level, hand) -> onPlayerRightClickItem(player, hand) ? InteractionResult.FAIL : InteractionResult.PASS);
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> onPlayerRightClickBlock(player, hand, hitResult) ? InteractionResult.FAIL : InteractionResult.PASS);
        UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> onPlayerRightClickEntity(player, hand, entity) ? InteractionResult.FAIL : InteractionResult.PASS);
        AttackBlockCallback.EVENT.register((player, level, hand, pos, direction) -> onPlayerLeftClickBlock(player, pos, direction) ? InteractionResult.FAIL : InteractionResult.PASS);
        AttackEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> onPlayerAttack(player, entity) ? InteractionResult.FAIL : InteractionResult.PASS);

        FuelValueEvents.BUILD.register((builder, context) -> {
            builder.add(BlockHandler.CLAWED_LOG, 300);
            builder.add(BlockHandler.PAINTED_ACACIA, 300);
            builder.add(BlockHandler.PAINTED_ACACIA_SLAB, 150);
            builder.add(BlockHandler.THATCH, 100);
        });
    }

    /** Called for both client and server levels. */
    public static void onJoinWorld(Entity entity) {
        if (entity instanceof Player player) {
            DataHandler.getData(player, DataHandler.PLAYER_DATA).addedToWorld(player);
        }

        if (entity.level().isClientSide()) {
            return;
        }
        if (entity instanceof Zombie && !(entity instanceof ZombifiedPiglin)) {
            ((PathfinderMob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntityFoliaath.class, 0, true, false, null));
            ((PathfinderMob) entity).targetSelector.addGoal(3, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntityUmvuthana.class, 0, true, false, null));
            ((PathfinderMob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntityUmvuthi.class, 0, true, false, null));
        }
        if (entity instanceof AbstractSkeleton) {
            ((PathfinderMob) entity).targetSelector.addGoal(3, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntityUmvuthana.class, 0, true, false, null));
            ((PathfinderMob) entity).targetSelector.addGoal(2, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntityUmvuthi.class, 0, true, false, null));
        }

        if (entity instanceof Parrot) {
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((PathfinderMob) entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
        }
        if (entity instanceof Animal) {
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((PathfinderMob) entity, EntityFoliaath.class, 6.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((PathfinderMob) entity, EntityUmvuthana.class, 6.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((PathfinderMob) entity, EntityUmvuthi.class, 6.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((PathfinderMob) entity, EntityNaga.class, 10.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityIfNotTamedGoal<>((PathfinderMob) entity, EntityFrostmaw.class, 10.0F, 1.0D, 1.2D));
        }
        if (entity instanceof AbstractVillager) {
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((PathfinderMob) entity, EntityUmvuthana.class, 6.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((PathfinderMob) entity, EntityUmvuthi.class, 6.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((PathfinderMob) entity, EntityNaga.class, 10.0F, 1.0D, 1.2D));
            ((PathfinderMob) entity).goalSelector.addGoal(3, new AvoidEntityGoal<>((PathfinderMob) entity, EntityFrostmaw.class, 10.0F, 1.0D, 1.2D));
        }

        if (entity instanceof Pillager) {
            ((PathfinderMob) entity).targetSelector.addGoal(3, new NearestAttackableTargetGoal<>((PathfinderMob) entity, EntitySculptor.class, 0, true, false, null));
        }
    }

    private static final Identifier GEOMANCY_BELT_DEFENSE = Identifier.fromNamespaceAndPath(MMCommon.MODID, "geomancy_belt_defense_boost");
    private static final AttributeModifier DEFENSE_MODIFIER_BELT = new AttributeModifier(GEOMANCY_BELT_DEFENSE, 4D, AttributeModifier.Operation.ADD_VALUE);
    private static final Identifier GEOMANCY_BELT_KNOCKBACK_RESISTANCE = Identifier.fromNamespaceAndPath(MMCommon.MODID, "geomancy_belt_knockback_resistance_boost");
    private static final AttributeModifier KNOCKBACK_MODIFIER_BELT = new AttributeModifier(GEOMANCY_BELT_KNOCKBACK_RESISTANCE, 1D, AttributeModifier.Operation.ADD_VALUE);

    /** Called from {@code LivingEntityMixin} at the end of every living entity tick, both sides. */
    public static void onLivingTick(LivingEntity livingEntity) {
        if (!livingEntity.level().isClientSide()) {
            Item headItemStack = livingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem();
            if (headItemStack instanceof ItemUmvuthanaMask mask) {
                EffectHandler.addOrCombineEffect(livingEntity, mask.getPotion(), 50, 0, true, false);
            }
        }

        if (livingEntity instanceof Mob mob && !(livingEntity instanceof EntityUmvuthanaCrane)) {
            if (mob.getTarget() instanceof EntityUmvuthi && mob.getTarget().hasEffect(EffectHandler.SUNBLOCK)) {
                if (mob.level() instanceof ServerLevel serverLevel) {
                    EntityUmvuthanaCrane sunblocker = serverLevel.getNearestEntity(EntityUmvuthanaCrane.class, TargetingConditions.DEFAULT, mob, mob.getX(), mob.getY() + mob.getEyeHeight(), mob.getZ(), mob.getBoundingBox().inflate(40.0D, 15.0D, 40.0D));
                    mob.setTarget(sunblocker);
                }
            }
        }

        DataHandler.getData(livingEntity, DataHandler.FROZEN_DATA).tick(livingEntity);
        DataHandler.getData(livingEntity, DataHandler.LIVING_DATA).tick(livingEntity);
        DataHandler.getData(livingEntity, DataHandler.ABILITY_DATA).tick(livingEntity);

        // Geomancer Belt mechanics
        AttributeInstance attributeInstanceArmor = livingEntity.getAttribute(Attributes.ARMOR);
        AttributeInstance attributeInstanceKnockbackRes = livingEntity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        if (livingEntity.getItemBySlot(EquipmentSlot.LEGS).is(ItemHandler.GEOMANCER_BELT) && livingEntity.hasEffect(MobEffects.SLOWNESS)) {
            if (attributeInstanceArmor != null && !attributeInstanceArmor.hasModifier(GEOMANCY_BELT_DEFENSE)) {
                attributeInstanceArmor.addTransientModifier(DEFENSE_MODIFIER_BELT);
            }
            if (attributeInstanceKnockbackRes != null && !attributeInstanceKnockbackRes.hasModifier(GEOMANCY_BELT_KNOCKBACK_RESISTANCE)) {
                attributeInstanceKnockbackRes.addTransientModifier(KNOCKBACK_MODIFIER_BELT);
            }
        }
        else {
            if (attributeInstanceArmor != null && attributeInstanceArmor.hasModifier(GEOMANCY_BELT_DEFENSE)) {
                attributeInstanceArmor.removeModifier(DEFENSE_MODIFIER_BELT);
            }
            if (attributeInstanceKnockbackRes != null && attributeInstanceKnockbackRes.hasModifier(GEOMANCY_BELT_KNOCKBACK_RESISTANCE)) {
                attributeInstanceKnockbackRes.removeModifier(KNOCKBACK_MODIFIER_BELT);
            }
        }

        if (livingEntity instanceof Player player) {
            onPlayerTick(player);
            if (player.level().isClientSide()) {
                MMCommon.PROXY.onPlayerTick(player);
            }
        }
    }

    /** Returns false if the effect must not be applied. Called from {@code LivingEntityMixin#canBeAffected}. */
    public static boolean onPotionEffectApplicable(LivingEntity entity, MobEffectInstance effectInstance) {
        if (entity.hasEffect(EffectHandler.ECLIPSED) && !entity.getEffect(EffectHandler.ECLIPSED).endsWithin(1)) {
            return false;
        }

        if (entity.hasEffect(EffectHandler.POISON_RESIST) && effectInstance.is(MobEffects.POISON)) {
            return false;
        }
        return true;
    }

    /** Called from {@code LivingEntityMixin#onEffectAdded}, both sides. */
    public static void onAddPotionEffect(LivingEntity entity, MobEffectInstance effectInstance) {
        if (effectInstance == null) {
            return;
        }

        if (effectInstance.is(EffectHandler.POISON_RESIST)) {
            entity.removeEffect(MobEffects.POISON);
        }

        if (effectInstance.is(EffectHandler.ECLIPSED)) {
            for (MobEffectInstance other : entity.getActiveEffects()) {
                if (!other.is(EffectHandler.ECLIPSED)) {
                    LivingData livingData = DataHandler.getData(entity, DataHandler.LIVING_DATA);
                    livingData.eclipseEffect(other);
                }
            }
            entity.removeAllEffects();
        }

        if (effectInstance.getEffect() == EffectHandler.SUNBLOCK) {
            if (!entity.level().isClientSide()) {
                NetworkHandler.sendToPlayersTrackingEntityAndSelf(entity, new MessageSunblockEffect(entity.getId(), true));
            }

            MMCommon.PROXY.playSunblockSound(entity);
        }

        if (effectInstance.getEffect() == EffectHandler.FROZEN) {
            if (!entity.level().isClientSide()) {
                DataHandler.getData(entity, DataHandler.FROZEN_DATA).onFreeze(entity);
                NetworkHandler.sendToPlayersTrackingEntityAndSelf(entity, new MessageFreezeEffect(entity.getId(), true));
            }
        }
    }

    /** Called from {@code LivingEntityMixin#onEffectsRemoved} for both manual removal and expiry, both sides. */
    public static void onRemovePotionEffect(LivingEntity entity, MobEffectInstance effectInstance) {
        if (effectInstance == null) {
            return;
        }

        if (effectInstance.is(EffectHandler.ECLIPSED)) {
            LivingData livingData = DataHandler.getData(entity, DataHandler.LIVING_DATA);
            livingData.unEclipseEffects(entity);
        }

        if (!entity.level().isClientSide() && effectInstance.getEffect() == EffectHandler.SUNBLOCK) {
            NetworkHandler.sendToPlayersTrackingEntityAndSelf(entity, new MessageSunblockEffect(entity.getId(), false));
        }

        if (!entity.level().isClientSide() && effectInstance.getEffect() == EffectHandler.FROZEN) {
            DataHandler.getData(entity, DataHandler.FROZEN_DATA).onUnfreeze(entity);
            NetworkHandler.sendToPlayersTrackingEntityAndSelf(entity, new MessageFreezeEffect(entity.getId(), false));
        }
    }

    /**
     * Called from {@code LivingEntityMixin#actuallyHurt} after armor/magic reductions but before health is modified.
     * Returns the modified damage.
     */
    public static float onLivingHurtPre(LivingEntity livingEntity, DamageSource source, float damage) {
        // Copied from LivingEntity's applyPotionDamageCalculations
        // SUNBLOCK
        if (!source.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
            if (livingEntity.hasEffect(EffectHandler.SUNBLOCK) && !source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
                int i = (livingEntity.getEffect(EffectHandler.SUNBLOCK).getAmplifier() + 2) * 5;
                int j = 25 - i;
                float f = damage * (float)j;
                float f1 = damage;
                damage = Math.max(f / 25.0F, 0.0F);
                float f2 = f1 - damage;
                if (f2 > 0.0F && f2 < 3.4028235E37F) {
                    if (livingEntity instanceof ServerPlayer) {
                        ((ServerPlayer)livingEntity).awardStat(Stats.DAMAGE_RESISTED, Math.round(f2 * 10.0F));
                    } else if (source.getEntity() instanceof ServerPlayer) {
                        ((ServerPlayer)source.getEntity()).awardStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(f2 * 10.0F));
                    }
                }
            }
        }

        // FRAGILITY
        if (livingEntity.hasEffect(EffectHandler.FRAGILITY) && !source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
            int i = (livingEntity.getEffect(EffectHandler.FRAGILITY).getAmplifier() + 1) * 5;
            int j = 25 + i;
            float f = damage * (float)j;
            damage = Math.max(f / 25.0F, 0.0F);
        }
        return damage;
    }

    /** Server side, after damage has been applied. {@code damage} is the amount that actually landed on health. */
    public static void onLivingHurtPost(LivingEntity entity, DamageSource source, float damage) {
        if (source.is(DamageTypeTags.IS_FIRE)) {
            entity.removeEffectNoUpdate(EffectHandler.FROZEN);
            DataHandler.getData(entity, DataHandler.FROZEN_DATA).onUnfreeze(entity);
            NetworkHandler.sendToPlayersTrackingEntityAndSelf(entity, new MessageFreezeEffect(entity.getId(), false));
        }
        if (entity instanceof Player player) {
            Power[] powers = DataHandler.getData(player, DataHandler.PLAYER_DATA).getPowers();
            for (Power power : powers) {
                power.onTakeDamage(player, source, damage);
            }

            if (player.getItemBySlot(EquipmentSlot.CHEST).is(ItemHandler.GEOMANCER_ROBE)) {
                spawnBoulderNearPlayer(player);
            }
        }

        DataHandler.getData(entity, DataHandler.LIVING_DATA).setLastDamage(damage);

        onLivingDamage(entity, source, damage);
        AbilityCommonEventHandler.onTakeDamage(entity, source, damage);
    }

    private static void onPlayerTick(Player player) {
        PlayerData data = DataHandler.getData(player, DataHandler.PLAYER_DATA);
        data.tick(player);
        Power[] powers = data.getPowers();
        for (Power power : powers) {
            power.tick(player);
        }
    }

    /** Called from {@code LivingEntityMixin#causeFallDamage}. Returns the modified damage multiplier. */
    public static float onLivingFall(LivingEntity entity, double distance, float damageMultiplier) {
        if (entity.getItemBySlot(EquipmentSlot.FEET).is(ItemHandler.GEOMANCER_SANDALS)) {
            if (distance > 4) {
                EffectHandler.addOrCombineEffect(entity, MobEffects.SPEED, 60, 0, false, false);
            }
        }
        return AbilityCommonEventHandler.onFall(entity, distance, damageMultiplier);
    }

    /** Returns true if the item use must be cancelled. Called from {@code LivingEntityMixin#startUsingItem}. */
    public static boolean onUseItem(LivingEntity living, ItemStack stack) {
        if (living.hasEffect(EffectHandler.FROZEN)) {
            return true;
        }

        return DataHandler.getData(living, DataHandler.ABILITY_DATA).itemUsePrevented(stack);
    }

    /** Returns true if the block placement must be cancelled. Called from {@code BlockItemMixin}. */
    public static boolean onPlaceBlock(Entity entity, BlockState block) {
        if (entity instanceof LivingEntity living) {
            if (living.hasEffect(EffectHandler.FROZEN)) {
                return true;
            }

            if (DataHandler.getData(living, DataHandler.ABILITY_DATA).blockBreakingBuildingPrevented()) {
                return true;
            }

            if (entity instanceof Player) {
                cheatSculptor((Player) entity);

                if (
                        block.getBlock() == Blocks.FIRE ||
                        block.getBlock() == Blocks.TNT ||
                        block.getBlock() == Blocks.RESPAWN_ANCHOR ||
                        block.getBlock() == Blocks.DISPENSER ||
                        block.getBlock() == Blocks.CACTUS
                ) {
                    aggroUmvuthana((Player) entity);
                }
            }
        }
        return false;
    }

    private static void onLivingDeath(LivingEntity entity, DamageSource source) {
        if (entity instanceof Player player) {
            PlayerData data = DataHandler.getData(player, DataHandler.PLAYER_DATA);
            EntitySculptor sculptor = data.getTestingSculptor();

            if (sculptor != null && sculptor.getTestingPlayer() == player && source == player.damageSources().fall()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    AdvancementHandler.SCULPTOR_FAILURE_TRIGGER.trigger(serverPlayer);
                }
            }
        }
    }

    /** Returns true if the block break must be cancelled. */
    private static boolean onBreakBlock(Player player, BlockState block) {
        if (player.hasEffect(EffectHandler.FROZEN)) {
            return true;
        }

        if (DataHandler.getData(player, DataHandler.ABILITY_DATA).blockBreakingBuildingPrevented()) {
            return true;
        }

        cheatSculptor(player);

        if (block.getBlock() == Blocks.GOLD_BLOCK ||
            block.is(BlockTags.PLANKS) ||
            block.is(BlockTags.LOGS) ||
            block.is(BlockTags.LEAVES) ||
            block.getBlock() == Blocks.DYED_TERRACOTTA.lightGray() ||
            block.getBlock() == Blocks.DYED_TERRACOTTA.red() ||
            block.getBlock() == Blocks.SMOOTH_RED_SANDSTONE_SLAB ||
            block.getBlock() == Blocks.SMOOTH_RED_SANDSTONE ||
            block.getBlock() == Blocks.SMOOTH_RED_SANDSTONE_STAIRS ||
            block.getBlock() == Blocks.CAMPFIRE ||
            block.getBlock() == Blocks.IRON_BARS ||
            block.getBlock() == Blocks.SKELETON_SKULL ||
            block.getBlock() == Blocks.TORCH ||
            block.getBlock() == Blocks.CHEST
        ) {
            aggroUmvuthana(player);
        }
        return false;
    }

    public static <T extends Entity> List<T> getEntitiesNearby(Entity startEntity, Class<T> entityClass, double r) {
        return startEntity.level().getEntitiesOfClass(entityClass, startEntity.getBoundingBox().inflate(r, r, r), e -> e != startEntity && startEntity.distanceTo(e) <= r);
    }

    private static List<LivingEntity> getEntityBaseNearby(LivingEntity user, double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> list = user.level().getEntities(user, user.getBoundingBox().inflate(distanceX, distanceY, distanceZ));
        return list.stream().filter(entityNeighbor -> entityNeighbor instanceof LivingEntity && user.distanceTo(entityNeighbor) <= radius).map(entityNeighbor -> (LivingEntity) entityNeighbor).collect(Collectors.toCollection(ArrayList::new));
    }

    /** Client side only, right click with an empty hand into the air. Called from {@code MinecraftMixin}. */
    public static void onPlayerRightClickEmpty(Player player, InteractionHand hand) {
        if (player.hasEffect(EffectHandler.FROZEN)) {
            return;
        }

        if (DataHandler.getData(player, DataHandler.ABILITY_DATA).interactingPrevented()) {
            return;
        }

        if (player.level().isClientSide() && player.getInventory().getSelectedItem().isEmpty() && player.hasEffect(EffectHandler.SUNS_BLESSING)) {
            if (player.isShiftKeyDown()) {
                AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(player, AbilityHandler.SOLAR_BEAM_ABILITY);
            } else {
                AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(player, AbilityHandler.SUNSTRIKE_ABILITY);
            }
        }

        Power[] powers = DataHandler.getData(player, DataHandler.PLAYER_DATA).getPowers();
        for (Power power : powers) {
            power.onRightClickEmpty(player, hand);
        }

        AbilityCommonEventHandler.onPlayerRightClickEmpty(player, hand);
    }

    /** Returns true if the interaction must be cancelled. */
    private static boolean onPlayerRightClickEntity(Player player, InteractionHand hand, Entity target) {
        if (player.hasEffect(EffectHandler.FROZEN)) {
            return true;
        }

        if (DataHandler.getData(player, DataHandler.ABILITY_DATA).interactingPrevented()) {
            return true;
        }

        Power[] powers = DataHandler.getData(player, DataHandler.PLAYER_DATA).getPowers();
        for (Power power : powers) {
            power.onRightClickEntity(player, hand, target);
        }

        AbilityCommonEventHandler.onPlayerRightClickEntity(player, hand, target);
        return false;
    }

    /** Returns true if the interaction must be cancelled. */
    private static boolean onPlayerRightClickBlock(Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.hasEffect(EffectHandler.FROZEN)) {
            return true;
        }

        if (DataHandler.getData(player, DataHandler.ABILITY_DATA).interactingPrevented()) {
            return true;
        }

        BlockPos pos = hitResult.getBlockPos();
        if (player.level().getBlockState(pos).getBlock() instanceof ChestBlock) {
            aggroUmvuthana(player);
        }

        ItemStack item = player.getItemInHand(hand);

        if (item.getItem() == Items.FLINT_AND_STEEL || item.getItem() == Items.TNT_MINECART) {
            aggroUmvuthana(player);
        }

        if (player.level().isClientSide() && player.getInventory().getSelectedItem().isEmpty() && player.hasEffect(EffectHandler.SUNS_BLESSING) && player.level().getBlockState(pos).getMenuProvider(player.level(), pos) == null) {
            if (player.isShiftKeyDown()) {
                AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(player, AbilityHandler.SOLAR_BEAM_ABILITY);
            } else {
                AbilityHandler.INSTANCE.sendPlayerTryAbilityMessage(player, AbilityHandler.SUNSTRIKE_ABILITY);
            }
        }
        if (player.getMainHandItem().is(ItemHandler.WROUGHT_AXE) && player.level().getBlockState(pos).getMenuProvider(player.level(), pos) != null) {
            player.resetAttackStrengthTicker();
            return false;
        }

        Power[] powers = DataHandler.getData(player, DataHandler.PLAYER_DATA).getPowers();
        for (Power power : powers) {
            power.onRightClickBlock(player, hand, hitResult);
        }

        AbilityCommonEventHandler.onPlayerRightClickBlock(player, hand, hitResult);
        return false;
    }

    /** Client side only, left click into the air. Called from {@code MinecraftMixin}. */
    public static void onPlayerLeftClickEmpty(Player player) {
        Power[] powers = DataHandler.getData(player, DataHandler.PLAYER_DATA).getPowers();
        for (Power power : powers) {
            power.onLeftClickEmpty(player);
        }

        AbilityCommonEventHandler.onPlayerLeftClickEmpty(player);
    }

    private static void onLivingDamage(LivingEntity entity, DamageSource source, float damage) {
        if (entity.getHealth() <= damage && entity.hasEffect(EffectHandler.FROZEN)) {
            entity.removeEffectNoUpdate(EffectHandler.FROZEN);
            DataHandler.getData(entity, DataHandler.FROZEN_DATA).onUnfreeze(entity);
            NetworkHandler.sendToPlayersTrackingEntityAndSelf(entity, new MessageFreezeEffect(entity.getId(), false));
        }

        if (damage > 0 && source.getEntity() instanceof Player player) {
            if (player.getItemBySlot(EquipmentSlot.CHEST).is(ItemHandler.GEOMANCER_ROBE)) {
                spawnBoulderNearPlayer(player);
            }
        }

        if (entity instanceof Player player && source == player.damageSources().fall() && player.getHealth() <= damage) {
            PlayerData data = DataHandler.getData(player, DataHandler.PLAYER_DATA);
            if (data.getTestingSculptor() != null) {
                EntitySculptor sculptor = data.getTestingSculptor();
                if (sculptor.getTestingPlayer() == player) {
                    if (player instanceof ServerPlayer) {
                        AdvancementHandler.SCULPTOR_FAILURE_TRIGGER.trigger((ServerPlayer) player);
                    }
                }
            }
        }
    }

    /** Returns true if the item use must be cancelled. */
    private static boolean onPlayerRightClickItem(Player player, InteractionHand hand) {
        Item item = player.getItemInHand(hand).getItem();

        if (item == Items.LAVA_BUCKET) {
            aggroUmvuthana(player);
        }

        if (item == Items.WATER_BUCKET) {
            cheatSculptor(player);
        }

        if (player.hasEffect(EffectHandler.FROZEN)) {
            return true;
        }

        if (DataHandler.getData(player, DataHandler.ABILITY_DATA).itemUsePrevented(player.getItemInHand(hand))) {
            return true;
        }

        Power[] powers = DataHandler.getData(player, DataHandler.PLAYER_DATA).getPowers();
        for (Power power : powers) {
            power.onRightClickWithItem(player, hand);
        }

        AbilityCommonEventHandler.onPlayerRightClickItem(player, hand);
        return false;
    }

    /** Returns true if the block attack must be cancelled. */
    private static boolean onPlayerLeftClickBlock(Player player, BlockPos pos, Direction direction) {
        if (player.hasEffect(EffectHandler.FROZEN)) {
            return true;
        }

        if (DataHandler.getData(player, DataHandler.ABILITY_DATA).blockBreakingBuildingPrevented()) {
            return true;
        }

        Power[] powers = DataHandler.getData(player, DataHandler.PLAYER_DATA).getPowers();
        for (Power power : powers) {
            power.onLeftClickBlock(player, pos, direction);
        }

        AbilityCommonEventHandler.onPlayerLeftClickBlock(player, pos, direction);
        return false;
    }

    /** Called from {@code LivingEntityMixin#jumpFromGround}, both sides. */
    public static void onLivingJump(LivingEntity entity) {
        if (entity.hasEffect(EffectHandler.FROZEN) && entity.onGround()) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(1, 0, 1));
        }

        if (entity instanceof Player player) {
            Power[] powers = DataHandler.getData(player, DataHandler.PLAYER_DATA).getPowers();

            for (Power power : powers) {
                power.onJump(player);
            }
        }

        AbilityCommonEventHandler.onJump(entity);
    }

    /** Returns true if the attack must be cancelled. */
    private static boolean onPlayerAttack(Player player, Entity target) {
        if (player.hasEffect(EffectHandler.FROZEN)) {
            return true;
        }

        if (DataHandler.getData(player, DataHandler.ABILITY_DATA).attackingPrevented()) {
            return true;
        }

        PlayerData data = DataHandler.getData(player, DataHandler.PLAYER_DATA);
        data.setPrevCooledAttackStrength(player.getAttackStrengthScale(0.5f));

        Power[] powers = data.getPowers();
        for (Power power : powers) {
            power.onLeftClickEntity(player, target);
        }

        AbilityCommonEventHandler.onLeftClickEntity(player, target);

        if (target instanceof ItemFrame itemFrame) {
            if (itemFrame.getItem().getItem() instanceof ItemUmvuthanaMask) {
                aggroUmvuthana(player);
            }
        }
        if (target instanceof LeaderSunstrikeImmune) {
            aggroUmvuthana(player);
        }

        if (!(target instanceof LivingEntity)) return false;
        if (target instanceof EntityUmvuthanaFollowerToPlayer) return false;
        if (!player.level().isClientSide()) {
            for (int i = 0; i < data.getPackSize(); i++) {
                EntityUmvuthanaFollowerToPlayer umvuthana = data.getUmvuthanaPack().get(i);
                LivingEntity living = (LivingEntity) target;
                if (umvuthana.getMaskType() != MaskType.FAITH) {
                    if (!living.isInvulnerable()) umvuthana.setTarget(living);
                }
            }
        }
        return false;
    }

    /** Result of {@link #onCriticalHit}: whether the hit is critical and the damage multiplier to apply. */
    public record CriticalHit(boolean critical, float damageMultiplier) {}

    /** Called from {@code PlayerMixin#attack} once vanilla has decided whether the hit is critical. */
    public static CriticalHit onCriticalHit(Player attacker, Entity target, float damageMultiplier, boolean isCriticalHit) {
        ItemStack weapon = attacker.getMainHandItem();

        if (DataHandler.getData(attacker, DataHandler.PLAYER_DATA).getPrevCooledAttackStrength() == 1 && !weapon.isEmpty() && target instanceof LivingEntity livingTarget) {
            if (weapon.getItem() instanceof ItemNagaFangDagger) {
                Vec3 lookDir = new Vec3(livingTarget.getLookAngle().x, 0, livingTarget.getLookAngle().z).normalize();
                Vec3 vecBetween = new Vec3(livingTarget.getX() - attacker.getX(), 0, livingTarget.getZ() - attacker.getZ()).normalize();
                double dot = lookDir.dot(vecBetween);
                if (dot > 0.7) {
                    isCriticalHit = true;
                    damageMultiplier = damageMultiplier + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.backstabDamageMultiplier.get().floatValue();
                    livingTarget.playSound(MMSounds.ENTITY_NAGA_ACID_HIT, 1f, 1.2f);
                    AbilityHandler.INSTANCE.sendAbilityMessage(attacker, AbilityHandler.BACKSTAB_ABILITY);

                    if (livingTarget.level().isClientSide()) {
                        Vec3 ringOffset = attacker.getLookAngle().scale(-livingTarget.getBbWidth() / 2.f);
                        ParticleRotation.OrientVector rotation = new ParticleRotation.OrientVector(ringOffset);
                        Vec3 pos = livingTarget.position().add(0, livingTarget.getBbHeight() / 2f, 0).add(ringOffset);
                        AdvancedParticleBase.spawnParticle(livingTarget.level(), ParticleHandler.RING_SPARKS, pos.x(), pos.y(), pos.z(), 0, 0, 0, rotation, 3.5F, 0.83f, 1, 0.39f, 1, 1, 6, false, true, new ParticleComponent[]{
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, new ParticleComponent.KeyTrack(new float[]{1f, 1f, 0f}, new float[]{0f, 0.5f, 1f}), false),
                                new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, ParticleComponent.KeyTrack.startAndEnd(0f, 15f), false)
                        });
                        RandomSource rand = attacker.level().getRandom();
                        float explodeSpeed = 2.5f;
                        for (int i = 0; i < 10; i++) {
                            Vec3 particlePos = new Vec3(rand.nextFloat() * 0.25, 0, 0);
                            particlePos = particlePos.yRot((float) (rand.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.xRot((float) (rand.nextFloat() * 2 * Math.PI));
                            double value = rand.nextFloat() * 0.1f;
                            double life = rand.nextFloat() * 8f + 15f;
                            ParticleVanillaCloudExtended.spawnVanillaCloud(livingTarget.level(), pos.x(), pos.y(), pos.z(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, 1, 0.25d + value, 0.75d + value, 0.25d + value, 0.6, life);
                        }
                        for (int i = 0; i < 10; i++) {
                            Vec3 particlePos = new Vec3(rand.nextFloat() * 0.25, 0, 0);
                            particlePos = particlePos.yRot((float) (rand.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.xRot((float) (rand.nextFloat() * 2 * Math.PI));
                            double value = rand.nextFloat() * 0.1f;
                            double life = rand.nextFloat() * 2.5f + 5f;
                            AdvancedParticleBase.spawnParticle(livingTarget.level(), ParticleHandler.PIXEL, pos.x(), pos.y(), pos.z(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.07d + value, 0.25d + value, 0.07d + value, 1d, 0.6, life * 0.95, false, true);
                        }
                        for (int i = 0; i < 6; i++) {
                            Vec3 particlePos = new Vec3(rand.nextFloat() * 0.25, 0, 0);
                            particlePos = particlePos.yRot((float) (rand.nextFloat() * 2 * Math.PI));
                            particlePos = particlePos.xRot((float) (rand.nextFloat() * 2 * Math.PI));
                            double value = rand.nextFloat() * 0.1f;
                            double life = rand.nextFloat() * 5f + 10f;
                            AdvancedParticleBase.spawnParticle(livingTarget.level(), ParticleHandler.BUBBLE, pos.x(), pos.y(), pos.z(), particlePos.x * explodeSpeed, particlePos.y * explodeSpeed, particlePos.z * explodeSpeed, true, 0, 0, 0, 0, 3f, 0.25d + value, 0.75d + value, 0.25d + value, 1d, 0.6, life * 0.95, false, true);
                        }
                    }
                }
            }
            else if (weapon.getItem() instanceof ItemSpear) {
                if (livingTarget instanceof Animal && livingTarget.getMaxHealth() <= 30 && attacker.level().getRandom().nextFloat() <= 0.334) {
                    isCriticalHit = true;
                    damageMultiplier = 400;
                }
            }
        }
        return new CriticalHit(isCriticalHit, damageMultiplier);
    }

    private static final AttributeModifier ATTACK_MODIFIER_BEADS = new AttributeModifier(Identifier.fromNamespaceAndPath(MMCommon.MODID, "geomancy_beads_attack_boost"), 3D, AttributeModifier.Operation.ADD_VALUE);

    private static void onEquipmentChanged(LivingEntity equipper, EquipmentSlot slot) {
        ItemStack weapon = equipper.getItemBySlot(EquipmentSlot.MAINHAND);
        AttributeInstance attributeinstance = equipper.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attributeinstance != null) {
            // If the head or hand slot was modified
            if ((slot == EquipmentSlot.HEAD || slot == EquipmentSlot.MAINHAND)) {
                // Start by clearing attack boost
                attributeinstance.removeModifier(ATTACK_MODIFIER_BEADS);
                // If wearing beads and unarmed
                if (equipper.getItemBySlot(EquipmentSlot.HEAD).is(ItemHandler.GEOMANCER_BEADS) && (weapon.is(MMItemTags.HAND_WEAPONS) || weapon.isEmpty())) {
                    // Apply or reapply attack boost
                    attributeinstance.addTransientModifier(ATTACK_MODIFIER_BEADS);
                }
            }
        }
    }

    /** Returns true if mounting must be cancelled. Called from {@code EntityMixin#startRiding}. */
    public static boolean onRideEntity(Entity entityMounting, Entity entityBeingMounted) {
        return entityMounting instanceof EntityUmvuthi ||
                entityMounting instanceof EntityFrostmaw ||
                entityMounting instanceof EntityWroughtnaut ||
                entityMounting instanceof EntitySculptor;
    }

    private static void onPlayerRespawn(ServerPlayer player) {
        List<MowzieEntity> mobs = getEntitiesNearby(player, MowzieEntity.class, 40);
        for (MowzieEntity mob : mobs) {
            if (mob.resetHealthOnPlayerRespawn()) {
                mob.setHealth(mob.getMaxHealth());
            }
        }
    }

    /** Returns false if the spawn must be denied. Called from {@code SpawnPlacementsMixin}. */
    public static boolean onSpawnPlacementCheck(EntityType<?> entityType, ServerLevelAccessor level, BlockPos pos) {
        StructureManager structureManager = level.getLevel().structureManager();
        Structure structure = structureManager.registryAccess().lookupOrThrow(Registries.STRUCTURE).getValue(MMCommon.resource("monastery"));
        if (entityType.getCategory() == MobCategory.MONSTER && structure != null && structureManager.getStructureAt(pos, structure).isValid()) {
            BlockState ground = level.getBlockState(pos.below());
            if (
                    level.canSeeSky(pos) &&
                    (ground.is(Blocks.DARK_OAK_PLANKS) ||
                    ground.is(Blocks.DARK_OAK_SLAB) ||
                    ground.is(Blocks.DARK_OAK_STAIRS))
            ) {
                return false;
            }
        }
        return true;
    }

    private static void aggroUmvuthana(Player player) {
        List<EntityUmvuthi> barakos = getEntitiesNearby(player, EntityUmvuthi.class, 50);
        for (EntityUmvuthi barako : barakos) {
            if (barako.getTarget() == null || !(barako.getTarget() instanceof Player)) {
                if (!player.isCreative() && !player.isSpectator() && player.blockPosition().distSqr(barako.getHomePosition()) < 900) {
                    if (barako.canAttack(player)) barako.setMisbehavedPlayerId(player.getUUID());
                }
            }
        }
        List<EntityUmvuthanaMinion> barakoas = getEntitiesNearby(player, EntityUmvuthanaMinion.class, 50);
        for (EntityUmvuthanaMinion barakoa : barakoas) {
            if (barakoa.getTarget() == null || !(barakoa.getTarget() instanceof Player)) {
                if (player.blockPosition().distSqr(barakoa.getHomePosition()) < 900) {
                    if (barakoa.canAttack(player)) barakoa.setMisbehavedPlayerId(player.getUUID());
                }
            }
        }
    }

    private static void cheatSculptor(Player player) {
        List<EntitySculptor> sculptors = player.level().getEntitiesOfClass(EntitySculptor.class, player.getBoundingBox().inflate(EntitySculptor.TEST_RADIUS + 3, EntitySculptor.TEST_HEIGHT, EntitySculptor.TEST_RADIUS + 3), s -> s.isTesting() && s.getActiveAbilityType() != EntitySculptor.PASS_TEST && s.getActiveAbilityType() != EntitySculptor.FAIL_TEST);
        for (EntitySculptor sculptor : sculptors) {
            sculptor.playerCheated();
        }
    }

    private static void spawnBoulderNearPlayer(Player player) {
        if (player.getRandom().nextFloat() > 0.5) return;
        int i = Mth.floor(player.getX());
        int j = Mth.floor(player.getY());
        int k = Mth.floor(player.getZ());
        for(int l = 0; l < 10; ++l) {
            double radius = Math.pow(player.getRandom().nextFloat(), 0.5) * 10 + 3;
            double angle = player.getRandom().nextFloat() * Math.PI * 2;
            int i1 = i + (int)(Math.cos(angle) * radius);
            int j1 = j + Mth.nextInt(player.getRandom(), 0, 15) * Mth.nextInt(player.getRandom(), -1, 1);
            int k1 = k + (int)(Math.sin(angle) * radius);
            BlockPos spawnBoulderPos = new BlockPos(i1, j1, k1);
            BlockState state = player.level().getBlockState(spawnBoulderPos);
            int searchDist = 0;
            int maxSearchDist = 10;
            // march down to solid ground
            while (state.canBeReplaced() && searchDist < maxSearchDist) {
                spawnBoulderPos = spawnBoulderPos.below();
                state = player.level().getBlockState(spawnBoulderPos);
                searchDist++;
            }
            // march up to air ground
            searchDist = 0;
            while (!state.canBeReplaced() && searchDist < maxSearchDist) {
                spawnBoulderPos = spawnBoulderPos.above();
                state = player.level().getBlockState(spawnBoulderPos);
                searchDist++;
            }
            spawnBoulderPos = spawnBoulderPos.below();
            state = player.level().getBlockState(spawnBoulderPos);

            if (EffectGeomancy.isBlockUseable(state)) {
                EntityBoulderProjectile boulder = new EntityBoulderProjectile(EntityHandler.BOULDER_PROJECTILE, player.level(), player, state, spawnBoulderPos, EntityGeomancyBase.GeomancyTier.SMALL);
                boulder.setPos(spawnBoulderPos.getX() + 0.5F, spawnBoulderPos.getY() + 2, spawnBoulderPos.getZ() + 0.5F);
                if (!player.level().isClientSide() && boulder.checkCanSpawn()) {
                    player.level().addFreshEntity(boulder);
                    break;
                }
            }
        }
    }
}
