package dev.rebby.yumecraft.datagen;

import dev.rebby.yumecraft.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    public ModBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.BLUESTONE);
        addDrop(ModBlocks.BLUE_COBBLE);
        addDrop(ModBlocks.WHITEBRICK);
        addDrop(ModBlocks.WHITEBRICK_STAIRS);
        addDrop(ModBlocks.WHITEBRICK_WALL);
        addDrop(ModBlocks.CHISELED_WHITEBRICK);
        addDrop(ModBlocks.CHISELED_WHITEBRICK_WALL);

        addDrop(ModBlocks.WHITEBRICK_SLAB, slabDrops(ModBlocks.WHITEBRICK_SLAB));
        addDrop(ModBlocks.CHISELED_WHITEBRICK_SLAB, slabDrops(ModBlocks.CHISELED_WHITEBRICK_SLAB));
    }
}
