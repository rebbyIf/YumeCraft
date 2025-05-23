package dev.rebby.yumecraft.block.entity;

import dev.rebby.yumecraft.YumeCraft;
import dev.rebby.yumecraft.block.ModBlocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntityTypes {

    public static final BlockEntityType<BubblingMagmaBE> BUBBLING_MAGMA_BLOCK_ENTITY = register("bubbling_magma_block_entity",
            BlockEntityType.Builder.create(BubblingMagmaBE::new, ModBlocks.BUBBLING_MAGMA_BLOCK).build());

    private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType<T> type) {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(YumeCraft.MOD_ID, name), type);
    }

    public static void init(){
        YumeCraft.LOGGER.info("Registering block entity types for " + YumeCraft.MOD_ID);
    }

}
