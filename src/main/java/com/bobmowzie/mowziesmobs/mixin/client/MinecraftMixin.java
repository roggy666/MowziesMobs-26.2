package com.bobmowzie.mowziesmobs.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow @Final public Options options;

    @Inject(method = "startAttack", at = @At("HEAD"))
    private void mm$onStartAttack(CallbackInfoReturnable<Boolean> cir) {
        NeoForge.EVENT_BUS.post(new InputEvent.InteractionKeyMappingTriggered(this.options.keyAttack));
    }

    @Inject(method = "startUseItem", at = @At("HEAD"))
    private void mm$onStartUseItem(CallbackInfo ci) {
        NeoForge.EVENT_BUS.post(new InputEvent.InteractionKeyMappingTriggered(this.options.keyUse));
    }
}
