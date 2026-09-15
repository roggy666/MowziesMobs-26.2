package com.bobmowzie.mowziesmobs.mixin.client;

import com.bobmowzie.mowziesmobs.client.ClientEventHandler;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {
    @Inject(method = "getFieldOfViewModifier", at = @At("RETURN"), cancellable = true)
    private void mowziesmobs$modifyFov(boolean firstPerson, float effectScale, CallbackInfoReturnable<Float> cir) {
        float modified = ClientEventHandler.updateFOV((Player) (Object) this, cir.getReturnValueF());
        if (modified != cir.getReturnValueF()) {
            cir.setReturnValue(modified);
        }
    }
}
