package dev.rebby.yumecraft.world.gen.chunk_generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.rebby.yumecraft.world.gen.structure.InfiniteStructure;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StructureChunkGenerator extends ChunkGenerator {

    public static final MapCodec<StructureChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(StructureChunkGenerator::getBiomeSource),
                    InfiniteStructure.INFINITE_STRUCTURE_CODEC.fieldOf("structure").forGetter(StructureChunkGenerator::getInfiniteStructure),
                    Codec.INT.fieldOf("minimum_y").forGetter(StructureChunkGenerator::getMinimumY),
                    Codec.INT.fieldOf("world_height").forGetter(StructureChunkGenerator::getWorldHeight))
                    .apply(instance, StructureChunkGenerator::new));


    public InfiniteStructure getInfiniteStructure() {
        return infiniteStructure;
    }

    private final InfiniteStructure infiniteStructure;
    private final int minY;
    private final int worldHeight;

    public StructureChunkGenerator(BiomeSource biomeSource, InfiniteStructure infiniteStructure, int minY, int worldHeight) {
        super(biomeSource);
        this.infiniteStructure = infiniteStructure;
        this.minY = minY;
        this.worldHeight = worldHeight;
        //this.infiniteStructure.load();
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carverStep) {

    }

    @Override
    public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {
        MinecraftServer server = region.getServer();
        if (server != null) {
            infiniteStructure.generate(server.getStructureTemplateManager(), region, chunk);
        }
        else {
            System.out.println("Error: cannot find server");
        }
    }

    @Override
    public void populateEntities(ChunkRegion region) {

    }

    @Override
    public int getWorldHeight() {
        return worldHeight;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getSeaLevel() {
        return minY;
    }

    @Override
    public int getMinimumY() {
        return minY;
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
        return 0;
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
        VerticalBlockSample sample = infiniteStructure.getColumnSample(x,z);
        if (sample == null) {
            sample = new VerticalBlockSample(minY, new BlockState[0]);
        }
        return sample;
    }

    @Override
    public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        text.add("Infinite structure density: "+ decimalFormat.format(infiniteStructure.getSample(pos)));
    }
}
