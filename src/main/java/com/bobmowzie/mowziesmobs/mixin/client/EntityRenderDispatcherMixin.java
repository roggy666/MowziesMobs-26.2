package com.bobmowzie.mowziesmobs.mixin.client;

import com.bobmowzie.mowziesmobs.client.ClientLayerRegistry;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MapRenderer;
import net.minecraft.client.renderer.PlayerSkinRenderCache;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.sprite.AtlasManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.PlayerModelType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.function.Supplier;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Shadow private Map<EntityType<?>, EntityRenderer<?, ?>> renderers;
    @Shadow private Map<PlayerModelType, AvatarRenderer<AbstractClientPlayer>> playerRenderers;
    @Shadow @Final private BlockModelResolver blockModelResolver;
    @Shadow @Final private ItemModelResolver itemModelResolver;
    @Shadow @Final private MapRenderer mapRenderer;
    @Shadow @Final private Supplier<EntityModelSet> entityModels;
    @Shadow @Final private EquipmentAssetManager equipmentAssets;
    @Shadow @Final private AtlasManager atlasManager;
    @Shadow @Final private Font font;
    @Shadow @Final private PlayerSkinRenderCache playerSkinRenderCache;

    @Inject(method = "onResourceManagerReload", at = @At("TAIL"))
    private void mm$onResourceManagerReload(ResourceManager resourceManager, CallbackInfo ci) {
        EntityRendererProvider.Context context = new EntityRendererProvider.Context(
            (EntityRenderDispatcher) (Object) this,
            this.blockModelResolver,
            this.itemModelResolver,
            this.mapRenderer,
            resourceManager,
            this.entityModels.get(),
            this.equipmentAssets,
            this.atlasManager,
            this.font,
            this.playerSkinRenderCache
        );
        EntityRenderersEvent.AddLayers addLayersEvent = new EntityRenderersEvent.AddLayers(context, this.renderers, this.playerRenderers);
        ClientLayerRegistry.onAddLayers(addLayersEvent);
    }
}
