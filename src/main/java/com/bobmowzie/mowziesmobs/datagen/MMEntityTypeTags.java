package com.bobmowzie.mowziesmobs.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalEntityTypeTags;
import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MMEntityTypeTags extends FabricTagsProvider.EntityTypeTagsProvider {
    public MMEntityTypeTags(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    public static class MMTagAppender implements net.minecraft.data.tags.TagAppender<EntityType<?>> {
        private final net.minecraft.data.tags.TagAppender<EntityType<?>> appender;

        public MMTagAppender(net.minecraft.data.tags.TagAppender<EntityType<?>> appender) {
            this.appender = appender;
        }

        public MMTagAppender add(EntityType<?> entityType) {
            this.appender.add(entityType.builtInRegistryHolder().key());
            return this;
        }

        public MMTagAppender add(EntityType<?>... entityTypes) {
            for (EntityType<?> entityType : entityTypes) {
                this.appender.add(entityType.builtInRegistryHolder().key());
            }
            return this;
        }

        @Override
        public MMTagAppender add(net.minecraft.resources.ResourceKey<EntityType<?>> element) {
            this.appender.add(element);
            return this;
        }

        @Override
        public MMTagAppender addOptional(net.minecraft.resources.ResourceKey<EntityType<?>> element) {
            this.appender.addOptional(element);
            return this;
        }

        @Override
        public MMTagAppender addTag(TagKey<EntityType<?>> tag) {
            this.appender.addTag(tag);
            return this;
        }

        @Override
        public MMTagAppender addOptionalTag(TagKey<EntityType<?>> tag) {
            this.appender.addOptionalTag(tag);
            return this;
        }
    }

    protected MMTagAppender tag(TagKey<EntityType<?>> tag) {
        return new MMTagAppender(builder(tag));
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        addToVanillaTags();
        addToCommonTags();

        tag(key("umvuthana"))
                .add(EntityHandler.UMVUTHI)
                .add(EntityHandler.UMVUTHANA_MINION)
                .add(EntityHandler.UMVUTHANA_RAPTOR)
                .add(EntityHandler.UMVUTHANA_CRANE)
                .add(EntityHandler.UMVUTHANA_FOLLOWER_TO_RAPTOR)
                .add(EntityHandler.UMVUTHANA_CRANE_TO_PLAYER)
                .add(EntityHandler.UMVUTHANA_FOLLOWER_TO_PLAYER);

        tag(key("umvuthana_umvuthi_aligned"))
                .add(EntityHandler.UMVUTHI)
                .add(EntityHandler.UMVUTHANA_MINION)
                .add(EntityHandler.UMVUTHANA_RAPTOR)
                .add(EntityHandler.UMVUTHANA_CRANE)
                .add(EntityHandler.UMVUTHANA_FOLLOWER_TO_RAPTOR);
    }

    private void addToVanillaTags() {
        tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER).add(EntityHandler.GROTTOL);
        tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES).add(EntityHandler.FROSTMAW);
        tag(EntityTypeTags.IMPACT_PROJECTILES)
                .add(EntityHandler.DART)
                .add(EntityHandler.BOULDER_PROJECTILE)
                .add(EntityHandler.POISON_BALL);
    }

    private void addToCommonTags() {
        tag(ConventionalEntityTypeTags.BOSSES)
                .add(EntityHandler.FROSTMAW)
                .add(EntityHandler.WROUGHTNAUT)
                .add(EntityHandler.UMVUTHI)
                .add(EntityHandler.SCULPTOR);
        tag(ConventionalEntityTypeTags.CAPTURING_NOT_SUPPORTED)
                .add(EntityHandler.FROSTMAW)
                .add(EntityHandler.WROUGHTNAUT)
                .add(EntityHandler.UMVUTHI)
                .add(EntityHandler.SCULPTOR);
        tag(ConventionalEntityTypeTags.TELEPORTING_NOT_SUPPORTED)
                .add(EntityHandler.UMVUTHI)
                .add(EntityHandler.SCULPTOR);
    }

    private static TagKey<EntityType<?>> key(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MMCommon.MODID, path));
    }
}
