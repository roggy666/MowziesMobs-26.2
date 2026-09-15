package com.bobmowzie.mowziesmobs.client;

import com.bobmowzie.mowziesmobs.client.render.entity.FrozenRenderHandler;
import com.bobmowzie.mowziesmobs.client.render.entity.layer.SunblockLayer;
import com.bobmowzie.mowziesmobs.client.render.entity.player.GeckoPlayer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityRenderLayerRegistrationCallback;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;

public class ClientLayerRegistry {

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void register() {
        LivingEntityRenderLayerRegistrationCallback.EVENT.register((entityType, renderer, helper, context) -> {
            if (entityType == EntityTypes.ENDER_DRAGON) return;
            helper.register((RenderLayer) new FrozenRenderHandler.LayerFrozen(renderer));
            helper.register((RenderLayer) new SunblockLayer(renderer));
            if (entityType == EntityTypes.PLAYER) {
                GeckoPlayer.GeckoPlayerThirdPerson.initRenderer(context);
            }
        });
    }
}
