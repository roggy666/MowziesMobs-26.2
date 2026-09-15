package com.bobmowzie.mowziesmobs.mixin;

import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigurableArmorMaterial;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

// ArmorMaterial is a record whose createAttributes(ArmorType) reads the defense/toughness fields directly, so the
// config multipliers are applied to the attribute modifiers it produces. createAttributes is called once per item at
// registration (Item.Properties#humanoidArmor), after the config has been loaded in MMCommon#onInitialize.
@Mixin(ArmorMaterial.class)
public abstract class ArmorMaterialMixin implements ConfigurableArmorMaterial {
    @Unique @Nullable private ConfigHandler.ArmorConfig mowziesmobs$config;

    @Override
    public void mowziesmobs$setConfig(ConfigHandler.ArmorConfig config) {
        this.mowziesmobs$config = config;
    }

    @ModifyReturnValue(method = "createAttributes", at = @At("RETURN"))
    private ItemAttributeModifiers mowziesmobs$configurableAttributes(ItemAttributeModifiers original, ArmorType type) {
        if (mowziesmobs$config == null || !ConfigHandler.COMMON_CONFIG.isLoaded()) {
            return original;
        }

        float defenseMultiplier = mowziesmobs$config.damageReductionMultiplier.get().floatValue();
        float toughnessMultiplier = mowziesmobs$config.toughnessMultiplier.get().floatValue();

        List<ItemAttributeModifiers.Entry> newEntries = new ArrayList<>(original.modifiers().size());
        for (ItemAttributeModifiers.Entry entry : original.modifiers()) {
            AttributeModifier modifier = entry.modifier();
            if (entry.attribute() == Attributes.ARMOR) {
                modifier = new AttributeModifier(modifier.id(), modifier.amount() * defenseMultiplier, modifier.operation());
            } else if (entry.attribute() == Attributes.ARMOR_TOUGHNESS) {
                modifier = new AttributeModifier(modifier.id(), modifier.amount() * toughnessMultiplier, modifier.operation());
            }
            newEntries.add(new ItemAttributeModifiers.Entry(entry.attribute(), modifier, entry.slot(), entry.display()));
        }

        return new ItemAttributeModifiers(newEntries);
    }
}
