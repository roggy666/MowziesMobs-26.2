package com.bobmowzie.mowziesmobs.server.capability;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.google.common.collect.MapMaker;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DataHandler {
    private static final Map<Entity, Map<Object, Object>> ATTACHMENT_DATA = new MapMaker().weakKeys().makeMap();

    public static final DeferredRegister<AttachmentType<?>> MM_ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MMCommon.MODID);

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<FrozenData>> FROZEN_DATA = MM_ATTACHMENT_TYPES.register("frozen_data", () -> AttachmentType.serializable(FrozenData::new).build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<LivingData>> LIVING_DATA = MM_ATTACHMENT_TYPES.register("living_data", () -> AttachmentType.serializable(LivingData::new).build());

    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerData>> PLAYER_DATA = MM_ATTACHMENT_TYPES.register("player_data", () -> AttachmentType.serializable(PlayerData::new).build());
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<AbilityData>> ABILITY_DATA = MM_ATTACHMENT_TYPES.register("ability_data", () -> AttachmentType.serializable(AbilityData::new).build());

    @SuppressWarnings("unchecked")
    public static <T> @NotNull T getData(@NotNull Entity entity, @NotNull DeferredHolder<AttachmentType<?>, AttachmentType<T>> type) {
        if (PLAYER_DATA.equals(type) && !(entity instanceof Player)) {
            throw new IllegalArgumentException("Cannot fetch player data for non-player entity of type [" + entity.getType() + "]");
        }

        Map<Object, Object> entityMap = ATTACHMENT_DATA.computeIfAbsent(entity, k -> new ConcurrentHashMap<>());
        return (T) entityMap.computeIfAbsent(type, k -> type.get().create());
    }
}
