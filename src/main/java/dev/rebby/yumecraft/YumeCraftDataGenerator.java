package dev.rebby.yumecraft;

import dev.rebby.yumecraft.datagen.ModBlockLootTableProvider;
import dev.rebby.yumecraft.datagen.ModBlockTagProvider;
import dev.rebby.yumecraft.datagen.ModItemTagProvider;
import dev.rebby.yumecraft.datagen.ModModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class YumeCraftDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();


		pack.addProvider(ModBlockLootTableProvider::new);
		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModModelProvider::new);
	}
}
