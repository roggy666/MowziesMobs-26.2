package net.neoforged.neoforge.event.entity.living;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.bus.api.Event;

public class MobSpawnEvent extends Event {
    public static class SpawnPlacementCheck extends Event {
        public enum Result {
            DEFAULT,
            ALLOW,
            FAIL
        }

        private final EntityType<? extends Mob> entityType;
        private final ServerLevelAccessor level;
        private final BlockPos pos;
        private Result result = Result.DEFAULT;

        public SpawnPlacementCheck(EntityType<? extends Mob> entityType, ServerLevelAccessor level, BlockPos pos) {
            this.entityType = entityType;
            this.level = level;
            this.pos = pos;
        }

        public EntityType<? extends Mob> getEntityType() {
            return entityType;
        }

        public ServerLevelAccessor getLevel() {
            return level;
        }

        public BlockPos getPos() {
            return pos;
        }

        public Result getResult() {
            return result;
        }

        public void setResult(Result result) {
            this.result = result;
        }
    }
}
