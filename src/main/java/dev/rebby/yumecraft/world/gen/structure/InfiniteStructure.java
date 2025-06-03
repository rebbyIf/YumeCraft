package dev.rebby.yumecraft.world.gen.structure;

import com.mojang.serialization.*;
import dev.rebby.yumecraft.YumeCraft;
import dev.rebby.yumecraft.world.gen.structure.check_value.CheckValue;
import dev.rebby.yumecraft.world.gen.structure.check_value.CheckValueType;
import net.minecraft.registry.Registry;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface InfiniteStructure {

    Codec<InfiniteStructure> INFINITE_STRUCTURE_CODEC = InfiniteStructureType.REGISTRY.getCodec()
            .dispatch("type",InfiniteStructure::getType, InfiniteStructureType::codec);


    void generate(NoiseConfig noiseConfig, StructureTemplateManager structureTemplateManager, ServerWorldAccess world, Chunk chunk);

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

    @Nullable
    MultiNoiseUtil.MultiNoiseSampler returnNoiseSampler();

    InfiniteStructureType<?> getType();

}
