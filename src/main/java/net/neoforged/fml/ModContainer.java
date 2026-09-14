package net.neoforged.fml;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.config.IConfigSpec;

public class ModContainer {
    private final String modId;

    public ModContainer(String modId) {
        this.modId = modId;
    }

    public String getModId() {
        return modId;
    }

    public void registerConfig(ModConfig.Type type, IConfigSpec spec) {
        ConfigRegistry.INSTANCE.register(modId, type, (net.neoforged.fml.config.IConfigSpec) spec);
    }
}
