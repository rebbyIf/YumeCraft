package dev.rebby.yumecraft;

import dev.rebby.yumecraft.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.world.gen.structure.Structure;

public class YumeCraftDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();


		pack.addProvider(ModBlockLootTableProvider::new);
		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModStructureTagProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);
	}
}
