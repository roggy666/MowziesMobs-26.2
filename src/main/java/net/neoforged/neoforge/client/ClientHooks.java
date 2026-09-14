package net.neoforged.neoforge.client;

import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class ClientHooks {
    public static Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType layerType, EquipmentClientInfo.Layer layer, Identifier defaultTexture) {
        return defaultTexture;
    }
}
