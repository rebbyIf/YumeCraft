package dev.rebby.yumecraft.world.gen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface InfiniteStructure {

    Codec<InfiniteStructure> INFINITE_STRUCTURE_CODEC = InfiniteStructureType.REGISTRY.getCodec()
            .dispatch("type",InfiniteStructure::getType, InfiniteStructureType::codec);


    void generate(StructureTemplateManager structureTemplateManager, ServerWorldAccess world, Chunk chunk);

    /**
     * Creates a vertical column of blocks for checking worldgen.
     * @param x x-pos
     * @param z z-pos
     * @return a vertical column of blocks
     */
    @Nullable
    VerticalBlockSample getColumnSample(int x, int z);

    double getSample(BlockPos pos);

    void generateDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos, StructureTemplateManager structureTemplateManager, long seed);

    InfiniteStructureType<?> getType();

    interface CheckValue {
        Codec<CheckValue> VALUE_CODEC = CheckValueType.REGISTRY.getCodec().dispatch("type",
                CheckValue::getType, CheckValueType::codec);

        boolean check(DensityFunction check, BlockPos loc, BlockPos minLoc, BlockPos maxLoc, int scale);

        boolean doesOverrideIfTrue();

        boolean doesOverrideIfFalse();

        CheckValueType<?> getType();
    }

    record CheckValueType<T extends CheckValue> (MapCodec<T> codec){

        public static final Registry<CheckValueType<?>> REGISTRY =
                new SimpleRegistry<>(RegistryKey.ofRegistry(YumeCraft.id("check_values")),
                        Lifecycle.stable());

    }

    class CheckValueTypes {
        public static final CheckValueType<DensityBasedInfiniteStructure.CheckDefaultValue> DEFAULT = register("default",
                new CheckValueType<>(DensityBasedInfiniteStructure.CheckDefaultValue.DEFAULT_VALUE_CODEC));
        public static final CheckValueType<DensityBasedInfiniteStructure.CheckPlaneForSingleValue> PLANE_FOR_SINGLE_VALUE = register("plane_for_value",
                new CheckValueType<>(DensityBasedInfiniteStructure.CheckPlaneForSingleValue.PLANE_SINGLE_VALUE_CODEC));
        public static final CheckValueType<DensityBasedInfiniteStructure.CheckAxisForSingleValue> AXIS_FOR_SINGLE_VALUE = register("axis_for_value",
                new CheckValueType<>(DensityBasedInfiniteStructure.CheckAxisForSingleValue.AXIS_SINGLE_VALUE_CODEC));

        public static <T extends CheckValue> CheckValueType<T> register(String id, CheckValueType<T> type){
            return Registry.register(CheckValueType.REGISTRY, YumeCraft.id(id), type);
        }

        public static void init() {
            Codec<CheckValueType<?>> valueTypeCodec = CheckValueType.REGISTRY.getCodec();

            Codec<CheckValue> valueCodec = valueTypeCodec.dispatch("type", CheckValue::getType, CheckValueType::codec);
        }
    }
}
