package com.bobmowzie.mowziesmobs.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
    @Inject(method = "addEntity", at = @At("TAIL"))
    private void mm$onAddEntity(Entity entity, CallbackInfo ci) {
        NeoForge.EVENT_BUS.post(new EntityJoinLevelEvent(entity, (ClientLevel) (Object) this));
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void mm$onClientLevelTick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        NeoForge.EVENT_BUS.post(new LevelTickEvent.Post((ClientLevel) (Object) this));
    }
}
