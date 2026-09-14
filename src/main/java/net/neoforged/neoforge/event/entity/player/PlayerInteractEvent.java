package net.neoforged.neoforge.event.entity.player;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class PlayerInteractEvent extends Event implements ICancellableEvent {
    private final Player entity;
    private final InteractionHand hand;
    private final BlockPos pos;
    private boolean canceled;

    public PlayerInteractEvent(Player player, InteractionHand hand, BlockPos pos) {
        this.entity = player;
        this.hand = hand;
        this.pos = pos;
    }

    public Player getEntity() {
        return entity;
    }

    public InteractionHand getHand() {
        return hand;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Level getLevel() {
        return entity.level();
    }

    public ItemStack getItemStack() {
        return entity.getItemInHand(hand != null ? hand : InteractionHand.MAIN_HAND);
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean cancel) {
        this.canceled = cancel;
    }

    public static class RightClickEmpty extends PlayerInteractEvent {
        public RightClickEmpty(Player player, InteractionHand hand) {
            super(player, hand, player.blockPosition());
        }
    }

    public static class EntityInteract extends PlayerInteractEvent {
        private final Entity target;

        public EntityInteract(Player player, InteractionHand hand, Entity target) {
            super(player, hand, target.blockPosition());
            this.target = target;
        }

        public Entity getTarget() {
            return target;
        }
    }

    public static class RightClickBlock extends PlayerInteractEvent {
        private final BlockHitResult hitVec;

        public RightClickBlock(Player player, InteractionHand hand, BlockPos pos, BlockHitResult hitVec) {
            super(player, hand, pos);
            this.hitVec = hitVec;
        }

        public BlockHitResult getHitVec() {
            return hitVec;
        }
    }

    public static class LeftClickEmpty extends PlayerInteractEvent {
        public LeftClickEmpty(Player player) {
            super(player, InteractionHand.MAIN_HAND, player.blockPosition());
        }
    }

    public static class RightClickItem extends PlayerInteractEvent {
        public RightClickItem(Player player, InteractionHand hand) {
            super(player, hand, player.blockPosition());
        }
    }

    public static class LeftClickBlock extends PlayerInteractEvent {
        public LeftClickBlock(Player player, BlockPos pos) {
            super(player, InteractionHand.MAIN_HAND, pos);
        }
    }
}
