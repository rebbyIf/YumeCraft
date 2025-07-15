package dev.rebby.yumecraft.datagen;

import dev.rebby.yumecraft.YumeCraft;
import dev.rebby.yumecraft.block.EdgeBlock;
import dev.rebby.yumecraft.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {

    public static final Model EDGE = new Model(
            Optional.of(YumeCraft.id("block/edge")),
            Optional.empty(),
            TextureKey.TEXTURE
    );
    public static final Model EDGE_90 = new Model(
            Optional.of(YumeCraft.id("block/edge_90")),
            Optional.empty(),
            TextureKey.TEXTURE
    );
    public static final Model EDGE_180 = new Model(
            Optional.of(YumeCraft.id("block/edge_180")),
            Optional.empty(),
            TextureKey.TEXTURE
    );
    public static final Model EDGE_270 = new Model(
            Optional.of(YumeCraft.id("block/edge_270")),
            Optional.empty(),
            TextureKey.TEXTURE
    );

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // Point Nemo Blocks

        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.BLUESTONE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.BLUE_COBBLE);

        final Identifier bubblingMagmaModelId = Models.CUBE_ALL.upload(ModBlocks.BUBBLING_MAGMA_BLOCK,
                TextureMap.all(Identifier.ofVanilla("block/magma")), blockStateModelGenerator.modelCollector);

        blockStateModelGenerator.registerParentedItemModel(ModBlocks.BUBBLING_MAGMA_BLOCK, bubblingMagmaModelId);
        blockStateModelGenerator.registerSimpleState(ModBlocks.BUBBLING_MAGMA_BLOCK);

        // Verdant Temple Blocks

        BlockStateModelGenerator.BlockTexturePool whitebrickPool =
                blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.WHITEBRICK);

        whitebrickPool.stairs(ModBlocks.WHITEBRICK_STAIRS);
        whitebrickPool.slab(ModBlocks.WHITEBRICK_SLAB);
        whitebrickPool.wall(ModBlocks.WHITEBRICK_WALL);

        BlockStateModelGenerator.BlockTexturePool chiseledWhitebrickPool =
                blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.CHISELED_WHITEBRICK);

        chiseledWhitebrickPool.slab(ModBlocks.CHISELED_WHITEBRICK_SLAB);
        chiseledWhitebrickPool.wall(ModBlocks.CHISELED_WHITEBRICK_WALL);

        // Copper Walls

        final TextureMap copperBlockTexture = TextureMap.all(Identifier.ofVanilla("block/copper_block"));
        final TextureMap exposedCopperTexture = TextureMap.all(Identifier.ofVanilla("block/exposed_copper"));
        final TextureMap weatheredCopperTexture = TextureMap.all(Identifier.ofVanilla("block/weathered_copper"));
        final TextureMap oxidizedCopperTexture = TextureMap.all(Identifier.ofVanilla("block/oxidized_copper"));

        registerVanillaWallVariant(copperBlockTexture, ModBlocks.COPPER_WALL, blockStateModelGenerator);
        registerVanillaWallVariant(copperBlockTexture, ModBlocks.WAXED_COPPER_WALL, blockStateModelGenerator);

        registerVanillaWallVariant(exposedCopperTexture, ModBlocks.EXPOSED_COPPER_WALL, blockStateModelGenerator);
        registerVanillaWallVariant(exposedCopperTexture, ModBlocks.WAXED_EXPOSED_COPPER_WALL, blockStateModelGenerator);

        registerVanillaWallVariant(weatheredCopperTexture, ModBlocks.WEATHERED_COPPER_WALL, blockStateModelGenerator);
        registerVanillaWallVariant(weatheredCopperTexture, ModBlocks.WAXED_WEATHERED_COPPER_WALL, blockStateModelGenerator);

        registerVanillaWallVariant(oxidizedCopperTexture, ModBlocks.OXIDIZED_COPPER_WALL, blockStateModelGenerator);
        registerVanillaWallVariant(oxidizedCopperTexture, ModBlocks.WAXED_OXIDIZED_COPPER_WALL, blockStateModelGenerator);

        /*
         * List for all Concrete variants
         */

        // For white, light gray, & gray
        Identifier [] blackEdgeModels = registerOuterEdgeModel("black", blockStateModelGenerator);
        // For brown & black
        Identifier [] whiteEdgeModels = registerOuterEdgeModel("white", blockStateModelGenerator);
        // For light blue, cyan, & blue
        Identifier [] blueEdgeModels = registerOuterEdgeModel("blue", blockStateModelGenerator);
        // For orange, yellow & red
        Identifier [] redEdgeModels = registerOuterEdgeModel("red", blockStateModelGenerator);
        // For magenta, pink, & purple
        Identifier [] purpleEdgeModels = registerOuterEdgeModel("purple", blockStateModelGenerator);
        // For lime & green
        Identifier [] greenEdgeModels = registerOuterEdgeModel("green", blockStateModelGenerator);

        // White
        Identifier whiteConcreteId = Identifier.ofVanilla("block/white_concrete");
        final TextureMap whiteConcreteTexture = TextureMap.all(whiteConcreteId);
        registerVanillaStairsVariant(whiteConcreteTexture, ModBlocks.WHITE_CONCRETE_STAIRS, blockStateModelGenerator);
        registerVanillaSlabVariant(whiteConcreteTexture, ModBlocks.WHITE_CONCRETE_SLABS, whiteConcreteId, blockStateModelGenerator);
        Models.CUBE_ALL.upload(ModBlocks.WHITE_CONCRETE_EDGE, whiteConcreteTexture, blockStateModelGenerator.modelCollector);
        registerEdgeBlock(ModBlocks.WHITE_CONCRETE_EDGE, whiteConcreteId, blockStateModelGenerator, blackEdgeModels);

        // LIGHT GRAY
        Identifier lightGrayConcreteId = Identifier.ofVanilla("block/light_gray_concrete");
        final TextureMap lightGrayConcreteTexture = TextureMap.all(lightGrayConcreteId);
        registerVanillaStairsVariant(lightGrayConcreteTexture, ModBlocks.LIGHT_GRAY_CONCRETE_STAIRS, blockStateModelGenerator);
        registerVanillaSlabVariant(lightGrayConcreteTexture, ModBlocks.LIGHT_GRAY_CONCRETE_SLABS, lightGrayConcreteId, blockStateModelGenerator);
        Models.CUBE_ALL.upload(ModBlocks.LIGHT_GRAY_CONCRETE_EDGE, lightGrayConcreteTexture, blockStateModelGenerator.modelCollector);
        registerEdgeBlock(ModBlocks.LIGHT_GRAY_CONCRETE_EDGE, lightGrayConcreteId, blockStateModelGenerator, blackEdgeModels);

        // GRAY
        Identifier grayConcreteId = Identifier.ofVanilla("block/gray_concrete");
        final TextureMap grayConcreteTexture = TextureMap.all(grayConcreteId);
        registerVanillaStairsVariant(grayConcreteTexture, ModBlocks.GRAY_CONCRETE_STAIRS, blockStateModelGenerator);
        registerVanillaSlabVariant(grayConcreteTexture, ModBlocks.GRAY_CONCRETE_SLABS, grayConcreteId, blockStateModelGenerator);
        Models.CUBE_ALL.upload(ModBlocks.GRAY_CONCRETE_EDGE, grayConcreteTexture, blockStateModelGenerator.modelCollector);
        registerEdgeBlock(ModBlocks.GRAY_CONCRETE_EDGE, grayConcreteId, blockStateModelGenerator, blackEdgeModels);

        // BLACK
        Identifier blackConcreteId = Identifier.ofVanilla("block/black_concrete");
        final TextureMap blackConcreteTexture = TextureMap.all(blackConcreteId);
        registerVanillaStairsVariant(blackConcreteTexture, ModBlocks.BLACK_CONCRETE_STAIRS, blockStateModelGenerator);
        registerVanillaSlabVariant(blackConcreteTexture, ModBlocks.BLACK_CONCRETE_SLABS, blackConcreteId, blockStateModelGenerator);
        Models.CUBE_ALL.upload(ModBlocks.BLACK_CONCRETE_EDGE, blackConcreteTexture, blockStateModelGenerator.modelCollector);
        registerEdgeBlock(ModBlocks.BLACK_CONCRETE_EDGE, blackConcreteId, blockStateModelGenerator, whiteEdgeModels);

        // Orange
        Identifier orangeConcreteId = Identifier.ofVanilla("block/orange_concrete");
        final TextureMap orangeConcreteTexture = TextureMap.all(orangeConcreteId);
        registerVanillaStairsVariant(orangeConcreteTexture, ModBlocks.ORANGE_CONCRETE_STAIRS, blockStateModelGenerator);
        registerVanillaSlabVariant(orangeConcreteTexture, ModBlocks.ORANGE_CONCRETE_SLABS, orangeConcreteId, blockStateModelGenerator);
        Models.CUBE_ALL.upload(ModBlocks.ORANGE_CONCRETE_EDGE, orangeConcreteTexture, blockStateModelGenerator.modelCollector);
        registerEdgeBlock(ModBlocks.ORANGE_CONCRETE_EDGE, orangeConcreteId, blockStateModelGenerator, redEdgeModels);

        // Magenta
        Identifier magentaConcreteId = Identifier.ofVanilla("block/magenta_concrete");
        final TextureMap magentaConcreteTexture = TextureMap.all(magentaConcreteId);
        registerVanillaStairsVariant(magentaConcreteTexture, ModBlocks.MAGENTA_CONCRETE_STAIRS, blockStateModelGenerator);
        registerVanillaSlabVariant(magentaConcreteTexture, ModBlocks.MAGENTA_CONCRETE_SLABS, magentaConcreteId, blockStateModelGenerator);
        Models.CUBE_ALL.upload(ModBlocks.MAGENTA_CONCRETE_EDGE, magentaConcreteTexture, blockStateModelGenerator.modelCollector);
        registerEdgeBlock(ModBlocks.MAGENTA_CONCRETE_EDGE, magentaConcreteId, blockStateModelGenerator, purpleEdgeModels);

        // Light Blue
        Identifier lightBlueConcreteId = Identifier.ofVanilla("block/light_blue_concrete");
        final TextureMap lightBlueConcreteTexture = TextureMap.all(lightBlueConcreteId);
        registerVanillaStairsVariant(lightBlueConcreteTexture, ModBlocks.LIGHT_BLUE_CONCRETE_STAIRS, blockStateModelGenerator);
        registerVanillaSlabVariant(lightBlueConcreteTexture, ModBlocks.LIGHT_BLUE_CONCRETE_SLABS, lightBlueConcreteId, blockStateModelGenerator);
        Models.CUBE_ALL.upload(ModBlocks.LIGHT_BLUE_CONCRETE_EDGE, lightBlueConcreteTexture, blockStateModelGenerator.modelCollector);
        registerEdgeBlock(ModBlocks.LIGHT_BLUE_CONCRETE_EDGE, lightBlueConcreteId, blockStateModelGenerator, blueEdgeModels);

        // YELLOW
        Identifier yellowConcreteId = Identifier.ofVanilla("block/yellow_concrete");
        final TextureMap yellowConcreteTexture = TextureMap.all(yellowConcreteId);
        registerVanillaStairsVariant(yellowConcreteTexture, ModBlocks.YELLOW_CONCRETE_STAIRS, blockStateModelGenerator);
        registerVanillaSlabVariant(yellowConcreteTexture, ModBlocks.YELLOW_CONCRETE_SLABS, yellowConcreteId, blockStateModelGenerator);
        Models.CUBE_ALL.upload(ModBlocks.YELLOW_CONCRETE_EDGE, yellowConcreteTexture, blockStateModelGenerator.modelCollector);
        registerEdgeBlock(ModBlocks.YELLOW_CONCRETE_EDGE, yellowConcreteId, blockStateModelGenerator, redEdgeModels);

        // LIME
        Identifier limeConcreteId = Identifier.ofVanilla("block/lime_concrete");
        final TextureMap limeConcreteTexture = TextureMap.all(limeConcreteId);
        registerVanillaStairsVariant(limeConcreteTexture, ModBlocks.LIME_CONCRETE_STAIRS, blockStateModelGenerator);
        registerVanillaSlabVariant(limeConcreteTexture, ModBlocks.LIME_CONCRETE_SLABS, limeConcreteId, blockStateModelGenerator);
        Models.CUBE_ALL.upload(ModBlocks.LIME_CONCRETE_EDGE, limeConcreteTexture, blockStateModelGenerator.modelCollector);
        registerEdgeBlock(ModBlocks.LIME_CONCRETE_EDGE, limeConcreteId, blockStateModelGenerator, greenEdgeModels);

        // PINK
        Identifier pinkConcreteId = Identifier.ofVanilla("block/pink_concrete");
        final TextureMap pinkConcreteTexture = TextureMap.all(pinkConcreteId);
        registerVanillaStairsVariant(pinkConcreteTexture, ModBlocks.PINK_CONCRETE_STAIRS, blockStateModelGenerator);
        registerVanillaSlabVariant(pinkConcreteTexture, ModBlocks.PINK_CONCRETE_SLABS, pinkConcreteId, blockStateModelGenerator);
        Models.CUBE_ALL.upload(ModBlocks.PINK_CONCRETE_EDGE, pinkConcreteTexture, blockStateModelGenerator.modelCollector);
        registerEdgeBlock(ModBlocks.PINK_CONCRETE_EDGE, pinkConcreteId, blockStateModelGenerator, purpleEdgeModels);

        // CYAN
        Identifier cyanConcreteId = Identifier.ofVanilla("block/cyan_concrete");
        final TextureMap cyanConcreteTexture = TextureMap.all(cyanConcreteId);
        registerVanillaStairsVariant(cyanConcreteTexture, ModBlocks.CYAN_CONCRETE_STAIRS, blockStateModelGenerator);
        registerVanillaSlabVariant(cyanConcreteTexture, ModBlocks.CYAN_CONCRETE_SLABS, cyanConcreteId, blockStateModelGenerator);
        Models.CUBE_ALL.upload(ModBlocks.CYAN_CONCRETE_EDGE, cyanConcreteTexture, blockStateModelGenerator.modelCollector);
        registerEdgeBlock(ModBlocks.CYAN_CONCRETE_EDGE, cyanConcreteId, blockStateModelGenerator, blueEdgeModels);

        // PURPLE
        Identifier purpleConcreteId = Identifier.ofVanilla("block/purple_concrete");
        final TextureMap purpleConcreteTexture = TextureMap.all(purpleConcreteId);
        registerVanillaStairsVariant(purpleConcreteTexture, ModBlocks.PURPLE_CONCRETE_STAIRS, blockStateModelGenerator);
        registerVanillaSlabVariant(purpleConcreteTexture, ModBlocks.PURPLE_CONCRETE_SLABS, purpleConcreteId, blockStateModelGenerator);
        Models.CUBE_ALL.upload(ModBlocks.PURPLE_CONCRETE_EDGE, purpleConcreteTexture, blockStateModelGenerator.modelCollector);
        registerEdgeBlock(ModBlocks.PURPLE_CONCRETE_EDGE, purpleConcreteId, blockStateModelGenerator, purpleEdgeModels);

        // Blue
        Identifier blueConcreteId = Identifier.ofVanilla("block/blue_concrete");
        final TextureMap blueConcreteTexture = TextureMap.all(blueConcreteId);
        registerVanillaStairsVariant(blueConcreteTexture, ModBlocks.BLUE_CONCRETE_STAIRS, blockStateModelGenerator);
        registerVanillaSlabVariant(blueConcreteTexture, ModBlocks.BLUE_CONCRETE_SLABS, blueConcreteId, blockStateModelGenerator);
        Models.CUBE_ALL.upload(ModBlocks.BLUE_CONCRETE_EDGE, blueConcreteTexture, blockStateModelGenerator.modelCollector);
        registerEdgeBlock(ModBlocks.BLUE_CONCRETE_EDGE, blueConcreteId, blockStateModelGenerator, blueEdgeModels);

        // BROWN
        Identifier brownConcreteId = Identifier.ofVanilla("block/brown_concrete");
        final TextureMap brownConcreteTexture = TextureMap.all(brownConcreteId);
        registerVanillaStairsVariant(brownConcreteTexture, ModBlocks.BROWN_CONCRETE_STAIRS, blockStateModelGenerator);
        registerVanillaSlabVariant(brownConcreteTexture, ModBlocks.BROWN_CONCRETE_SLABS, brownConcreteId, blockStateModelGenerator);
        Models.CUBE_ALL.upload(ModBlocks.BROWN_CONCRETE_EDGE, brownConcreteTexture, blockStateModelGenerator.modelCollector);
        registerEdgeBlock(ModBlocks.BROWN_CONCRETE_EDGE, brownConcreteId, blockStateModelGenerator, blackEdgeModels);

        // GREEN
        Identifier greenConcreteId = Identifier.ofVanilla("block/green_concrete");
        final TextureMap greenConcreteTexture = TextureMap.all(greenConcreteId);
        registerVanillaStairsVariant(greenConcreteTexture, ModBlocks.GREEN_CONCRETE_STAIRS, blockStateModelGenerator);
        registerVanillaSlabVariant(greenConcreteTexture, ModBlocks.GREEN_CONCRETE_SLABS, greenConcreteId, blockStateModelGenerator);
        Models.CUBE_ALL.upload(ModBlocks.GREEN_CONCRETE_EDGE, greenConcreteTexture, blockStateModelGenerator.modelCollector);
        registerEdgeBlock(ModBlocks.GREEN_CONCRETE_EDGE, greenConcreteId, blockStateModelGenerator, greenEdgeModels);

        // RED
        Identifier redConcreteId = Identifier.ofVanilla("block/red_concrete");
        final TextureMap redConcreteTexture = TextureMap.all(redConcreteId);
        registerVanillaStairsVariant(redConcreteTexture, ModBlocks.RED_CONCRETE_STAIRS, blockStateModelGenerator);
        registerVanillaSlabVariant(redConcreteTexture, ModBlocks.RED_CONCRETE_SLABS, redConcreteId, blockStateModelGenerator);
        Models.CUBE_ALL.upload(ModBlocks.RED_CONCRETE_EDGE, redConcreteTexture, blockStateModelGenerator.modelCollector);
        registerEdgeBlock(ModBlocks.RED_CONCRETE_EDGE, redConcreteId, blockStateModelGenerator, redEdgeModels);
    }

    private static void registerVanillaStairsVariant(TextureMap texture, Block block,
                                                     BlockStateModelGenerator modelGenerator) {
        final Identifier stairsModelId = Models.STAIRS.upload(block, texture, modelGenerator.modelCollector);
        final Identifier innerStairsModelId = Models.INNER_STAIRS.upload(block, texture, modelGenerator.modelCollector);
        final Identifier outerStairsModelId = Models.OUTER_STAIRS.upload(block, texture, modelGenerator.modelCollector);
        modelGenerator.blockStateCollector.accept(
                BlockStateModelGenerator.createStairsBlockState(block,
                        innerStairsModelId,
                        stairsModelId,
                        outerStairsModelId
                ));
        modelGenerator.registerParentedItemModel(block, stairsModelId);
    }

    private static void registerVanillaSlabVariant(TextureMap texture, Block block, Identifier vanillaId,
                                                   BlockStateModelGenerator modelGenerator) {
        final Identifier slabBottomModelId = Models.SLAB.upload(block, texture, modelGenerator.modelCollector);
        final Identifier slabTopModelId = Models.SLAB_TOP.upload(block, texture, modelGenerator.modelCollector);
        modelGenerator.blockStateCollector.accept(
                BlockStateModelGenerator.createSlabBlockState(block,
                        slabBottomModelId,
                        slabTopModelId,
                        vanillaId)
        );
        modelGenerator.registerParentedItemModel(block, slabBottomModelId);
    }

    private static void registerVanillaWallVariant(TextureMap texture, Block block,
                                                   BlockStateModelGenerator modelGenerator) {
        final Identifier postModelId = Models.TEMPLATE_WALL_POST.upload(block, texture, modelGenerator.modelCollector);
        final Identifier lowSideModelId = Models.TEMPLATE_WALL_SIDE.upload(block, texture, modelGenerator.modelCollector);
        final Identifier tallSideModelId = Models.TEMPLATE_WALL_SIDE_TALL.upload(block, texture, modelGenerator.modelCollector);
        modelGenerator.blockStateCollector.accept(
                BlockStateModelGenerator.createWallBlockState(block,
                        postModelId,
                        lowSideModelId,
                        tallSideModelId)
        );

        final Identifier inventoryModelId = Models.WALL_INVENTORY.upload(block, texture, modelGenerator.modelCollector);

        modelGenerator.registerParentedItemModel(block, inventoryModelId);
    }

    private static Identifier [] registerOuterEdgeModel(String modelName, BlockStateModelGenerator blockStateModelGenerator) {
        Identifier baseId = YumeCraft.id(modelName).withPrefixedPath("block/").withSuffixedPath("_edge");

        return new Identifier[]{
                EDGE.upload(baseId.withSuffixedPath(SubModelIds.INVENTORY.str),
                        TextureMap.texture(baseId.withSuffixedPath(SubModelIds.INVENTORY.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(baseId.withSuffixedPath(SubModelIds.END.str),
                        TextureMap.texture(baseId.withSuffixedPath(SubModelIds.END.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_90.upload(baseId.withSuffixedPath(SubModelIds.END_90.str),
                        TextureMap.texture(baseId.withSuffixedPath(SubModelIds.END.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_180.upload(baseId.withSuffixedPath(SubModelIds.END_180.str),
                        TextureMap.texture(baseId.withSuffixedPath(SubModelIds.END.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_270.upload(baseId.withSuffixedPath(SubModelIds.END_270.str),
                        TextureMap.texture(baseId.withSuffixedPath(SubModelIds.END.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(baseId.withSuffixedPath(SubModelIds.CORNER.str),
                        TextureMap.texture(baseId.withSuffixedPath(SubModelIds.CORNER.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_90.upload(baseId.withSuffixedPath(SubModelIds.CORNER_90.str),
                        TextureMap.texture(baseId.withSuffixedPath(SubModelIds.CORNER.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_180.upload(baseId.withSuffixedPath(SubModelIds.CORNER_180.str),
                        TextureMap.texture(baseId.withSuffixedPath(SubModelIds.CORNER.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_270.upload(baseId.withSuffixedPath(SubModelIds.CORNER_270.str),
                        TextureMap.texture(baseId.withSuffixedPath(SubModelIds.CORNER.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(baseId.withSuffixedPath(SubModelIds.COLUMN.str),
                        TextureMap.texture(baseId.withSuffixedPath(SubModelIds.COLUMN.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_90.upload(baseId.withSuffixedPath(SubModelIds.COLUMN_ALT.str),
                        TextureMap.texture(baseId.withSuffixedPath(SubModelIds.COLUMN.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(baseId.withSuffixedPath(SubModelIds.FACE.str),
                        TextureMap.texture(baseId.withSuffixedPath(SubModelIds.FACE.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_90.upload(baseId.withSuffixedPath(SubModelIds.FACE_90.str),
                        TextureMap.texture(baseId.withSuffixedPath(SubModelIds.FACE.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_180.upload(baseId.withSuffixedPath(SubModelIds.FACE_180.str),
                        TextureMap.texture(baseId.withSuffixedPath(SubModelIds.FACE.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_270.upload(baseId.withSuffixedPath(SubModelIds.FACE_270.str),
                        TextureMap.texture(baseId.withSuffixedPath(SubModelIds.FACE.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(baseId.withSuffixedPath(SubModelIds.BLANK.str),
                        TextureMap.texture(baseId.withSuffixedPath(SubModelIds.BLANK.str)),
                        blockStateModelGenerator.modelCollector),
        };
    }

    private static void registerEdgeBlock(EdgeBlock block, BlockStateModelGenerator blockStateModelGenerator, Identifier [] subModelIds) {

        Identifier baseModelId = Models.CUBE_ALL.upload(block, TextureMap.all(block), blockStateModelGenerator.modelCollector);

        registerEdgeBlock(block, baseModelId, blockStateModelGenerator, subModelIds);
    }

    private static void registerEdgeBlock(EdgeBlock block, Identifier baseModelId, BlockStateModelGenerator blockStateModelGenerator, Identifier [] subModelIds) {

        MultipartBlockStateSupplier multipartBlockStateSupplier = MultipartBlockStateSupplier.create(block)
                .with(BlockStateVariant.create().put(VariantSettings.MODEL, baseModelId));
        multipartBlockStateSupplier = checkEdgeBlockFace(multipartBlockStateSupplier, Properties.EAST,
                Properties.UP, Properties.WEST, Properties.DOWN, Properties.NORTH, VariantSettings.Y, VariantSettings.Rotation.R0, subModelIds);
        multipartBlockStateSupplier = checkEdgeBlockFace(multipartBlockStateSupplier, Properties.SOUTH,
                Properties.UP, Properties.NORTH, Properties.DOWN, Properties.EAST, VariantSettings.Y, VariantSettings.Rotation.R90, subModelIds);
        multipartBlockStateSupplier = checkEdgeBlockFace(multipartBlockStateSupplier, Properties.WEST,
                Properties.UP, Properties.EAST, Properties.DOWN, Properties.SOUTH, VariantSettings.Y, VariantSettings.Rotation.R180, subModelIds);
        multipartBlockStateSupplier = checkEdgeBlockFace(multipartBlockStateSupplier, Properties.NORTH,
                Properties.UP, Properties.SOUTH, Properties.DOWN, Properties.WEST, VariantSettings.Y, VariantSettings.Rotation.R270, subModelIds);
        multipartBlockStateSupplier = checkEdgeBlockFace(multipartBlockStateSupplier, Properties.EAST,
                Properties.SOUTH, Properties.WEST, Properties.NORTH, Properties.UP, VariantSettings.X, VariantSettings.Rotation.R270, subModelIds);
        multipartBlockStateSupplier = checkEdgeBlockFace(multipartBlockStateSupplier, Properties.EAST,
                Properties.NORTH, Properties.WEST, Properties.SOUTH, Properties.DOWN, VariantSettings.X, VariantSettings.Rotation.R90, subModelIds);

        blockStateModelGenerator.blockStateCollector.accept(multipartBlockStateSupplier);
    }



    private static void registerEdgeBlockWithUniqueModel(EdgeBlock block, BlockStateModelGenerator blockStateModelGenerator) {

        Models.CUBE_ALL.upload(block, TextureMap.all(block), blockStateModelGenerator.modelCollector);

        Identifier [] subModelIds = {
                EDGE.upload(block, SubModelIds.INVENTORY.str, TextureMap.texture(Registries.BLOCK.getId(block)
                        .withPrefixedPath("block/").withSuffixedPath(SubModelIds.INVENTORY.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.END.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.END.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_90.upload(block, SubModelIds.END_90.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.END.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_180.upload(block, SubModelIds.END_180.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.END.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_270.upload(block, SubModelIds.END_270.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.END.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.CORNER.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.CORNER.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_90.upload(block, SubModelIds.CORNER_90.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.CORNER.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_180.upload(block, SubModelIds.CORNER_180.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.CORNER.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_270.upload(block, SubModelIds.CORNER_270.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.CORNER.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.COLUMN.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.COLUMN.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_90.upload(block, SubModelIds.COLUMN_ALT.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.COLUMN.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.FACE.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.FACE.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_90.upload(block, SubModelIds.FACE_90.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.FACE.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_180.upload(block, SubModelIds.FACE_180.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.FACE.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE_270.upload(block, SubModelIds.FACE_270.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.FACE.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.BLANK.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.BLANK.str)),
                        blockStateModelGenerator.modelCollector),
        };

        MultipartBlockStateSupplier multipartBlockStateSupplier = MultipartBlockStateSupplier.create(block);
        multipartBlockStateSupplier = checkEdgeBlockFace(multipartBlockStateSupplier, Properties.EAST,
                Properties.UP, Properties.WEST, Properties.DOWN, Properties.NORTH, VariantSettings.Y, VariantSettings.Rotation.R0, subModelIds);
        multipartBlockStateSupplier = checkEdgeBlockFace(multipartBlockStateSupplier, Properties.SOUTH,
                Properties.UP, Properties.NORTH, Properties.DOWN, Properties.EAST, VariantSettings.Y, VariantSettings.Rotation.R90, subModelIds);
        multipartBlockStateSupplier = checkEdgeBlockFace(multipartBlockStateSupplier, Properties.WEST,
                Properties.UP, Properties.EAST, Properties.DOWN, Properties.SOUTH, VariantSettings.Y, VariantSettings.Rotation.R180, subModelIds);
        multipartBlockStateSupplier = checkEdgeBlockFace(multipartBlockStateSupplier, Properties.NORTH,
                Properties.UP, Properties.SOUTH, Properties.DOWN, Properties.WEST, VariantSettings.Y, VariantSettings.Rotation.R270, subModelIds);
        multipartBlockStateSupplier = checkEdgeBlockFace(multipartBlockStateSupplier, Properties.EAST,
                Properties.SOUTH, Properties.WEST, Properties.NORTH, Properties.UP, VariantSettings.X, VariantSettings.Rotation.R270, subModelIds);
        multipartBlockStateSupplier = checkEdgeBlockFace(multipartBlockStateSupplier, Properties.EAST,
                Properties.NORTH, Properties.WEST, Properties.SOUTH, Properties.DOWN, VariantSettings.X, VariantSettings.Rotation.R90, subModelIds);

        blockStateModelGenerator.blockStateCollector.accept(multipartBlockStateSupplier);
    }

    private static MultipartBlockStateSupplier checkEdgeBlockFace(MultipartBlockStateSupplier init,
                                                                  BooleanProperty left, BooleanProperty up, BooleanProperty right, BooleanProperty down, BooleanProperty front,
                                                                  VariantSetting<VariantSettings.Rotation> axis, VariantSettings.Rotation rotation,
                                                                  Identifier [] ids){

        return init.with(When.create()
                                .set(left, false)
                                .set(up, false)
                                .set(right, false)
                                .set(down, false)
                                .set(front, false),
                        BlockStateVariant.create().put(VariantSettings.MODEL, ids[0]).put(axis, rotation)
                                )
                // 1 Axis
                .with(When.create()
                                .set(left, true)
                                .set(up, false)
                                .set(right, false)
                                .set(down, false),
                        BlockStateVariant.create().put(VariantSettings.MODEL, ids[1]).put(axis, rotation)
                                )
                .with(When.create()
                                .set(left, false)
                                .set(up, true)
                                .set(right, false)
                                .set(down, false)
                                .set(front, false),
                        BlockStateVariant.create().put(VariantSettings.MODEL, ids[2]).put(axis, rotation)
                                )
                .with(When.create()
                                .set(left, false)
                                .set(up, false)
                                .set(right, true)
                                .set(down, false)
                                .set(front, false),
                        BlockStateVariant.create().put(VariantSettings.MODEL, ids[3]).put(axis, rotation)
                                )
                .with(When.create()
                                .set(left, false)
                                .set(up, false)
                                .set(right, false)
                                .set(down, true)
                                .set(front, false),
                        BlockStateVariant.create().put(VariantSettings.MODEL, ids[4]).put(axis, rotation)
                                )

                // Corners
                .with(When.create()
                                .set(left, true)
                                .set(up, true)
                                .set(right, false)
                                .set(down, false)
                                .set(front, false),
                        BlockStateVariant.create().put(VariantSettings.MODEL, ids[5]).put(axis, rotation)
                                )
                .with(When.create()
                                .set(left, false)
                                .set(up, true)
                                .set(right, true)
                                .set(down, false)
                                .set(front, false),
                        BlockStateVariant.create().put(VariantSettings.MODEL, ids[6]).put(axis, rotation)
                                )
                .with(When.create()
                                .set(left, false)
                                .set(up, false)
                                .set(right, true)
                                .set(down, true)
                                .set(front, false),
                        BlockStateVariant.create().put(VariantSettings.MODEL, ids[7]).put(axis, rotation)
                                )
                .with(When.create()
                                .set(left, true)
                                .set(up, false)
                                .set(right, false)
                                .set(down, true)
                                .set(front, false),
                        BlockStateVariant.create().put(VariantSettings.MODEL, ids[8]).put(axis, rotation)
                                )

                // COLUMNS
                .with(When.create()
                                .set(left, true)
                                .set(up, false)
                                .set(right, true)
                                .set(down, false)
                                .set(front, false),
                        BlockStateVariant.create().put(VariantSettings.MODEL, ids[9]).put(axis, rotation)
                                )
                .with(When.create()
                                .set(left, false)
                                .set(up, true)
                                .set(right, false)
                                .set(down, true)
                                .set(front, false),
                        BlockStateVariant.create().put(VariantSettings.MODEL, ids[10]).put(axis, rotation)
                                )

                //FACES
                .with(When.create()
                                .set(left, true)
                                .set(up, true)
                                .set(right, true)
                                .set(down, false)
                                .set(front, false),
                        BlockStateVariant.create().put(VariantSettings.MODEL, ids[11]).put(axis, rotation)
                                )
                .with(When.create()
                                .set(left, false)
                                .set(up, true)
                                .set(right, true)
                                .set(down, true)
                                .set(front, false),
                        BlockStateVariant.create().put(VariantSettings.MODEL, ids[12]).put(axis, rotation)
                                )
                .with(When.create()
                                .set(left, true)
                                .set(up, false)
                                .set(right, true)
                                .set(down, true)
                                .set(front, false),
                        BlockStateVariant.create().put(VariantSettings.MODEL, ids[13]).put(axis, rotation)
                                )
                .with(When.create()
                                .set(left, true)
                                .set(up, true)
                                .set(right, false)
                                .set(down, true)
                                .set(front, false),
                        BlockStateVariant.create().put(VariantSettings.MODEL, ids[14]).put(axis, rotation)
                                )

                //Blank
                .with(When.create()
                                .set(left, true)
                                .set(up, true)
                                .set(right, true)
                                .set(down, true)
                                .set(front, false),
                        BlockStateVariant.create().put(VariantSettings.MODEL, ids[15]).put(axis, rotation)
                                );


    }

    private enum SubModelIds {
        INVENTORY(0, "_inventory"),
        END(1, "_end"),
        END_90(2, "_end_90"),
        END_180(3, "_end_180"),
        END_270(4, "_end_270"),
        CORNER(5, "_corner"),
        CORNER_90(6, "_corner_90"),
        CORNER_180(7, "_corner_180"),
        CORNER_270(8, "_corner_270"),
        COLUMN(9, "_column"),
        COLUMN_ALT(10, "_column_alt"),
        FACE(11, "_face"),
        FACE_90(12, "_face_90"),
        FACE_180(13, "_face_180"),
        FACE_270(14, "_face_270"),
        BLANK(15, "_blank");

        public final int idx;
        public final String str;

        private SubModelIds(int idx, String str) {
            this.idx = idx;
            this.str = str;
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
    }
}
