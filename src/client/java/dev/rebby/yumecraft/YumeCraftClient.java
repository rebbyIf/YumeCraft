package dev.rebby.yumecraft;

import dev.rebby.yumecraft.particles.ClientSideParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.*;
import net.minecraft.world.gen.foliage.CherryFoliagePlacer;

public class YumeCraftClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ClientSideParticles.register();
	}
}