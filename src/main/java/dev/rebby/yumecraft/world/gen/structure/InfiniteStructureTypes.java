package dev.rebby.yumecraft.world.gen.structure;

import com.mojang.serialization.Codec;
import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class InfiniteStructureTypes {

    public static InfiniteStructureType<DensityBasedInfiniteStructure> DENSITY_BASED_STRUCTURE =
            register("density_based",
                    new InfiniteStructureType<>(DensityBasedInfiniteStructure.CODEC));

    public static <T extends InfiniteStructure> InfiniteStructureType<T> register(String id, InfiniteStructureType<T> structureType){
        return Registry.register(InfiniteStructureType.REGISTRY, Identifier.of(YumeCraft.MOD_ID, id), structureType);
    }

    public static void init(){
        Codec<InfiniteStructureType<?>> structureTypeCodec = InfiniteStructureType.REGISTRY.getCodec();

        Codec<InfiniteStructure> structureCodec = structureTypeCodec.dispatch("type", InfiniteStructure::getType, InfiniteStructureType::codec);

        InfiniteStructure.CheckValueTypes.init();

        YumeCraft.LOGGER.info("Registering Infinite Structures for " + YumeCraft.MOD_ID);
    }
}
