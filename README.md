# Mowzie's Mobs — Fabric 26.2 (Unofficial Port)

> [!NOTE]
> **Official Release & Credits:**
> This is an unofficial port. The original mod is created and maintained by **BobMowzie** and the Mowzie's Mobs team.  
> Official Release: [CurseForge](https://www.curseforge.com/minecraft/mc-mods/mowzies-mobs) | Source: [GitHub (BobMowzie/MowziesMobs-Public)](https://github.com/BobMowzie/MowziesMobs-Public)

---

## Overview

This repository is an unofficial port of **Mowzie's Mobs** to **Minecraft 26.2** running natively on the **Fabric Loader** and **Fabric API**.

### System & Dependency Requirements
- **Minecraft:** `26.2`
- **Java:** `25`
- **Fabric Loader:** `>= 0.16.0`
- **Fabric API:** `0.160.0+26.2`
- **GeckoLib:** `5.5.5`
- **Forge Config API Port (Fabric):** `>= 26.2.0`

---

## What Was Done in This Port

### 1. Pure Fabric API Migration
- Fully transitioned from NeoForge shim compatibility layers directly to standard **Fabric API**.
- Network architecture ported to Fabric Networking (`ServerPlayNetworking`, `ClientPlayNetworking`, `PayloadTypeRegistry`, `CustomPacketPayload`).
- Lifecycle, event hooks, entity attribute registration, and menu screens ported to standard Fabric entrypoints and callbacks.

### 2. Fabric Data Generation (`fabric-datagen`)
- Configured Fabric Data Generation entrypoint (`DataGenerators.java`).
- Full datagen support for block tags, item tags, entity type tags, biome tags, and recipes.
- Dynamic registry generation for structures and structure sets (`Wroughtnaut Chamber`, `Frostmaw`, `Umvuthana Grove`, `Monastery`).

### 3. Rendering Pipeline & GeckoLib 5.5.5
- Updated entity and block entity renderers to Fabric `EntityRendererRegistry` and `BlockEntityRendererFactories`.
- Model layers registered via Fabric `ModelLayerRegistry`.
- Implemented GeckoLib 5.5.5 armor rendering layer (`GeckoPlayerArmorLayer`) with proper layer colors and support for GeckoLib animatable mobs (`IS_GECKOLIB_WEARER`).
- Updated culling logic (`affectedByCulling`, `getBoundingBoxForCulling`) according to Minecraft 26.2 entity renderer standards.
- Custom particle shaders and terrain particles ported to 26.2 `SingleQuadParticle.Layer`.

### 4. Minecraft 26.2 Item Definitions & Models
- Fully updated client item definitions under `assets/mowziesmobs/items/*.json` adhering to 26.2 specification.
- Converted conditional overrides (such as `blowgun` using `minecraft:using_item` and lunar phases for `elokosa_paw_*`) to modern component/item definition syntax.

### 5. Entities, Bosses & Gameplay
- Boss bars ported to Minecraft 26.2 `ServerBossEvent` with dynamic screen darkening and color handling.
- Full parity for mobs and bosses: Ferrous Wroughtnaut, Frostmaw, Umvuthi the Sunbird, Naga, Foliaath, Barakoa/Umvuthana tribes, Grottol, Lantern, and Bluff.
- Celestial / time-of-day logic and physics impulse synchronization updated to 26.2 standards (`level.isBrightOutside()`, `needsSync`).

### 6. Localization
- 100% key coverage for English (`en_us.json`) and Russian (`ru_ru.json`), including all technical entities, projectiles, effects, and item groups.

---

## Building from Source

To build the mod jar:

```bash
./gradlew build
```

Generated mod artifacts will be located in `build/libs/`.

---

## License & Attribution

- Original mod, art, models, animations, and sound effects are intellectual property of **BobMowzie** and the Mowzie's Mobs team.
- Port to Minecraft 26.2 Fabric maintained by **roggy666**, **Claude**, and **Gemini**.
