package com.bobmowzie.mowziesmobs.server.item;

import com.bobmowzie.mowziesmobs.MMCommon;
import com.bobmowzie.mowziesmobs.server.config.ConfigHandler;
import com.bobmowzie.mowziesmobs.server.entity.EntityHandler;
import com.bobmowzie.mowziesmobs.server.entity.elokosa.PawType;
import com.bobmowzie.mowziesmobs.server.entity.umvuthana.MaskType;
import com.bobmowzie.mowziesmobs.server.sound.MMSounds;
import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.DispenserBlock;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ItemHandler {
    // Default attribute values
    private static final int NEGATE_ATTACK_DAMAGE = -2; // 2 as base set for players
    private static final int NEGATE_ATTACK_SPEED = -4; // 4 as base from the attribute

    public static Style TOOLTIP_STYLE = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY));


    public static final ItemFoliaathSeed FOLIAATH_SEED = register("foliaath_seed", p -> new ItemFoliaathSeed(p));
    public static final ItemMobRemover MOB_REMOVER = register("mob_remover", p -> new ItemMobRemover(p));
    public static final ItemWroughtAxe WROUGHT_AXE = register("wrought_axe", p -> new ItemWroughtAxe(p.rarity(Rarity.UNCOMMON)));
    // NOTE: durability is now baked into ItemXxx's constructor via Properties#humanoidArmor(material, slot) (ArmorItem
    // was removed upstream), which pulls the same per-piece multiplier that used to be passed here explicitly.
    public static final ItemWroughtHelm WROUGHT_HELMET = register("wrought_helmet", p -> new ItemWroughtHelm(p.rarity(Rarity.UNCOMMON)));
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_FURY = register("umvuthana_mask_fury", p -> new ItemUmvuthanaMask(MaskType.FURY, p));
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_FEAR = register("umvuthana_mask_fear", p -> new ItemUmvuthanaMask(MaskType.FEAR, p));
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_RAGE = register("umvuthana_mask_rage", p -> new ItemUmvuthanaMask(MaskType.RAGE, p));
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_BLISS = register("umvuthana_mask_bliss", p -> new ItemUmvuthanaMask(MaskType.BLISS, p));
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_MISERY = register("umvuthana_mask_misery", p -> new ItemUmvuthanaMask(MaskType.MISERY, p));
    public static final ItemUmvuthanaMask UMVUTHANA_MASK_FAITH = register("umvuthana_mask_faith", p -> new ItemUmvuthanaMask(MaskType.FAITH, p));
    public static final ItemSolVisage SOL_VISAGE = register("sol_visage", p -> new ItemSolVisage(p.rarity(Rarity.RARE)));
    public static final ItemDart DART = register("dart", p -> new ItemDart(p));
    public static final ItemSpear SPEAR = register("spear", p -> new ItemSpear(p.stacksTo(1)));
    public static final ItemBlowgun BLOWGUN = register("blowgun", p -> new ItemBlowgun(p.stacksTo(1).durability(300)));
    public static final ItemGrantSunsBlessing GRANT_SUNS_BLESSING = register("grant_suns_blessing", p -> new ItemGrantSunsBlessing(p.stacksTo(1).rarity(Rarity.EPIC)));
    public static final ItemIceCrystal ICE_CRYSTAL = register("ice_crystal", p -> new ItemIceCrystal(p.durability(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ICE_CRYSTAL.durabilityValue).rarity(Rarity.RARE)));
    public static final ItemCapturedGrottol CAPTURED_GROTTOL = register("captured_grottol", p -> new ItemCapturedGrottol(p.stacksTo(1)));
    public static final ItemGlowingJelly GLOWING_JELLY = register("glowing_jelly", p -> new ItemGlowingJelly(p.food(ItemGlowingJelly.GLOWING_JELLY_FOOD, ItemGlowingJelly.GLOWING_JELLY_CONSUMABLE)));
    public static final ItemNagaFang NAGA_FANG = register("naga_fang", p -> new ItemNagaFang(p));
    public static final ItemNagaFangDagger NAGA_FANG_DAGGER = register("naga_fang_dagger", p -> new ItemNagaFangDagger(p));
    public static final ItemEarthrendGauntlet EARTHREND_GAUNTLET = register("earthrend_gauntlet", p -> new ItemEarthrendGauntlet(p.durability(ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.durabilityValue).rarity(Rarity.RARE)));
    public static final ItemSculptorStaff SCULPTOR_STAFF = register("sculptor_staff", p -> new ItemSculptorStaff(p.rarity(Rarity.RARE)));
    public static final ItemSandRake SAND_RAKE = register("sand_rake", p -> new ItemSandRake(p.durability(64)));
    public static final ItemGeomancerArmor GEOMANCER_BEADS = register("geomancer_beads", p -> new ItemGeomancerArmor(ArmorType.HELMET, p.rarity(Rarity.UNCOMMON)));
    public static final ItemGeomancerArmor GEOMANCER_ROBE = register("geomancer_robe", p -> new ItemGeomancerArmor(ArmorType.CHESTPLATE, p.rarity(Rarity.UNCOMMON)));
    public static final ItemGeomancerArmor GEOMANCER_BELT = register("geomancer_belt", p -> new ItemGeomancerArmor(ArmorType.LEGGINGS, p.rarity(Rarity.UNCOMMON)));
    public static final ItemGeomancerArmor GEOMANCER_SANDALS = register("geomancer_sandals", p -> new ItemGeomancerArmor(ArmorType.BOOTS, p.rarity(Rarity.UNCOMMON)));
    public static final ItemBluffRod BLUFF_ROD = register("bluff_rod", p -> new ItemBluffRod(p));
    public static final ItemElokosaPaw ELOKOSA_PAW_FULL = register("elokosa_paw_full", p -> new ItemElokosaPaw(PawType.FULL, p));
    public static final ItemElokosaPaw ELOKOSA_PAW_GIBBOUS = register("elokosa_paw_gibbous", p -> new ItemElokosaPaw(PawType.GIBBOUS, p));
    public static final ItemElokosaPaw ELOKOSA_PAW_HALF = register("elokosa_paw_half", p -> new ItemElokosaPaw(PawType.HALF, p));
    public static final ItemElokosaPaw ELOKOSA_PAW_CRESCENT = register("elokosa_paw_crescent", p -> new ItemElokosaPaw(PawType.CRESCENT, p));
    public static final ItemElokosaPaw ELOKOSA_PAW_NEW = register("elokosa_paw_new", p -> new ItemElokosaPaw(PawType.NEW, p));
    public static final ImmutableList<ItemElokosaPaw> ELOKOSA_PAWS = ImmutableList.of(
            ELOKOSA_PAW_FULL,
            ELOKOSA_PAW_GIBBOUS,
            ELOKOSA_PAW_HALF,
            ELOKOSA_PAW_CRESCENT,
            ELOKOSA_PAW_NEW
    );

    public static final Item LOGO = register("logo", p -> new Item(p));
    public static final Item PETIOLE_MUSIC_DISC = register("music_disc_petiole", p -> new Item(p.stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(MMSounds.JUKEBOX_PETIOLE)));

    public static final SpawnEggItem FOLIAATH_SPAWN_EGG = register("foliaath_spawn_egg", p -> new SpawnEggItem(p.spawnEgg(EntityHandler.FOLIAATH)));
    public static final SpawnEggItem WROUGHTNAUT_SPAWN_EGG = register("wroughtnaut_spawn_egg", p -> new SpawnEggItem(p.spawnEgg(EntityHandler.WROUGHTNAUT)));
    public static final SpawnEggItem UMVUTHANA_SPAWN_EGG = register("umvuthana_spawn_egg", p -> new SpawnEggItem(p.spawnEgg(EntityHandler.UMVUTHANA_MINION)));
    public static final SpawnEggItem UMVUTHANA_RAPTOR_SPAWN_EGG = register("umvuthana_raptor_spawn_egg", p -> new SpawnEggItem(p.spawnEgg(EntityHandler.UMVUTHANA_RAPTOR)));
    public static final SpawnEggItem UMVUTHANA_CRANE_SPAWN_EGG = register("umvuthana_crane_spawn_egg", p -> new SpawnEggItem(p.spawnEgg(EntityHandler.UMVUTHANA_CRANE)));
    public static final SpawnEggItem UMVUTHI_SPAWN_EGG = register("umvuthi_spawn_egg", p -> new SpawnEggItem(p.spawnEgg(EntityHandler.UMVUTHI)));
    public static final SpawnEggItem FROSTMAW_SPAWN_EGG = register("frostmaw_spawn_egg", p -> new SpawnEggItem(p.spawnEgg(EntityHandler.FROSTMAW)));
    public static final SpawnEggItem GROTTOL_SPAWN_EGG = register("grottol_spawn_egg", p -> new SpawnEggItem(p.spawnEgg(EntityHandler.GROTTOL)));
    public static final SpawnEggItem LANTERN_SPAWN_EGG = register("lantern_spawn_egg", p -> new SpawnEggItem(p.spawnEgg(EntityHandler.LANTERN)));
    public static final SpawnEggItem NAGA_SPAWN_EGG = register("naga_spawn_egg", p -> new SpawnEggItem(p.spawnEgg(EntityHandler.NAGA)));
    public static final SpawnEggItem SCULPTOR_SPAWN_EGG = register("sculptor_spawn_egg", p -> new SpawnEggItem(p.spawnEgg(EntityHandler.SCULPTOR)));
    public static final SpawnEggItem BLUFF_SPAWN_EGG = register("bluff_spawn_egg", p -> new SpawnEggItem(p.spawnEgg(EntityHandler.BLUFF)));
    public static final SpawnEggItem ELOKOSA_SPAWN_EGG = register("elokosa_spawn_egg", p -> new SpawnEggItem(p.spawnEgg(EntityHandler.ELOKOSA_FOLLOWER_TO_HOWLER)));
    public static final SpawnEggItem ELOKOSA_HOWLER_SPAWN_EGG = register("elokosa_howler_spawn_egg", p -> new SpawnEggItem(p.spawnEgg(EntityHandler.ELOKOSA_HOWLER)));


    /**
     * Adds a translated tooltip entry, splitting it into one line per {@code 
} (multi-line strings are no longer
     * wrapped by the tooltip renderer).
     */
    public static void addTooltip(Consumer<Component> tooltip, String translationKey) {
        String text = Component.translatable(translationKey).getString();
        for (String line : text.split("\n")) {
            if (!line.isEmpty()) {
                tooltip.accept(Component.literal(line).setStyle(TOOLTIP_STYLE));
            }
        }
    }

    private static <T extends Item> T register(String name, Function<Item.Properties, T> factory) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, MMCommon.resource(name));
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(new Item.Properties().setId(key)));
    }

    public static void register() {
        DefaultItemComponentEvents.MODIFY.register(ItemHandler::modifyComponents);
    }

    // TODO: Some tools missing
    private static void modifyComponents(DefaultItemComponentEvents.ModifyContext event) {
        if (!ConfigHandler.COMMON_CONFIG.isLoaded()) {
            // Should only be the case for data generation
            MMCommon.LOGGER.error("Could not modify default components due to config not being loaded yet");
            return;
        }

        event.modify(WROUGHT_AXE, builder -> {
            if (!ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.breakable.get()) {
                builder.set(DataComponents.MAX_DAMAGE, null);
                builder.set(DataComponents.DAMAGE, null);
            }

            builder.set(DataComponents.ATTRIBUTE_MODIFIERS, createToolAttributes(
                    ToolMaterial.IRON,
                    NEGATE_ATTACK_DAMAGE + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackDamage.get().floatValue(),
                    NEGATE_ATTACK_SPEED + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.AXE_OF_A_THOUSAND_METALS.toolConfig.attackSpeed.get().floatValue()
            ));
        });

        event.modify(SPEAR, builder -> {
            builder.set(DataComponents.ATTRIBUTE_MODIFIERS, createToolAttributes(
                    ToolMaterial.STONE,
                    NEGATE_ATTACK_DAMAGE + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig.attackDamage.get().floatValue(),
                    NEGATE_ATTACK_SPEED + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SPEAR.toolConfig.attackSpeed.get().floatValue()
            ).withModifierAdded(
                    Attributes.ENTITY_INTERACTION_RANGE,
                    new AttributeModifier(ItemSpear.SPEAR_REACH_ID, 1.5, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND
            ));
        });

        event.modify(NAGA_FANG_DAGGER, builder -> {
            builder.set(DataComponents.ATTRIBUTE_MODIFIERS, createToolAttributes(
                    ToolMaterial.STONE,
                    NEGATE_ATTACK_DAMAGE + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig.attackDamage.get().floatValue(),
                    NEGATE_ATTACK_SPEED + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.NAGA_FANG_DAGGER.toolConfig.attackSpeed.get().floatValue()
            ));
            // isValidRepairItem(ItemStack, ItemStack) no longer exists as an override point - repair-by-ItemNagaFang
            // is applied here instead, now that every item is guaranteed to be registered.
            builder.set(DataComponents.REPAIRABLE, new Repairable(HolderSet.direct(NAGA_FANG.builtInRegistryHolder())));
        });

        event.modify(EARTHREND_GAUNTLET, builder -> {
            // DiggerItem (via Properties#pickaxe) implicitly grants enchantability from the tool material - strip it
            // back off to match the old "isEnchantable() -> false" override, which no longer exists as an override point.
            builder.set(DataComponents.ENCHANTABLE, null);

            if (!ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.breakable.get()) {
                builder.set(DataComponents.MAX_DAMAGE, null);
                builder.set(DataComponents.DAMAGE, null);
            }

            builder.set(DataComponents.ATTRIBUTE_MODIFIERS, createToolAttributes(
                    ToolMaterial.STONE,
                    NEGATE_ATTACK_DAMAGE + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.toolConfig.attackDamage.get().floatValue(),
                    NEGATE_ATTACK_SPEED + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.EARTHREND_GAUNTLET.toolConfig.attackSpeed.get().floatValue()
            ));
        });

        event.modify(SOL_VISAGE, builder -> {
            if (!ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SOL_VISAGE.breakable.get()) {
                builder.set(DataComponents.MAX_DAMAGE, null);
                builder.set(DataComponents.DAMAGE, null);
                // isValidRepairItem(ItemStack, ItemStack) no longer exists as an override point - repair-only-while-
                // breakable is now expressed by stripping the REPAIRABLE component .humanoidArmor() implicitly added.
                builder.set(DataComponents.REPAIRABLE, null);
            }
        });

        event.modify(WROUGHT_HELMET, builder -> {
            if (!ConfigHandler.COMMON.TOOLS_AND_ABILITIES.WROUGHT_HELM.breakable.get()) {
                builder.set(DataComponents.MAX_DAMAGE, null);
                builder.set(DataComponents.DAMAGE, null);
                builder.set(DataComponents.REPAIRABLE, null);
            }
        });

        // Never repairable (old isValidRepairItem(ItemStack, ItemStack) override always returned false, which no
        // longer exists as an override point) - strip the placeholder REPAIRABLE component .humanoidArmor() added.
        for (ItemUmvuthanaMask mask : List.of(
                UMVUTHANA_MASK_FURY, UMVUTHANA_MASK_FEAR, UMVUTHANA_MASK_RAGE, UMVUTHANA_MASK_BLISS, UMVUTHANA_MASK_MISERY, UMVUTHANA_MASK_FAITH)) {
            event.modify(mask, builder -> builder.set(DataComponents.REPAIRABLE, null));
        }

        // Durability initially gets set in 'TieredItem' based on the tier
        event.modify(SCULPTOR_STAFF, builder -> {
            // DiggerItem (via Properties#hoe) implicitly grants enchantability from the tool material - strip it
            // back off to match the old "isEnchantable() -> false" override, which no longer exists as an override point.
            builder.set(DataComponents.ENCHANTABLE, null);

            builder.set(DataComponents.ATTRIBUTE_MODIFIERS, createToolAttributes(
                    ToolMaterial.STONE,
                    NEGATE_ATTACK_DAMAGE + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SCULPTOR_STAFF.toolConfig.attackDamage.get().floatValue(),
                    NEGATE_ATTACK_SPEED + ConfigHandler.COMMON.TOOLS_AND_ABILITIES.SCULPTOR_STAFF.toolConfig.attackSpeed.get().floatValue()
            ));
            builder.set(DataComponents.MAX_DAMAGE, 200);
            // isValidRepairItem(ItemStack, ItemStack) no longer exists as an override point - repair-by-BLUFF_ROD is
            // applied here instead, now that every item is guaranteed to be registered.
            builder.set(DataComponents.REPAIRABLE, new Repairable(HolderSet.direct(BLUFF_ROD.builtInRegistryHolder())));
        });

        // isValidRepairItem(ItemStack, ItemStack) no longer exists as an override point - repair-by-BLUFF_ROD is
        // applied here instead of in the constructor (overriding the placeholder tag-based repair component that
        // Properties#humanoidArmor sets), now that every item is guaranteed to be registered.
        for (ItemGeomancerArmor geomancerPiece : List.of(GEOMANCER_BEADS, GEOMANCER_ROBE, GEOMANCER_BELT, GEOMANCER_SANDALS)) {
            event.modify(geomancerPiece, builder ->
                    builder.set(DataComponents.REPAIRABLE, new Repairable(HolderSet.direct(BLUFF_ROD.builtInRegistryHolder()))));
        }

        for (ItemElokosaPaw item : ELOKOSA_PAWS) {
            event.modify(item, builder -> {
                builder.set(DataComponents.MAX_DAMAGE, ConfigHandler.COMMON.TOOLS_AND_ABILITIES.ELOKOSA_PAW.numberOfUses.getAsInt());
            });
        }
    }

    /**
     * Replacement for the old {@code DiggerItem.createAttributes}/{@code AxeItem.createAttributes} static helpers,
     * which no longer exist now that DiggerItem/PickaxeItem/SwordItem etc. were removed upstream in favor of plain
     * Item + ToolMaterial. Mirrors {@code ToolMaterial#createToolAttributes} (private in vanilla).
     */
    private static ItemAttributeModifiers createToolAttributes(ToolMaterial material, float attackDamageBaseline, float attackSpeedBaseline) {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, attackDamageBaseline + material.attackDamageBonus(), AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED,
                        new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, attackSpeedBaseline, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    public static void initializeDispenserBehaviors() {
        DispenserBlock.registerBehavior(DART, new ProjectileDispenseBehavior(DART));
    }
}