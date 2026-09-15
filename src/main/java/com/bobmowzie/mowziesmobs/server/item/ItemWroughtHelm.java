package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.client.model.LayerHandler;
import com.bobmowzie.mowziesmobs.client.model.armor.WroughtHelmModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.function.Consumer;

public class ItemWroughtHelm extends Item {
    public ItemWroughtHelm(Item.Properties properties) {
        // ArmorItem was removed upstream - repair-while-breakable is now handled via a conditional
        // DataComponents.REPAIRABLE removal in ItemHandler#modifyComponents instead of overriding
        // isValidRepairItem(ItemStack, ItemStack), which no longer exists. .humanoidArmor() already grants
        // enchantability from the material's enchantment value, matching the old "isEnchantable() -> true" override.
        super(properties.humanoidArmor(MaterialHandler.ARMOR_WROUGHT_HELM, ArmorType.HELMET));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, display, tooltip, flagIn);
        ItemHandler.addTooltip(tooltip, getDescriptionId() + ".text.0");
    }

    public static final class ArmorRender {
        private static HumanoidModel<?> MODEL;

        public static Model getArmorModel() {
            if (MODEL == null) {
                EntityModelSet models = Minecraft.getInstance().getEntityModels();
                ModelPart root = models.bakeLayer(LayerHandler.WROUGHT_HELM_LAYER);
                MODEL = new WroughtHelmModel<>(root);
            }
            return MODEL;
        }
    }
}
