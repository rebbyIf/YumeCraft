package dev.rebby.yumecraft.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.AscendingParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class LingeringBubbleParticle extends AscendingParticle {
    protected LingeringBubbleParticle(
            ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ,
            float scaleMultiplier, SpriteProvider spriteProvider
    ) {
        super (world, x, y, z, 0.2f, 0.0f, 0.2f,
                velocityX, velocityY, velocityZ, scaleMultiplier, spriteProvider, 1.0f,
                30, 0.01f, true);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new LingeringBubbleParticle(world, x, y, z, 0.0f, 0.4f, 0.4f, 1.0f, this.spriteProvider);
        }
    }
}
