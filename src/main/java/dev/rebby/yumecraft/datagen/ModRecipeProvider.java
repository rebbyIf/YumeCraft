package dev.rebby.yumecraft.datagen;

import com.google.common.collect.ImmutableList;
import dev.rebby.yumecraft.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    private static final ImmutableList<ItemConvertible> REINFORCED_IRON_SOURCES = ImmutableList.of(Items.RAW_IRON_BLOCK, Items.IRON_BLOCK);

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

        // Reinforced Iron
        offerSmelting(recipeExporter, REINFORCED_IRON_SOURCES, RecipeCategory.BUILDING_BLOCKS, ModBlocks.REINFORCED_IRON, 1.0f, 400, "reinforced_iron");
        offerWallRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.REINFORCED_IRON_WALL, ModBlocks.REINFORCED_IRON);

        // Copper Walls
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.COPPER_WALL, Blocks.COPPER_BLOCK);

        /*
         * Concrete Alt Blocks
         */

        // BLUE
        createStairsRecipe(ModBlocks.BLUE_CONCRETE_STAIRS, Ingredient.ofItems(Blocks.BLUE_CONCRETE))
                .criterion(hasItem(Blocks.BLUE_CONCRETE), conditionsFromItem(Blocks.BLUE_CONCRETE))
                .offerTo(recipeExporter);
        offerSlabRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLUE_CONCRETE_SLABS, Blocks.BLUE_CONCRETE);
        offer2x2CompactingRecipe(recipeExporter, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLUE_CONCRETE_EDGE, Blocks.BLUE_CONCRETE);
    }
}
