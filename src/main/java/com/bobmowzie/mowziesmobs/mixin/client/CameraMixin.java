package com.bobmowzie.mowziesmobs.mixin.client;

import com.bobmowzie.mowziesmobs.client.ClientEventHandler;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public abstract class CameraMixin {
    // Camera shake: offset the base rotation taken from the camera entity (the two setRotation calls at the top of
    // alignWithEntity, before any third person adjustments)
    @ModifyArgs(method = "alignWithEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V", ordinal = 0))
    private void mowziesmobs$shakeCameraMinecart(Args args) {
        mowziesmobs$applyShake(args);
    }

    @ModifyArgs(method = "alignWithEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V", ordinal = 1))
    private void mowziesmobs$shakeCamera(Args args) {
        mowziesmobs$applyShake(args);
    }

    private static void mowziesmobs$applyShake(Args args) {
        float[] shake = ClientEventHandler.getCameraShake();
        if (shake == null) return;
        args.set(0, (float) args.get(0) + shake[0]);
        args.set(1, (float) args.get(1) + shake[1]);
    }
}
