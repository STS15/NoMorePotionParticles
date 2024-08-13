package com.sts15.nomorepotionparticles.mixins;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleEngine.class)
public class MixinParticleEngine {


    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private void onAddParticle(Particle particle, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.getMainCamera();

        if (particle.getRenderType() == ParticleTypes.EFFECT || particle instanceof SpellParticle) {
            if (minecraft.options.getCameraType().isFirstPerson()) {
                double distanceSquared = camera.getPosition().distanceToSqr(particle.getPos().x, particle.getPos().y, particle.getPos().z);

                // Define a threshold distance (e.g., 2 blocks = 4 units squared)
                double thresholdDistanceSquared = 4.0;
                if (distanceSquared < thresholdDistanceSquared) {
                    ci.cancel();
                }
            }
        }
    }

}
