package dev.rebby.yumecraft.particle;

import dev.rebby.yumecraft.YumeCraft;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {

    public static final SimpleParticleType LINGERING_BUBBLE = FabricParticleTypes.simple();

    public static void init(){
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(YumeCraft.MOD_ID, "lingering_bubble"), LINGERING_BUBBLE);

        YumeCraft.LOGGER.info("Registering particles for " + YumeCraft.MOD_ID);
    }

}
