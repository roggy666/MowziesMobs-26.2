package net.neoforged.neoforge.entity;

import net.minecraft.world.entity.Entity;

public interface PartEntity<T extends Entity> {
    T getParent();
}
