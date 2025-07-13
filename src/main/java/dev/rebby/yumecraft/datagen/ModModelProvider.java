package dev.rebby.yumecraft.datagen;

import dev.rebby.yumecraft.YumeCraft;
import dev.rebby.yumecraft.block.EdgeBlock;
import dev.rebby.yumecraft.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {

    public static final Model EDGE = new Model(
            Optional.of(YumeCraft.id("block/edge")),
            Optional.empty(),
            TextureKey.TEXTURE
    );

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.BLUESTONE);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.BLUE_COBBLE);

        final Identifier bubblingMagmaModelId = Models.CUBE_ALL.upload(ModBlocks.BUBBLING_MAGMA_BLOCK,
                TextureMap.all(Identifier.ofVanilla("block/magma")), blockStateModelGenerator.modelCollector);

        blockStateModelGenerator.registerParentedItemModel(ModBlocks.BUBBLING_MAGMA_BLOCK, bubblingMagmaModelId);
        blockStateModelGenerator.registerSimpleState(ModBlocks.BUBBLING_MAGMA_BLOCK);

        BlockStateModelGenerator.BlockTexturePool whitebrickPool =
                blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.WHITEBRICK);

        whitebrickPool.stairs(ModBlocks.WHITEBRICK_STAIRS);
        whitebrickPool.slab(ModBlocks.WHITEBRICK_SLAB);
        whitebrickPool.wall(ModBlocks.WHITEBRICK_WALL);

        BlockStateModelGenerator.BlockTexturePool chiseledWhitebrickPool =
                blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.CHISELED_WHITEBRICK);

        chiseledWhitebrickPool.slab(ModBlocks.CHISELED_WHITEBRICK_SLAB);
        chiseledWhitebrickPool.wall(ModBlocks.CHISELED_WHITEBRICK_WALL);

        BlockStateModelGenerator.BlockTexturePool reinforcedBlueConcretePool =
                blockStateModelGenerator.registerCubeAllModelTexturePool(ModBlocks.REINFORCED_BLUE_CONCRETE);

        reinforcedBlueConcretePool.stairs(ModBlocks.REINFORCED_BLUE_CONCRETE_STAIRS);
        reinforcedBlueConcretePool.slab(ModBlocks.REINFORCED_BLUE_CONCRETE_SLABS);

        registerEdgeBlock(ModBlocks.BLUE_CONCRETE_EDGE, blockStateModelGenerator);


    }



    private static void registerEdgeBlock(EdgeBlock block, BlockStateModelGenerator blockStateModelGenerator) {

        Models.CUBE_ALL.upload(block, TextureMap.all(block), blockStateModelGenerator.modelCollector);

        Identifier [] subModelIds = {
                EDGE.upload(block, SubModelIds.INVENTORY.str, TextureMap.texture(Registries.BLOCK.getId(block)
                        .withPrefixedPath("block/").withSuffixedPath(SubModelIds.INVENTORY.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.END.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.END.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.END_90.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.END_90.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.END_180.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.END_180.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.END_270.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.END_270.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.CORNER.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.CORNER.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.CORNER_90.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.CORNER_90.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.CORNER_180.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.CORNER_180.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.CORNER_270.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.CORNER_270.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.COLUMN.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.COLUMN.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.COLUMN_ALT.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.COLUMN_ALT.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.FACE.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.FACE.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.FACE_90.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.FACE_90.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.FACE_180.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.FACE_180.str)),
                        blockStateModelGenerator.modelCollector),
                EDGE.upload(block, SubModelIds.FACE_270.str, TextureMap.texture(Registries.BLOCK.getId(block)
                                .withPrefixedPath("block/").withSuffixedPath(SubModelIds.FACE_270.str)),
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
