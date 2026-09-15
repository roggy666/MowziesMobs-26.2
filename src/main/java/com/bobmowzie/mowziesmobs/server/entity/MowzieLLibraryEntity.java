package com.bobmowzie.mowziesmobs.server.entity;

import net.minecraft.server.level.ServerEntity;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import com.ilexiconn.llibrary.server.animation.Animation;
import com.ilexiconn.llibrary.server.animation.AnimationHandler;
import com.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

public abstract class MowzieLLibraryEntity extends MowzieEntity implements IAnimatedEntity {
    private int animationTick;
    private Animation animation = NO_ANIMATION;

    public MowzieLLibraryEntity(EntityType<? extends MowzieEntity> type, Level world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (getAnimation() != NO_ANIMATION) {
            animationTick++;
            if (level().isClientSide() && animationTick >= animation.getDuration()) {
                setAnimation(NO_ANIMATION);
            }
        }
    }

    protected void onAnimationFinish(Animation animation) {}

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        boolean attack = super.hurtServer(level, source, damage);
        if (attack) {
            if (getHealth() > 0.0F && (getAnimation() == NO_ANIMATION || hurtInterruptsAnimation) && playsHurtAnimation) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, getHurtAnimation());
            } else if (getHealth() <= 0.0F) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, getDeathAnimation());
            }
        }
        return attack;
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return this.animation;
    }

    @Override
    public void setAnimation(Animation animation) {
        if (animation == NO_ANIMATION) {
            onAnimationFinish(this.animation);
        }
        this.animation = animation;
        setAnimationTick(0);
    }

    public abstract Animation getDeathAnimation();

    public abstract Animation getHurtAnimation();

    @Override
    protected int getDeathDuration() {
        Animation death;
        if ((death = getDeathAnimation()) != null) {
            return death.getDuration() - 20;
        }
        return 20;
    }

    // The current animation (index + tick) is packed into the spawn packet's data field so that players that start
    // tracking this entity mid-animation see the right pose
    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        int animOrdinal = ArrayUtils.indexOf(this.getAnimations(), this.getAnimation());
        return new ClientboundAddEntityPacket(this, serverEntity, ((animOrdinal + 1) << 16) | (this.getAnimationTick() & 0xFFFF));
    }

    @Override
    public void recreateFromPacket(@NotNull ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        int animOrdinal = (packet.getData() >>> 16) - 1;
        int animTick = packet.getData() & 0xFFFF;
        this.setAnimation(animOrdinal == -1 ? IAnimatedEntity.NO_ANIMATION : this.getAnimations()[animOrdinal]);
        this.setAnimationTick(animTick);
    }
}
