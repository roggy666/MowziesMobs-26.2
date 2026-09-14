package net.neoforged.neoforge.client.event;

import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelType;
import net.neoforged.bus.api.Event;

import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

public class EntityRenderersEvent extends Event {
    public static class AddLayers extends EntityRenderersEvent {
        private final EntityRendererProvider.Context context;
        private final java.util.Map<EntityType<?>, EntityRenderer<?, ?>> renderers;
        private final java.util.Map<PlayerModelType, ? extends LivingEntityRenderer<?, ?, ?>> playerRenderers;

        public AddLayers(EntityRendererProvider.Context context) {
            this(context, null, null);
        }

        public AddLayers(EntityRendererProvider.Context context, java.util.Map<EntityType<?>, EntityRenderer<?, ?>> renderers, java.util.Map<PlayerModelType, ? extends LivingEntityRenderer<?, ?, ?>> playerRenderers) {
            this.context = context;
            this.renderers = renderers;
            this.playerRenderers = playerRenderers;
        }

        public Set<PlayerModelType> getSkins() {
            return playerRenderers != null ? playerRenderers.keySet() : Collections.emptySet();
        }

        public LivingEntityRenderer<?, ?, ?> getPlayerRenderer(PlayerModelType skin) {
            return playerRenderers != null ? playerRenderers.get(skin) : null;
        }

        public EntityRenderer<?, ?> getRenderer(EntityType<? extends LivingEntity> entityType) {
            return renderers != null ? renderers.get(entityType) : null;
        }

        public EntityRendererProvider.Context getContext() {
            return context;
        }
    }

    public static class RegisterLayerDefinitions extends EntityRenderersEvent {
        public void registerLayerDefinition(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier) {
            ModelLayerRegistry.registerModelLayer(layerLocation, supplier::get);
        }
    }
}
