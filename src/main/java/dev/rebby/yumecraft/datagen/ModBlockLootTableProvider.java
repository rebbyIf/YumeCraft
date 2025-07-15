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
        // Point Nemo
        addDrop(ModBlocks.BLUESTONE);
        addDrop(ModBlocks.BLUE_COBBLE);

        // Verdant Temple
        addDrop(ModBlocks.WHITEBRICK);
        addDrop(ModBlocks.WHITEBRICK_STAIRS);
        addDrop(ModBlocks.WHITEBRICK_WALL);
        addDrop(ModBlocks.CHISELED_WHITEBRICK);
        addDrop(ModBlocks.CHISELED_WHITEBRICK_WALL);

        addDrop(ModBlocks.WHITEBRICK_SLAB, slabDrops(ModBlocks.WHITEBRICK_SLAB));
        addDrop(ModBlocks.CHISELED_WHITEBRICK_SLAB, slabDrops(ModBlocks.CHISELED_WHITEBRICK_SLAB));

        // Copper Walls
        addDrop(ModBlocks.COPPER_WALL);
        addDrop(ModBlocks.EXPOSED_COPPER_WALL);
        addDrop(ModBlocks.WEATHERED_COPPER_WALL);
        addDrop(ModBlocks.OXIDIZED_COPPER_WALL);

        addDrop(ModBlocks.WAXED_COPPER_WALL);
        addDrop(ModBlocks.WAXED_EXPOSED_COPPER_WALL);
        addDrop(ModBlocks.WAXED_WEATHERED_COPPER_WALL);
        addDrop(ModBlocks.WAXED_OXIDIZED_COPPER_WALL);

        /*
         * Concrete Blocks
         */

        // BLUE
        addDrop(ModBlocks.BLUE_CONCRETE_STAIRS);
        addDrop(ModBlocks.BLUE_CONCRETE_SLABS);
        addDrop(ModBlocks.BLUE_CONCRETE_SLABS, slabDrops(ModBlocks.BLUE_CONCRETE_SLABS));
        addDrop(ModBlocks.BLUE_CONCRETE_EDGE);
    }
}
