package net.neoforged.neoforge.event.tick;

import net.minecraft.world.entity.player.Player;

public class PlayerTickEvent extends EntityTickEvent {
    public PlayerTickEvent(Player player) {
        super(player);
    }

    @Override
    public Player getEntity() {
        return (Player) super.getEntity();
    }

    public static class Pre extends PlayerTickEvent {
        public Pre(Player player) {
            super(player);
        }
    }

    public static class Post extends PlayerTickEvent {
        public Post(Player player) {
            super(player);
        }
    }
}
