package net.neoforged.neoforge.event.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class BlockEvent extends Event {
    private final LevelAccessor level;
    private final BlockPos pos;
    private final BlockState state;

    public BlockEvent(LevelAccessor level, BlockPos pos, BlockState state) {
        this.level = level;
        this.pos = pos;
        this.state = state;
    }

    public LevelAccessor getLevel() {
        return level;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getState() {
        return state;
    }

    public static class EntityPlaceEvent extends BlockEvent implements ICancellableEvent {
        private final Entity entity;
        private final BlockState placedBlock;
        private boolean canceled;

        public EntityPlaceEvent(LevelAccessor level, BlockPos pos, BlockState placedBlock, Entity entity) {
            super(level, pos, placedBlock);
            this.placedBlock = placedBlock;
            this.entity = entity;
        }

        public Entity getEntity() {
            return entity;
        }

        public BlockState getPlacedBlock() {
            return placedBlock;
        }

        @Override
        public boolean isCanceled() {
            return canceled;
        }

        @Override
        public void setCanceled(boolean cancel) {
            this.canceled = cancel;
        }
    }
}
