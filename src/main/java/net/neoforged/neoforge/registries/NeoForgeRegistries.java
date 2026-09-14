package net.neoforged.neoforge.registries;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.world.BiomeModifier;

public class NeoForgeRegistries {
    public static final ResourceKey<Registry<EntityDataSerializer<?>>> ENTITY_DATA_SERIALIZERS = 
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("neoforge", "entity_data_serializers"));

    @SuppressWarnings("unchecked")
    public static final ResourceKey<Registry<AttachmentType<?>>> ATTACHMENT_TYPES = 
            (ResourceKey) ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("neoforge", "attachment_types"));

    @SuppressWarnings("unchecked")
    public static final ResourceKey<Registry<MapCodec<? extends BiomeModifier>>> BIOME_MODIFIER_SERIALIZERS = 
            (ResourceKey) ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("neoforge", "biome_modifier_serializers"));
}
