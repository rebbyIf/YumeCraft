package dev.rebby.yumecraft.world.gen.structure;

import com.mojang.serialization.MapCodec;
import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public interface ModStructureTypes<S extends Structure> {

    StructureType<RepeatingStructure> REPEATING_STRUCTURE = register("repeating_structure", RepeatingStructure.CODEC);
    StructureType<DensityBasedStructure> DENSITY_BASED_STRUCTURE = register("density_based", DensityBasedStructure.CODEC);

    MapCodec<S> codec();

    private static <S extends Structure> StructureType<S> register(String id, MapCodec<S> codec) {
        return Registry.register(Registries.STRUCTURE_TYPE, YumeCraft.id(id), () -> codec);
    }

    static void init(){
        YumeCraft.LOGGER.info("Registering Structure Types for "+YumeCraft.MOD_ID);
    }
}
