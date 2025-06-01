package dev.rebby.yumecraft;

import dev.rebby.yumecraft.block.entity.ModBlockEntityTypes;
import dev.rebby.yumecraft.block.ModBlocks;
import dev.rebby.yumecraft.items.ModItemGroups;
import dev.rebby.yumecraft.items.ModItems;
import dev.rebby.yumecraft.particle.ModParticles;
import dev.rebby.yumecraft.sound.ModSounds;
import dev.rebby.yumecraft.util.DimensionalTeleportationHandler;
import dev.rebby.yumecraft.util.LoadingWorldHandler;
import dev.rebby.yumecraft.world.gen.chunk_generator.FractalChunkGenerator;
import dev.rebby.yumecraft.world.gen.chunk_generator.ModChunkGenerators;
import dev.rebby.yumecraft.world.gen.density_function.ModDensityFunctions;
import dev.rebby.yumecraft.world.gen.fractal.FractalTypes;
import dev.rebby.yumecraft.world.gen.structure.InfiniteStructureTypes;
import net.fabricmc.api.ModInitializer;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YumeCraft implements ModInitializer {
	public static final String MOD_ID = "yumecraft";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LoadingWorldHandler.init();
		DimensionalTeleportationHandler.init();

		InfiniteStructureTypes.init();
		FractalTypes.init();
		ModDensityFunctions.init();
		ModChunkGenerators.register();
		ModItems.init();
		ModBlocks.init();
		ModItemGroups.init();
		ModBlockEntityTypes.init();
		ModParticles.init();
		ModSounds.init();
		LOGGER.info("Hello Fabric world!");
	}

	public static Identifier id(String name) {
		return Identifier.of(MOD_ID, name);
	}
}