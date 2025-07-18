package dev.rebby.yumecraft.world.gen.structure.placement;

import com.mojang.serialization.MapCodec;
import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import net.minecraft.world.gen.chunk.placement.StructurePlacementType;

public interface ModStructurePlacementTypes<SP extends StructurePlacement>{

    StructurePlacementType<GridStructurePlacement> GRID = register("grid_pattern", GridStructurePlacement.CODEC);

    MapCodec<SP> codec();

    private static <SP extends StructurePlacement> StructurePlacementType<SP> register(String id, MapCodec<SP> codec) {
        return Registry.register(Registries.STRUCTURE_PLACEMENT, YumeCraft.id(id), () -> codec);
    }

    static void init(){
        YumeCraft.LOGGER.info("Registering Structure Placement Types for "+YumeCraft.MOD_ID);
    }
}
