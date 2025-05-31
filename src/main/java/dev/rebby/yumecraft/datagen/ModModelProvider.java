package dev.rebby.yumecraft.datagen;

import dev.rebby.yumecraft.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.BlockState;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {
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
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }
}
