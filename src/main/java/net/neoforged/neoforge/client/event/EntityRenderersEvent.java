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

        public AddLayers(EntityRendererProvider.Context context) {
            this.context = context;
        }

        public Set<PlayerModelType> getSkins() {
            return Collections.emptySet();
        }

        public LivingEntityRenderer<?, ?, ?> getPlayerRenderer(PlayerModelType skin) {
            return null;
        }

        public EntityRenderer<?, ?> getRenderer(EntityType<? extends LivingEntity> entityType) {
            return null;
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
