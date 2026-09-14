package net.neoforged.neoforge.client.extensions.common;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;

public interface IClientItemExtensions {
    IClientItemExtensions DEFAULT = new IClientItemExtensions() {};
    Map<ItemLike, IClientItemExtensions> REGISTRY = new IdentityHashMap<>();

    static IClientItemExtensions of(ItemStack stack) {
        return stack == null ? DEFAULT : of(stack.getItem());
    }

    static IClientItemExtensions of(ItemLike item) {
        return REGISTRY.getOrDefault(item, DEFAULT);
    }

    default Model getGenericArmorModel(ItemStack stack, EquipmentClientInfo.LayerType layerType, Model defaultModel) {
        return getHumanoidArmorModel(stack, layerType, defaultModel);
    }

    default Model getHumanoidArmorModel(ItemStack itemStack, EquipmentClientInfo.LayerType layerType, Model original) {
        return original;
    }

    default @Nullable Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer, Identifier _default) {
        return _default;
    }

    default int getDefaultDyeColor(ItemStack stack) {
        return -1;
    }

    default int getArmorLayerTintColor(ItemStack stack, EquipmentClientInfo.Layer layer, int layerIndex, int defaultColor) {
        return defaultColor;
    }
}
