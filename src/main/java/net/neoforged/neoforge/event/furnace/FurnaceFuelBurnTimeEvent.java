package net.neoforged.neoforge.event.furnace;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;

public class FurnaceFuelBurnTimeEvent extends Event {
    private final ItemStack itemStack;
    private final @Nullable RecipeType<?> recipeType;
    private int burnTime;

    public FurnaceFuelBurnTimeEvent(ItemStack itemStack, int burnTime, @Nullable RecipeType<?> recipeType) {
        this.itemStack = itemStack;
        this.burnTime = burnTime;
        this.recipeType = recipeType;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public @Nullable RecipeType<?> getRecipeType() {
        return recipeType;
    }

    public int getBurnTime() {
        return burnTime;
    }

    public void setBurnTime(int burnTime) {
        this.burnTime = burnTime;
    }
}
