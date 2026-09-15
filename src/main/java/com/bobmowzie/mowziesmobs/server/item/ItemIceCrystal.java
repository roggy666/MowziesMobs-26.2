package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.server.ability.Ability;
import com.bobmowzie.mowziesmobs.server.ability.AbilityHandler;
import com.bobmowzie.mowziesmobs.server.capability.DataHandler;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by BobMowzie on 6/6/2017.
 */
public class ItemIceCrystal extends Item {
    public ItemIceCrystal(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level worldIn, Player player, InteractionHand handIn) {
        ItemStack stack = player.getItemInHand(handIn);
        boolean breakable = ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable.get();
        if (!breakable || stack.getDamageValue() + 5 < stack.getMaxDamage()) {
            if (!worldIn.isClientSide()) AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.ICE_BREATH_ABILITY);
            if (breakable) {
                stack.hurtAndBreak(5, player, handIn.asEquipmentSlot());
            }
            player.startUsingItem(handIn);
            return InteractionResult.CONSUME;
        } else {
            Ability<?> ability = AbilityHandler.INSTANCE.getAbility(player, AbilityHandler.ICE_BREATH_ABILITY);
            if (ability != null && ability.isUsing()) {
                ability.end();
            }
        }
        return super.use(worldIn, player, handIn);
    }

    // PORTING NOTE (1.21.1 -> 26.1.2): Item#releaseUsing now returns boolean instead of void.
    @Override
    public boolean releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            Ability<?> iceBreathAbility = AbilityHandler.INSTANCE.getAbility(player, AbilityHandler.ICE_BREATH_ABILITY);
            if (iceBreathAbility != null && iceBreathAbility.isUsing()) {
                iceBreathAbility.end();
            }
        }
        return super.releaseUsing(stack, worldIn, entityLiving, timeLeft);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    public int getMaxDamage(ItemStack stack) {
        return ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durability.get();
    }

    // isEnchantable(ItemStack) no longer exists as an Item/IItemExtension override point - this item's registration
    // never calls Properties#enchantable(int) either, so it remains non-enchantable by default, matching the old override.

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, context, display, tooltip, flagIn);
        ItemHandler.addTooltip(tooltip, getDescriptionId() + ".text.0");
        if (!ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.breakable.get()) {
            ItemHandler.addTooltip(tooltip, getDescriptionId() + ".text.1");
        }
    }
}
