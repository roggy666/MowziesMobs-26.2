package com.bobmowzie.mowziesmobs.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.block.BlockHandler;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MMRecipes extends FabricRecipeProvider {
    public MMRecipes(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider lookupProvider, RecipeOutput recipeOutput) {
        return new RecipeProvider(lookupProvider, recipeOutput) {
            @Override
            public void buildRecipes() {
                // Shaped
                shaped(RecipeCategory.COMBAT, ItemHandler.SPEAR)
                        .pattern(" F ")
                        .pattern("ES ")
                        .pattern(" S ")
                        .define('F', Items.FLINT)
                        .define('S', ConventionalItemTags.WOODEN_RODS)
                        .define('E', ConventionalItemTags.FEATHERS)
                        .unlockedBy(getHasName(Items.FLINT), has(Items.FLINT))
                        .save(output);

                shaped(RecipeCategory.COMBAT, ItemHandler.BLOWGUN)
                        .pattern(" B ")
                        .pattern("SBS")
                        .pattern(" B ")
                        .define('B', Items.BAMBOO)
                        .define('S', ConventionalItemTags.STRINGS)
                        .unlockedBy(getHasName(Items.BAMBOO), has(Items.BAMBOO))
                        .save(output);

                shaped(RecipeCategory.COMBAT, ItemHandler.DART, 8)
                        .pattern("F")
                        .pattern("S")
                        .pattern("E")
                        .define('F', ItemHandler.NAGA_FANG)
                        .define('S', ConventionalItemTags.WOODEN_RODS)
                        .define('E', ConventionalItemTags.FEATHERS)
                        .unlockedBy(getHasName(ItemHandler.NAGA_FANG), has(ItemHandler.NAGA_FANG))
                        .save(output);

                shaped(RecipeCategory.COMBAT, ItemHandler.NAGA_FANG_DAGGER)
                        .pattern("F")
                        .pattern("I")
                        .pattern("S")
                        .define('F', ItemHandler.NAGA_FANG)
                        .define('I', ConventionalItemTags.IRON_INGOTS)
                        .define('S', ConventionalItemTags.WOODEN_RODS)
                        .unlockedBy(getHasName(ItemHandler.NAGA_FANG), has(ItemHandler.NAGA_FANG))
                        .save(output);

                shaped(RecipeCategory.BUILDING_BLOCKS, BlockHandler.PAINTED_ACACIA)
                        .pattern("S")
                        .pattern("S")
                        .define('S', BlockHandler.PAINTED_ACACIA_SLAB)
                        .unlockedBy(getHasName(BlockHandler.PAINTED_ACACIA_SLAB), has(BlockHandler.PAINTED_ACACIA_SLAB))
                        .save(output, ResourceKey.create(Registries.RECIPE, MMCommon.resource("painted_acacia_block_from_slab")));

                shaped(RecipeCategory.BUILDING_BLOCKS, BlockHandler.PAINTED_ACACIA_SLAB)
                        .pattern("###")
                        .define('#', BlockHandler.PAINTED_ACACIA)
                        .unlockedBy(getHasName(BlockHandler.PAINTED_ACACIA), has(BlockHandler.PAINTED_ACACIA))
                        .save(output);

                // Shapeless
                shapeless(RecipeCategory.BUILDING_BLOCKS, Items.JUNGLE_PLANKS, 4)
                        .requires(BlockHandler.CLAWED_LOG)
                        .unlockedBy(getHasName(BlockHandler.CLAWED_LOG), has(BlockHandler.CLAWED_LOG))
                        .save(output, ResourceKey.create(Registries.RECIPE, MMCommon.resource("jungle_planks_from_clawed_log")));

                shapeless(RecipeCategory.BUILDING_BLOCKS, BlockHandler.PAINTED_ACACIA, 4)
                        .requires(Items.ACACIA_PLANKS, 4)
                        .requires(Items.DYE.yellow())
                        .requires(Items.DYE.white())
                        .requires(Items.DYE.cyan())
                        .unlockedBy("has_painted_acacia_materials", has(
                                BlockHandler.PAINTED_ACACIA,
                                Items.DYE.yellow(),
                                Items.DYE.white(),
                                Items.DYE.cyan())
                        )
                        .save(output);

                shapeless(RecipeCategory.BUILDING_BLOCKS, BlockHandler.THATCH)
                        .requires(Ingredient.of(Items.SHORT_GRASS, Items.TALL_GRASS), 9)
                        .unlockedBy("has_thatch_material", has(Items.SHORT_GRASS, Items.TALL_GRASS))
                        .save(output);

                // Smelting
                SimpleCookingRecipeBuilder
                        .smelting(Ingredient.of(BlockHandler.CLAWED_LOG), RecipeCategory.MISC, CookingBookCategory.MISC, Items.CHARCOAL, 0.15f, 200)
                        .unlockedBy(getHasName(BlockHandler.CLAWED_LOG), has(BlockHandler.CLAWED_LOG))
                        .save(output, ResourceKey.create(Registries.RECIPE, MMCommon.resource("charcoal")));

                SimpleCookingRecipeBuilder
                        .smelting(Ingredient.of(ItemHandler.CAPTURED_GROTTOL), RecipeCategory.MISC, CookingBookCategory.MISC, Items.DIAMOND, 1, 200)
                        .unlockedBy(getHasName(ItemHandler.CAPTURED_GROTTOL), has(ItemHandler.CAPTURED_GROTTOL))
                        .save(output, ResourceKey.create(Registries.RECIPE, MMCommon.resource("grottol_smelt")));

                // Blasting
                SimpleCookingRecipeBuilder
                        .blasting(Ingredient.of(ItemHandler.CAPTURED_GROTTOL), RecipeCategory.MISC, CookingBookCategory.MISC, Items.DIAMOND, 1, 200)
                        .unlockedBy(getHasName(ItemHandler.CAPTURED_GROTTOL), has(ItemHandler.CAPTURED_GROTTOL))
                        .save(output, ResourceKey.create(Registries.RECIPE, MMCommon.resource("grottol_blast")));
            }

            protected @NotNull Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike... items) {
                return inventoryTrigger(ItemPredicate.Builder.item().of(this.registries.lookupOrThrow(Registries.ITEM), items));
            }
        };
    }

    @Override
    public @NotNull String getName() {
        return "Mowzie's Mobs Recipes";
    }
}
