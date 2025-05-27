package dev.rebby.yumecraft.world.gen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import org.jetbrains.annotations.Nullable;

public interface InfiniteStructure {

    Codec<InfiniteStructure> INFINITE_STRUCTURE_CODEC = InfiniteStructureType.REGISTRY.getCodec()
            .dispatch("type",InfiniteStructure::getType, InfiniteStructureType::codec);

    void load();


    void generate(StructureTemplateManager structureTemplateManager, ServerWorldAccess world, Chunk chunk);

    /**
     * Creates a vertical column of blocks for checking worldgen.
     * @param x x-pos
     * @param z z-pos
     * @return a vertical column of blocks
     */
    @Nullable
    VerticalBlockSample getColumnSample(int x, int z);

    InfiniteStructureType<?> getType();
}
