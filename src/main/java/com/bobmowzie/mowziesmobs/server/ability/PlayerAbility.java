package com.bobmowzie.mowziesmobs.server.ability;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import com.bobmowzie.mowziesmobs.client.model.tools.geckolib.MowzieAnimationController;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.object.LoopType;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.RawAnimation;
import com.geckolib.renderer.base.GeoRenderState;

public class PlayerAbility extends Ability<Player> {
    protected RawAnimation activeFirstPersonAnimation;

    protected ItemStack heldItemMainHandVisualOverride;
    protected ItemStack heldItemOffHandVisualOverride;

    public enum HandDisplay {
        DEFAULT,
        DONT_RENDER,
        FORCE_RENDER
    }

    protected HandDisplay firstPersonMainHandDisplay;
    protected HandDisplay firstPersonOffHandDisplay;

    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("idle");

    public PlayerAbility(AbilityType<Player, ? extends Ability<?>> abilityType, Player user, AbilitySection[] sectionTrack, int cooldownMax) {
        super(abilityType, user, sectionTrack, cooldownMax);
        if (user.level().isClientSide()) {
            this.activeAnimation = IDLE_ANIM;
            heldItemMainHandVisualOverride = null;
            heldItemOffHandVisualOverride = null;
            firstPersonMainHandDisplay = HandDisplay.DEFAULT;
            firstPersonOffHandDisplay = HandDisplay.DEFAULT;
        }
    }

    public PlayerAbility(AbilityType<Player, ? extends Ability> abilityType, Player user, AbilitySection[] sectionTrack) {
        this(abilityType, user, sectionTrack, 0);
    }

    public void playAnimation(RawAnimation animation, GeckoPlayer.Perspective perspective) {
        if (getUser() != null && getUser().level().isClientSide()) {
            if (perspective == GeckoPlayer.Perspective.FIRST_PERSON) {
                activeFirstPersonAnimation = animation;
            }
            else {
                activeAnimation = animation;
            }
            MowzieAnimationController<GeckoPlayer> controller = GeckoPlayer.getAnimationController(getUser(), perspective);
            GeckoPlayer geckoPlayer = GeckoPlayer.getGeckoPlayer(getUser(), perspective);
            if (controller != null && geckoPlayer != null) {
                AnimatableManager<GeckoPlayer> manager = geckoPlayer.getAnimatableInstanceCache()
                        .getManagerForId(geckoPlayer.getPlayerRenderer().getInstanceId(geckoPlayer, null));
                controller.playAnimation(geckoPlayer, new GeoRenderState.Impl(), manager, geckoPlayer.getModel(), animation);
            }
        }
    }

    public void playAnimation(String animationName, GeckoPlayer.Perspective perspective, LoopType loopType) {
        playAnimation(RawAnimation.begin().then(animationName, loopType), perspective);
    }

    public void playAnimation(RawAnimation animation) {
        playAnimation(animation, GeckoPlayer.Perspective.FIRST_PERSON);
        playAnimation(animation, GeckoPlayer.Perspective.THIRD_PERSON);
    }

    public void playAnimation3rdPerson(RawAnimation animation) {
        playAnimation(animation, GeckoPlayer.Perspective.THIRD_PERSON);
    }

    public InteractionHand getActiveHand() {
        return getUser().getUsedItemHand();
    }

    public void playAnimation(String animationName, LoopType loopType, boolean separateLeftAndRight1stPerson, boolean separateLeftAndRight3rdPerson) {
        boolean usingMainHand = getActiveHand() == InteractionHand.MAIN_HAND;
        boolean isRightHanded = getUser().getMainArm() == HumanoidArm.RIGHT;
        // 1st person
        if (separateLeftAndRight1stPerson) {
            playAnimation(animationName + (usingMainHand ? "_right" : "_left"), GeckoPlayer.Perspective.FIRST_PERSON, loopType);
        }
        else {
            playAnimation(animationName, GeckoPlayer.Perspective.FIRST_PERSON, loopType);
        }

        // 3rd person
        if (separateLeftAndRight3rdPerson) {
            playAnimation(animationName + "_" + (usingMainHand == isRightHanded ? "right" : "left"), GeckoPlayer.Perspective.THIRD_PERSON, loopType);
        }
        else {
            playAnimation(RawAnimation.begin().then(animationName, loopType), GeckoPlayer.Perspective.THIRD_PERSON);
        }
    }

    @Override
    public void end() {
        super.end();
        if (getUser().level().isClientSide()) {
            heldItemMainHandVisualOverride = null;
            heldItemOffHandVisualOverride = null;
            firstPersonMainHandDisplay = HandDisplay.DEFAULT;
            firstPersonOffHandDisplay = HandDisplay.DEFAULT;
        }
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !getUser().isSpectator();
    }

    @Override
    protected boolean canContinueUsing() {
        return super.canContinueUsing() && !getUser().isSpectator();
    }

    public <E extends GeoEntity> PlayState animationPredicate(AnimationTest<E> e, GeckoPlayer.Perspective perspective) {
        RawAnimation whichAnimation;
        if (perspective == GeckoPlayer.Perspective.FIRST_PERSON) {
            whichAnimation = activeFirstPersonAnimation;
        }
        else {
            whichAnimation = activeAnimation;
        }
        if (whichAnimation == null || whichAnimation.getAnimationStages().isEmpty())
            return PlayState.STOP;
        e.controller().setAnimation(whichAnimation);
        return PlayState.CONTINUE;
    }

    public ItemStack heldItemMainHandOverride() {
        return heldItemMainHandVisualOverride;
    }

    public ItemStack heldItemOffHandOverride() {
        return heldItemOffHandVisualOverride;
    }

    public HandDisplay getFirstPersonMainHandDisplay() {
        return firstPersonMainHandDisplay;
    }

    public HandDisplay getFirstPersonOffHandDisplay() {
        return firstPersonOffHandDisplay;
    }

    // Events
    public void onRightClickEmpty(Player player, InteractionHand hand) {

    }

    public void onRightClickBlock(Player player, InteractionHand hand, BlockHitResult hitResult) {

    }

    public void onRightClickWithItem(Player player, InteractionHand hand) {

    }

    public void onRightClickEntity(Player player, InteractionHand hand, Entity target) {

    }

    public void onLeftClickEmpty(Player player) {

    }

    public void onLeftClickBlock(Player player, BlockPos pos, Direction direction) {

    }

    public void onLeftClickEntity(Player player, Entity target) {

    }

    @Override
    public void onTakeDamage(DamageSource source, float damage) {
        super.onTakeDamage(source, damage);
    }

    public void onJump(Player player) {

    }

    /** Returns the (possibly modified) fall damage multiplier. */
    public float onFall(Player player, double distance, float damageMultiplier) {
        return damageMultiplier;
    }

    public void onRightMouseDown(Player player) {

    }

    public void onLeftMouseDown(Player player) {

    }

    public void onRightMouseUp(Player player) {

    }

    public void onLeftMouseUp(Player player) {

    }

    public void onSneakDown(Player player) {

    }

    public void onSneakUp(Player player) {

    }
}
