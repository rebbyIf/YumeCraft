package dev.rebby.yumecraft.world.gen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.rebby.yumecraft.world.gen.fractal.Fractal;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.*;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.*;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FractalChunkGenerator extends ChunkGenerator {

    public static final MapCodec<FractalChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(FractalChunkGenerator::getBiomeSource),
                    Codec.INT.fieldOf("world_height").forGetter(FractalChunkGenerator::getWorldHeight),
                    Fractal.FRACTAL_CODEC.fieldOf("fractal").forGetter(FractalChunkGenerator::getFractal),
                    BlockState.CODEC.fieldOf("default_block").forGetter(FractalChunkGenerator::getDefaultBlock)
            ).apply(instance, FractalChunkGenerator::new));

    private final int worldHeight;
    private final Fractal fractal;
    private final BlockState defaultBlock;

    FractalChunkGenerator(BiomeSource biomeSource, int worldHeight, Fractal fractal, BlockState defaultBlock) {
        super(biomeSource);
        this.worldHeight = worldHeight;
        this.fractal = fractal;
        this.defaultBlock = defaultBlock;
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

    }

    @Override
    public void populateEntities(ChunkRegion region) {

    }

    @Override
    public int getWorldHeight() {
        return worldHeight;
    }

    public Fractal getFractal() {
        return fractal;
    }

    public BlockState getDefaultBlock() {
        return defaultBlock;
    }

    @Override
    public CompletableFuture<Chunk> populateNoise(Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk) {
        Heightmap heightmap = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmap2 = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
        int minimumY = 0;
        ChunkPos chunkPos = chunk.getPos();
        int i = chunkPos.getStartX();
        int j = chunkPos.getStartZ();
        float gradient = 3.0f;

        BlockPos.Mutable mutable = new BlockPos.Mutable();


        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = minimumY; y < worldHeight; y++) {
                    float value = -fractal.getValue(i + x, y, j + z);
                    value += gradient / (y);

                    if (value > 0 && fractal.inFractal(i + x, y, j + z)) {
                        chunk.setBlockState(mutable.set(x, y, z), defaultBlock, false);
                        heightmap.trackUpdate(x, y, z, defaultBlock);
                        heightmap2.trackUpdate(x, y, z, defaultBlock);
                    }
                }
            }
        }

        return CompletableFuture.completedFuture(chunk);
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @Override
    public int getMinimumY() {
        return 0;
    }

    @Override
    public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig) {
        return world.getHeight();
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig) {
        BlockState [] blockStates = {defaultBlock};
        return new VerticalBlockSample(getMinimumY(), blockStates);
    }

    @Override
    public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos) {

    }
}
