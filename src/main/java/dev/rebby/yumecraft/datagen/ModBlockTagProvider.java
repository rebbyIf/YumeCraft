package dev.rebby.yumecraft.datagen;

import dev.rebby.yumecraft.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)

                // POINT NEMO BLOCKS
                .add(ModBlocks.BLUESTONE)
                .add(ModBlocks.BLUE_COBBLE)
                .add(ModBlocks.BUBBLING_MAGMA_BLOCK)

                //WHITEBRICK
                .add(ModBlocks.CHISELED_WHITEBRICK)
                .add(ModBlocks.WHITEBRICK)
                .add(ModBlocks.CHISELED_WHITEBRICK_WALL)
                .add(ModBlocks.WHITEBRICK_WALL)
                .add(ModBlocks.WHITEBRICK_SLAB)
                .add(ModBlocks.CHISELED_WHITEBRICK_SLAB)
                .add(ModBlocks.WHITEBRICK_STAIRS)

                // REINFORCED IRON
                .add(ModBlocks.REINFORCED_IRON)
                .add(ModBlocks.REINFORCED_IRON_WALL)

                // COPPER WALLS
                .add(ModBlocks.COPPER_WALL)
                .add(ModBlocks.EXPOSED_COPPER_WALL)
                .add(ModBlocks.WEATHERED_COPPER_WALL)
                .add(ModBlocks.OXIDIZED_COPPER_WALL)
                .add(ModBlocks.WAXED_COPPER_WALL)
                .add(ModBlocks.WAXED_EXPOSED_COPPER_WALL)
                .add(ModBlocks.WAXED_WEATHERED_COPPER_WALL)
                .add(ModBlocks.WAXED_OXIDIZED_COPPER_WALL)

                // CONCRETE ALT
                // BLUE
                .add(ModBlocks.BLUE_CONCRETE_SLABS)
                .add(ModBlocks.BLUE_CONCRETE_STAIRS)
                .add(ModBlocks.BLUE_CONCRETE_EDGE);

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)

                // POINT NEMO
                .add(ModBlocks.BLUESTONE)

                // WHITEBRICK
                .add(ModBlocks.CHISELED_WHITEBRICK)
                .add(ModBlocks.WHITEBRICK)
                .add(ModBlocks.CHISELED_WHITEBRICK_WALL)
                .add(ModBlocks.WHITEBRICK_WALL)
                .add(ModBlocks.WHITEBRICK_SLAB)
                .add(ModBlocks.CHISELED_WHITEBRICK_SLAB)
                .add(ModBlocks.WHITEBRICK_STAIRS)

                // CONCRETE ALT
                // BLUE
                .add(ModBlocks.BLUE_CONCRETE_SLABS)
                .add(ModBlocks.BLUE_CONCRETE_STAIRS)
                .add(ModBlocks.BLUE_CONCRETE_EDGE);

        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.BLUE_COBBLE);

        getOrCreateTagBuilder(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.REINFORCED_IRON)
                .add(ModBlocks.REINFORCED_IRON_WALL);

        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(ModBlocks.WHITEBRICK_WALL)
                .add(ModBlocks.CHISELED_WHITEBRICK_WALL)
                .add(ModBlocks.REINFORCED_IRON_WALL)

                // COPPER WALLS
                .add(ModBlocks.COPPER_WALL)
                .add(ModBlocks.EXPOSED_COPPER_WALL)
                .add(ModBlocks.WEATHERED_COPPER_WALL)
                .add(ModBlocks.OXIDIZED_COPPER_WALL)
                .add(ModBlocks.WAXED_COPPER_WALL)
                .add(ModBlocks.WAXED_EXPOSED_COPPER_WALL)
                .add(ModBlocks.WAXED_WEATHERED_COPPER_WALL)
                .add(ModBlocks.WAXED_OXIDIZED_COPPER_WALL);

    }
}
