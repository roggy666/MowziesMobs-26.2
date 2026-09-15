package com.bobmowzie.mowziesmobs.server.entity.foliaath;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import com.bobmowzie.mowziesmobs.client.model.tools.ControlledAnimation;
import com.bobmowzie.mowziesmobs.server.ai.animation.AnimationBabyFoliaathEatAI;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.MowzieEntity;
import com.bobmowzie.mowziesmobs.server.entity.MowzieLLibraryEntity;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EntityBabyFoliaath extends MowzieLLibraryEntity {
    private static final EntityDataAccessor<Integer> GROWTH = SynchedEntityData.defineId(EntityBabyFoliaath.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> INFANT = SynchedEntityData.defineId(EntityBabyFoliaath.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> HUNGRY = SynchedEntityData.defineId(EntityBabyFoliaath.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<ItemStack> EATING = SynchedEntityData.defineId(EntityBabyFoliaath.class, EntityDataSerializers.ITEM_STACK);

    public static final Animation EAT_ANIMATION = Animation.create(20);
    public ControlledAnimation activate = new ControlledAnimation(5);
    private double prevActivate;

    public EntityBabyFoliaath(EntityType<? extends EntityBabyFoliaath> type, Level world) {
        super(type, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AnimationBabyFoliaathEatAI<EntityBabyFoliaath>(this, EAT_ANIMATION));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return MowzieEntity.createAttributes().add(Attributes.MAX_HEALTH, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }

    protected boolean isMovementNoisy() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public void push(double x, double y, double z) {
        super.push(0, y, 0);
    }

    @Override
    protected void pushEntities() {

    }

    @Override
    public void tick() {
        super.tick();
        setDeltaMovement(0, getDeltaMovement().y, 0);
        yBodyRot = 0;

        if (arePlayersCarryingMeat(getPlayersNearby(3, 3, 3, 3)) && getAnimation() == NO_ANIMATION) {
            activate.increaseTimer();
        } else {
            activate.decreaseTimer();
        }

        if (activate.getTimer() == 1 && prevActivate - activate.getTimer() < 0) {
            playSound(MMSounds.ENTITY_FOLIAATH_GRUNT, 0.5F, 1.5F);
        }
        prevActivate = activate.getTimer();

        if (!level().isClientSide() && getAnimation() == NO_ANIMATION) {
            for (ItemEntity meat : getMeatsNearby(1.0, 0.5, 1.0, 1.5)) {
                ItemStack stack = meat.getItem().split(1);
                if (!stack.isEmpty()) {
                    setEating(stack);
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, EAT_ANIMATION);
                    playSound(MMSounds.ENTITY_FOLIAATH_BABY_EAT, 0.5F, 1.2F);
                    setGrowth(getGrowth() + 60);
                    setHungry(false);
                    break;
                }
            }
        }
        if (level().isClientSide() && getAnimation() == EAT_ANIMATION && (getAnimationTick() == 3 || getAnimationTick() == 7 || getAnimationTick() == 11 || getAnimationTick() == 15 || getAnimationTick() == 19)) {
            for (int i = 0; i <= 5; i++) {
                level().addParticle(new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(getEating())), getX(), getY() + 0.2, getZ(), random.nextFloat() * 0.2 - 0.1, random.nextFloat() * 0.2, random.nextFloat() * 0.2 - 0.1);
            }
        }

        //Growing
        if (!level().isClientSide()) {
            if (tickCount % 20 == 0 && !getHungry()) {
                incrementGrowth();
            }
            setInfant(getGrowth() < 600);
            if (getGrowth() == 600 || getGrowth() == 1200 || getGrowth() == 1800) {
                setHungry(true);
            }
            if (getGrowth() >= 2400) {
                EntityFoliaath adultFoliaath = new EntityFoliaath(EntityHandler.FOLIAATH, level());
                adultFoliaath.setPos(getX(), getY(), getZ());
                adultFoliaath.setCanDespawn(false);
                level().addFreshEntity(adultFoliaath);
                discard();
            }
        }
    }

    @Override
    public Animation getDeathAnimation() {
        return null;
    }

    @Override
    public Animation getHurtAnimation() {
        return null;
    }

    public static boolean isMeat(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (stack.is(ItemTags.MEAT) || stack.is(ConventionalItemTags.RAW_MEAT_FOODS) || stack.is(ConventionalItemTags.COOKED_MEAT_FOODS)) {
            return true;
        }
        if (stack.is(Items.PORKCHOP) || stack.is(Items.COOKED_PORKCHOP)
                || stack.is(Items.BEEF) || stack.is(Items.COOKED_BEEF)
                || stack.is(Items.CHICKEN) || stack.is(Items.COOKED_CHICKEN)
                || stack.is(Items.MUTTON) || stack.is(Items.COOKED_MUTTON)
                || stack.is(Items.RABBIT) || stack.is(Items.COOKED_RABBIT)
                || stack.is(Items.ROTTEN_FLESH)) {
            return true;
        }
        return stack.has(net.minecraft.core.component.DataComponents.FOOD);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (isMeat(stack)) {
            if (getAnimation() == NO_ANIMATION) {
                if (!level().isClientSide()) {
                    ItemStack eatingStack = stack.consumeAndReturn(1, player);
                    setEating(eatingStack);
                    AnimationHandler.INSTANCE.sendAnimationMessage(this, EAT_ANIMATION);
                    playSound(MMSounds.ENTITY_FOLIAATH_BABY_EAT, 0.5F, 1.2F);
                    setGrowth(getGrowth() + 60);
                    setHungry(false);
                }
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        }
        return super.mobInteract(player, hand);
    }

    private boolean arePlayersCarryingMeat(List<Player> players) {
        if (!players.isEmpty()) {
            for (Player player : players) {
                if (isMeat(player.getMainHandItem()) || isMeat(player.getOffhandItem())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        for (int i = 0; i < 10; i++) {
            level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.JUNGLE_LEAVES.defaultBlockState()), getX(), getY() + 0.2, getZ(), 0, 0, 0);
        }
        discard() ;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void push(Entity collider) {
        setDeltaMovement(0, getDeltaMovement().y, 0);
    }

    @Override
    protected SoundEvent getDeathSound() {
        playSound(SoundEvents.GRASS_BREAK, 1, 0.8F);
        return null;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor world, EntitySpawnReason reason) {
        if (world.isUnobstructed(this) && world.noCollision(this) && !world.containsAnyLiquid(getBoundingBox())) {
            BlockPos ground = new BlockPos(
                    Mth.floor(getX()),
                    Mth.floor(getBoundingBox().minY) - 1,
                    Mth.floor(getZ())
            );

            BlockState block = world.getBlockState(ground);

            if (block.getBlock() == Blocks.GRASS_BLOCK || block.is(BlockTags.DIRT) || block.is(BlockTags.LEAVES)) {
                playSound(SoundEvents.GRASS_HIT, 1, 0.8F);
                return true;
            }
        }
        return false;
    }

    public List<ItemEntity> getMeatsNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        List<Entity> list = level().getEntities(this, getBoundingBox().inflate(distanceX, distanceY, distanceZ));
        ArrayList<ItemEntity> listEntityItem = new ArrayList<>();
        for (Entity entityNeighbor : list) {
            if (entityNeighbor instanceof ItemEntity itemEntity && distanceTo(entityNeighbor) <= radius) {
                ItemStack stack = itemEntity.getItem();
                if (isMeat(stack)) {
                    listEntityItem.add(itemEntity);
                }
            }
        }
        return listEntityItem;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("tickGrowth", getGrowth());
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setGrowth(input.getIntOr("tickGrowth", 0));
    }

    @Override
    public boolean requiresCustomPersistence() {
        return true;
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(GROWTH, 0);
        builder.define(INFANT, true);
        builder.define(HUNGRY, false);
        builder.define(EATING, ItemStack.EMPTY);
    }

    public int getGrowth() {
        return getEntityData().get(GROWTH);
    }

    public void setGrowth(int growth) {
        getEntityData().set(GROWTH, growth);
    }

    public void incrementGrowth() {
        setGrowth(getGrowth() + 1);
    }

    public boolean getInfant() {
        return getEntityData().get(INFANT);
    }

    public void setInfant(boolean infant) {
        getEntityData().set(INFANT, infant);
    }

    public boolean getHungry() {
        return getEntityData().get(HUNGRY);
    }

    public void setHungry(boolean hungry) {
        getEntityData().set(HUNGRY, hungry);
    }

    public void setEating(ItemStack stack) {
        getEntityData().set(EATING, stack);
    }

    public ItemStack getEating() {
        return getEntityData().get(EATING);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{EAT_ANIMATION};
    }
}