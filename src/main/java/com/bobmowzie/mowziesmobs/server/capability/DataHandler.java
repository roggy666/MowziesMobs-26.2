package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class DataHandler {
    public static final AttachmentType<FrozenData> FROZEN_DATA = register("frozen_data", FrozenData::new);
    public static final AttachmentType<LivingData> LIVING_DATA = register("living_data", LivingData::new);

    public static final AttachmentType<PlayerData> PLAYER_DATA = register("player_data", PlayerData::new);
    public static final AttachmentType<AbilityData> ABILITY_DATA = register("ability_data", AbilityData::new);

    private static <T extends SerializableData> AttachmentType<T> register(String name, Supplier<T> factory) {
        return AttachmentRegistry.create(MMCommon.resource(name), builder -> builder
                .initializer(factory)
                .persistent(codec(factory)));
    }

    private static <T extends SerializableData> Codec<T> codec(Supplier<T> factory) {
        return CompoundTag.CODEC.xmap(tag -> {
            T data = factory.get();
            data.deserialize(TagValueInput.create(ProblemReporter.DISCARDING, RegistryAccess.EMPTY, tag));
            return data;
        }, data -> {
            TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, RegistryAccess.EMPTY);
            data.serialize(output);
            return output.buildResult();
        });
    }

    public static void register() {
    }

    // Single point of usage in case additional checks are needed etc.
    public static <T> @NotNull T getData(@NotNull Entity entity, @NotNull AttachmentType<T> type) {
        if (PLAYER_DATA == type && !(entity instanceof Player)) {
            // It's basically choosing between checking which entity gets passed into here vs. having a null check for every call of this method
            throw new IllegalArgumentException("Cannot fetch player data for non-player entity of type [" + entity.getType() + "]");
        }

        return entity.getAttachedOrCreate(type);
    }
}
