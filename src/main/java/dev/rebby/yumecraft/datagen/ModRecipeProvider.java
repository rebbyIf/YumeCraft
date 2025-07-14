package dev.rebby.yumecraft.datagen;

import dev.rebby.yumecraft.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        offer2x2CompactingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WHITEBRICK, Blocks.CALCITE);
        createStairsRecipe(ModBlocks.WHITEBRICK_STAIRS, Ingredient.ofItems(ModBlocks.WHITEBRICK))
                .criterion(hasItem(ModBlocks.WHITEBRICK), conditionsFromItem(ModBlocks.WHITEBRICK))
                .offerTo(recipeExporter);
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WHITEBRICK_SLAB, ModBlocks.WHITEBRICK);
        offerWallRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.WHITEBRICK_WALL, ModBlocks.WHITEBRICK);

        offerPolishedStoneRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_WHITEBRICK, ModBlocks.WHITEBRICK);
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_WHITEBRICK_SLAB, ModBlocks.CHISELED_WHITEBRICK);
        offerWallRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_WHITEBRICK_WALL, ModBlocks.CHISELED_WHITEBRICK);

        /*
         * Concrete Alt Blocks
         */

        // Blue
        createStairsRecipe(ModBlocks.BLUE_CONCRETE_STAIRS, Ingredient.ofItems(Blocks.BLUE_CONCRETE))
                .criterion(hasItem(Blocks.BLUE_CONCRETE), conditionsFromItem(Blocks.BLUE_CONCRETE))
                .offerTo(recipeExporter);
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLUE_CONCRETE_SLABS, Blocks.BLUE_CONCRETE);
    }
}
