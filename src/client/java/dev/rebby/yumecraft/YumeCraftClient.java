package dev.rebby.yumecraft;

import dev.rebby.yumecraft.particles.ClientSideParticles;
import dev.rebby.yumecraft.sound.SoundsHandler;
import net.fabricmc.api.ClientModInitializer;

public class YumeCraftClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		SoundsHandler.register();
		ClientSideParticles.register();

	}
}