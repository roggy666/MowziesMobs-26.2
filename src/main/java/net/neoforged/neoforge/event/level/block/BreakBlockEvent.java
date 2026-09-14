package net.neoforged.neoforge.event.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class BreakBlockEvent extends Event implements ICancellableEvent {
    private final Level level;
    private final BlockPos pos;
    private final BlockState state;
    private final Player player;
    private boolean canceled;

    public BreakBlockEvent(Level level, BlockPos pos, BlockState state, Player player) {
        this.level = level;
        this.pos = pos;
        this.state = state;
        this.player = player;
    }

    public Level getLevel() {
        return level;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getState() {
        return state;
    }

    public Player getPlayer() {
        return player;
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
