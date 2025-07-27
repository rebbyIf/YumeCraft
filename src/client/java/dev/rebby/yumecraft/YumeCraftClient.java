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
				ModBlocks.WHITE_CONCRETE_EDGE,
				ModBlocks.LIGHT_GRAY_CONCRETE_EDGE,
				ModBlocks.GRAY_CONCRETE_EDGE,
				ModBlocks.BLACK_CONCRETE_EDGE,
				ModBlocks.ORANGE_CONCRETE_EDGE,
				ModBlocks.MAGENTA_CONCRETE_EDGE,
				ModBlocks.LIGHT_BLUE_CONCRETE_EDGE,
				ModBlocks.YELLOW_CONCRETE_EDGE,
				ModBlocks.LIME_CONCRETE_EDGE,
				ModBlocks.PINK_CONCRETE_EDGE,
				ModBlocks.CYAN_CONCRETE_EDGE,
				ModBlocks.PURPLE_CONCRETE_EDGE,
				ModBlocks.BLUE_CONCRETE_EDGE,
				ModBlocks.BROWN_CONCRETE_EDGE,
				ModBlocks.GREEN_CONCRETE_EDGE,
				ModBlocks.RED_CONCRETE_EDGE
		);

	}

	private static void registerClientListeners(){

	}
}