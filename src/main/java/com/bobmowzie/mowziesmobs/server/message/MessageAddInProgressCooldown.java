package com.bobmowzie.mowziesmobs.server.message;

import com.bobmowzie.mowziesmobs.MMCommon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public record MessageAddInProgressCooldown(Item item, Integer startTime, Integer endTime) implements CustomPacketPayload {
    public static final Type<MessageAddInProgressCooldown> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MMCommon.MODID, "message_add_in_progress_cooldown"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MessageAddInProgressCooldown> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ITEM),
            MessageAddInProgressCooldown::item,
            ByteBufCodecs.VAR_INT,
            MessageAddInProgressCooldown::startTime,
            ByteBufCodecs.VAR_INT,
            MessageAddInProgressCooldown::endTime,
            MessageAddInProgressCooldown::new
    );

    public static void handleClient(final MessageAddInProgressCooldown packet, final Player player) {
        if (player != null) {
            player.getCooldowns().addCooldown(BuiltInRegistries.ITEM.getKey(packet.item()), packet.endTime() - packet.startTime());
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
