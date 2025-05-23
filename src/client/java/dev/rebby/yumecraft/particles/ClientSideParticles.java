package dev.rebby.yumecraft.particles;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class ClientSideParticles {

    public static void register(){
        ParticleFactoryRegistry.getInstance().register(dev.rebby.yumecraft.particle.ModParticles.LINGERING_BUBBLE, LingeringBubbleParticle.Factory::new);
    }
}
