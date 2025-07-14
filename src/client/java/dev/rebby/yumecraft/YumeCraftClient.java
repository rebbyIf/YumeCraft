package dev.rebby.yumecraft;

import dev.rebby.yumecraft.block.ModBlocks;
import dev.rebby.yumecraft.particles.ClientSideParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.minecraft.client.render.RenderLayer;

public class YumeCraftClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		//SoundsHandler.register();

		ClientSideParticles.register();

		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(),
				ModBlocks.BLUE_CONCRETE_EDGE,
				ModBlocks.WHITE_CONCRETE_EDGE
		);

	}

	private static void registerClientListeners(){

	}
}