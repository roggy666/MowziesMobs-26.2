package com.bobmowzie.mowziesmobs.mixin;

import com.bobmowzie.mowziesmobs.server.ServerEventHandler;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "startRiding(Lnet/minecraft/world/entity/Entity;ZZ)Z", at = @At("HEAD"), cancellable = true)
    private void mowziesmobs$onStartRiding(Entity vehicle, boolean force, boolean sendEventAndTriggers, CallbackInfoReturnable<Boolean> cir) {
        if (ServerEventHandler.onRideEntity((Entity) (Object) this, vehicle)) {
            cir.setReturnValue(false);
        }
    }
}
