package dev.rebby.yumecraft.world.gen.chunk_generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.rebby.yumecraft.YumeCraft;
import dev.rebby.yumecraft.world.gen.structure.InfiniteStructure;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.BiomeSupplier;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.chunk.BelowZeroRetrogen;
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
    private StructureTemplateManager structureTemplateManager;
    private Long seed;

    public StructureChunkGenerator(BiomeSource biomeSource, InfiniteStructure infiniteStructure, int minY, int worldHeight) {
        super(biomeSource);
        this.infiniteStructure = infiniteStructure;
        this.minY = minY;
        this.worldHeight = worldHeight;
        structureTemplateManager = null;
        seed = null;
        //this.infiniteStructure.load();
    }

    @Override
    public CompletableFuture<Chunk> populateBiomes(NoiseConfig noiseConfig, Blender blender, StructureAccessor structureAccessor, Chunk chunk) {
        return CompletableFuture.supplyAsync(Util.debugSupplier("init_structure_biomes", () -> {
            this.populateBiomes(blender, chunk);
            return chunk;
        }), Util.getMainWorkerExecutor());
    }

    private void populateBiomes(Blender blender, Chunk chunk) {

        BiomeSupplier biomeSupplier = BelowZeroRetrogen.getBiomeSupplier(blender.getBiomeSupplier(this.biomeSource), chunk);
        MultiNoiseUtil.MultiNoiseSampler noiseSampler = infiniteStructure.returnNoiseSampler();
        if (noiseSampler != null) {
            chunk.populateBiomes(biomeSupplier, noiseSampler);
        }
    }

    @Override
    protected MapCodec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carverStep) {


        if (this.seed == null) {
            this.seed = chunkRegion.getSeed();
        }
        MinecraftServer server = chunkRegion.getServer();
        if (server != null) {
            if (structureTemplateManager == null) {
                structureTemplateManager = server.getStructureTemplateManager();
            }
            infiniteStructure.generate(noiseConfig, structureTemplateManager, chunkRegion, chunk);
        }
        else {
            YumeCraft.LOGGER.error("Error: cannot find server");
        }
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
        if (structureTemplateManager != null && seed != null) {
            infiniteStructure.generateDebugHudText(text, noiseConfig, pos, structureTemplateManager, seed);
            return;
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        text.add("Infinite structure density: "+ decimalFormat.format(infiniteStructure.getSample(pos)));
    }
}
