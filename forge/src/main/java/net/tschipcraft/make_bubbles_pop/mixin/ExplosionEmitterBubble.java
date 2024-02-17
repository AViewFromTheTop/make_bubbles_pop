package net.tschipcraft.make_bubbles_pop.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.HugeExplosionSeedParticle;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPop;
import net.tschipcraft.make_bubbles_pop.MakeBubblesPopConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin injects into the ExplosionEmitter particle class to add bubbles to explosions underwater.
 */
@Mixin(HugeExplosionSeedParticle.class)
public abstract class ExplosionEmitterBubble extends NoRenderParticle {

    protected ExplosionEmitterBubble(ClientLevel clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", shift = At.Shift.BEFORE))
    protected void injectBubbleParticle(CallbackInfo info) {
        if (!MakeBubblesPop.MIDNIGHTLIB_INSTALLED || MakeBubblesPopConfig.EXPLOSION_BUBBLES_ENABLED) {
            // Add bubble particles to explosions
            if (this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).is(FluidTags.WATER)) {
                for (int i = 0; i < 2; i++) {
                    double dx = (this.random.nextDouble() - this.random.nextDouble());
                    double dy = (this.random.nextDouble() - this.random.nextDouble());
                    double dz = (this.random.nextDouble() - this.random.nextDouble());
                    double x = this.x + dx * 3.5;
                    double y = this.y + dy * 3.5;
                    double z = this.z + dz * 3.5;
                    this.level.addParticle(ParticleTypes.BUBBLE, x, y, z, dx * 3, dy * 3, dz * 3);
                }
            }
        }
    }
}
