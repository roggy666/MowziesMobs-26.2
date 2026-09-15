package com.bobmowzie.mowziesmobs.server.capability;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public interface SerializableData {
    void serialize(ValueOutput output);

    void deserialize(ValueInput input);
}
