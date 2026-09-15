package com.bobmowzie.mowziesmobs.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MMItemTags extends FabricTagsProvider.ItemTagsProvider {
    public static final TagKey<Item> CAN_HIT_GROTTOL = key("can_hit_grottol");
    public static final TagKey<Item> HAND_WEAPONS = key("hand_weapons");

    public MMItemTags(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, FabricTagsProvider.BlockTagsProvider blockTags) {
        super(output, lookupProvider, blockTags);
    }

    public MMItemTags(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    public static class MMTagAppender implements net.minecraft.data.tags.TagAppender<Item> {
        private final net.minecraft.data.tags.TagAppender<Item> appender;

        public MMTagAppender(net.minecraft.data.tags.TagAppender<Item> appender) {
            this.appender = appender;
        }

        public MMTagAppender add(Item item) {
            this.appender.add(item.builtInRegistryHolder().key());
            return this;
        }

        public MMTagAppender add(Item... items) {
            for (Item item : items) {
                this.appender.add(item.builtInRegistryHolder().key());
            }
            return this;
        }

        @Override
        public MMTagAppender add(net.minecraft.resources.ResourceKey<Item> element) {
            this.appender.add(element);
            return this;
        }

        @Override
        public MMTagAppender addOptional(net.minecraft.resources.ResourceKey<Item> element) {
            this.appender.addOptional(element);
            return this;
        }

        @Override
        public MMTagAppender addTag(TagKey<Item> tag) {
            this.appender.addTag(tag);
            return this;
        }

        @Override
        public MMTagAppender addOptionalTag(TagKey<Item> tag) {
            this.appender.addOptionalTag(tag);
            return this;
        }
    }

    protected MMTagAppender tag(TagKey<Item> tag) {
        return new MMTagAppender(builder(tag));
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        addToVanillaTags();
        addToCommonTags();

        tag(CAN_HIT_GROTTOL)
                .addOptional(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("cagedmobs", "dnasamplerdiamond")))
                .addOptional(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("cagedmobs", "dnasamplernetherite")));

        tag(HAND_WEAPONS).add(ItemHandler.EARTHREND_GAUNTLET);
    }

    private void addToVanillaTags() {
        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .add(ItemHandler.SPEAR)
                .add(ItemHandler.NAGA_FANG_DAGGER);

        tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .add(ItemHandler.SPEAR)
                .add(ItemHandler.NAGA_FANG_DAGGER);

        tag(ItemTags.FIRE_ASPECT_ENCHANTABLE)
                .add(ItemHandler.SPEAR)
                .add(ItemHandler.NAGA_FANG_DAGGER)
                .add(ItemHandler.WROUGHT_AXE);

        tag(ItemTags.AXES)
                .add(ItemHandler.WROUGHT_AXE);

        tag(ItemTags.BOW_ENCHANTABLE)
                .add(ItemHandler.BLOWGUN);

        tag(ItemTags.HEAD_ARMOR)
                .add(ItemHandler.GEOMANCER_BEADS)
                .add(ItemHandler.WROUGHT_HELMET)
                .add(ItemHandler.SOL_VISAGE)
                .add(ItemHandler.UMVUTHANA_MASK_FURY)
                .add(ItemHandler.UMVUTHANA_MASK_FEAR)
                .add(ItemHandler.UMVUTHANA_MASK_RAGE)
                .add(ItemHandler.UMVUTHANA_MASK_BLISS)
                .add(ItemHandler.UMVUTHANA_MASK_MISERY)
                .add(ItemHandler.UMVUTHANA_MASK_FAITH);

        tag(ItemTags.CHEST_ARMOR).add(ItemHandler.GEOMANCER_ROBE);
        tag(ItemTags.LEG_ARMOR).add(ItemHandler.GEOMANCER_BELT);
        tag(ItemTags.FOOT_ARMOR).add(ItemHandler.GEOMANCER_SANDALS);
        tag(ItemTags.PIGLIN_LOVED).add(ItemHandler.SOL_VISAGE);

        tag(ItemTags.PLANKS).add(BlockHandler.PAINTED_ACACIA.asItem());
        tag(ItemTags.WOODEN_SLABS).add(BlockHandler.PAINTED_ACACIA_SLAB.asItem());
    }

    private void addToCommonTags() {
        tag(ConventionalItemTags.TRIDENT_TOOLS).add(ItemHandler.SPEAR);
        tag(ConventionalItemTags.MUSIC_DISCS).add(ItemHandler.PETIOLE_MUSIC_DISC);
        tag(ConventionalItemTags.SEEDS).add(ItemHandler.FOLIAATH_SEED);
        tag(ConventionalItemTags.SLIME_BALLS).add(ItemHandler.GLOWING_JELLY);
    }

    private static TagKey<Item> key(String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MMCommon.MODID, path));
    }
}
