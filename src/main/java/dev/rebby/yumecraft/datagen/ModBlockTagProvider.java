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
                .add(ModBlocks.BLUESTONE)
                .add(ModBlocks.BLUE_COBBLE)
                .add(ModBlocks.BUBBLING_MAGMA_BLOCK)
                .add(ModBlocks.CHISELED_WHITEBRICK)
                .add(ModBlocks.WHITEBRICK)
                .add(ModBlocks.CHISELED_WHITEBRICK_WALL)
                .add(ModBlocks.WHITEBRICK_WALL)
                .add(ModBlocks.WHITEBRICK_SLAB)
                .add(ModBlocks.CHISELED_WHITEBRICK_SLAB)
                .add(ModBlocks.WHITEBRICK_STAIRS);

        getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.BLUESTONE)
                .add(ModBlocks.CHISELED_WHITEBRICK)
                .add(ModBlocks.WHITEBRICK)
                .add(ModBlocks.CHISELED_WHITEBRICK_WALL)
                .add(ModBlocks.WHITEBRICK_WALL)
                .add(ModBlocks.WHITEBRICK_SLAB)
                .add(ModBlocks.CHISELED_WHITEBRICK_SLAB)
                .add(ModBlocks.WHITEBRICK_STAIRS);

        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.BLUE_COBBLE);

        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(ModBlocks.WHITEBRICK_WALL)
                .add(ModBlocks.CHISELED_WHITEBRICK_WALL);

    }
}
