package dev.rebby.yumecraft.world.gen.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public record DensityBasedInfiniteStructure(int minY, int maxY, int fac, RegistryEntry<DensityFunction> densityFunctionEntry, List<DensityBasedInfiniteStructure.Structure> structures) implements InfiniteStructure{

    public static final Codec<Either<Identifier, StructureTemplate>> LOCATION_CODEC = Codec.of(
            DensityBasedInfiniteStructure.Structure::encodeLocation, Identifier.CODEC.map(Either::left));

    public static final MapCodec<DensityBasedInfiniteStructure.Structure> STRUCTURE_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.DOUBLE.fieldOf("min_inclusive").forGetter(DensityBasedInfiniteStructure.Structure::min_inclusive),
                    Codec.DOUBLE.fieldOf("max_inclusive").forGetter(DensityBasedInfiniteStructure.Structure::max_inclusive),
                    LOCATION_CODEC.fieldOf("location").forGetter(DensityBasedInfiniteStructure.Structure::location))
                    .apply(instance, DensityBasedInfiniteStructure.Structure::new));

    public static final MapCodec<DensityBasedInfiniteStructure> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("min_y").forGetter(DensityBasedInfiniteStructure::minY),
                    Codec.INT.fieldOf("max_y").forGetter(DensityBasedInfiniteStructure::maxY),
                    Codec.intRange(2, 4).fieldOf("factor").forGetter(DensityBasedInfiniteStructure::fac),
                    DensityFunction.REGISTRY_ENTRY_CODEC.fieldOf("density_function").forGetter(DensityBasedInfiniteStructure::densityFunctionEntry),
                    STRUCTURE_CODEC.codec().listOf().fieldOf("structure_pieces").forGetter(DensityBasedInfiniteStructure::structures))
                    .apply(instance, DensityBasedInfiniteStructure::new));

    @Override
    public void load() {
        densityFunctionEntry.value().apply(new DensityFunction.DensityFunctionVisitor() {
            private final Map<DensityFunction, DensityFunction> unwrapped = new HashMap<>();

            private DensityFunction unwrap(DensityFunction densityFunction) {
                if (densityFunction instanceof DensityFunctionTypes.RegistryEntryHolder registryEntryHolder) {
                    return (DensityFunction) registryEntryHolder.function().value();
                } else if (densityFunction instanceof DensityFunctionTypes.Wrapper wrapper) {
                    return wrapper.wrapped();
                } else {
                    return densityFunction;
                }
            }

            public DensityFunction apply(DensityFunction densityFunction) {
                return (DensityFunction)this.unwrapped.computeIfAbsent(densityFunction, this::unwrap);
            }

            @Override
            public DensityFunction.Noise apply(DensityFunction.Noise noiseDensityFunction) {
                DoublePerlinNoiseSampler doublePerlinNoiseSampler = DoublePerlinNoiseSampler.create(new LocalRandom(1L), noiseDensityFunction.noiseData().value());
                return new DensityFunction.Noise(noiseDensityFunction.noiseData(), doublePerlinNoiseSampler);
            }
        });
    }

    @Override
    public void generate(StructureTemplateManager structureTemplateManager, ServerWorldAccess world, Chunk chunk) {
        int scale = (int) Math.pow(2, fac);

        for (int x = 0; x < 16; x += scale) {
            for (int y = minY; y < maxY; y += scale) {
                for (int z = 0; z < 16; z += scale) {
                    BlockPos pos = chunk.getPos().getStartPos().add(x, y, z);
                    double density = densityFunctionEntry.value().sample(new DensityFunction.UnblendedNoisePos(pos.getX(), pos.getY(), pos.getZ()));
                    //System.out.println("Y = "+pos.getY()+", density: "+density);
                    for (DensityBasedInfiniteStructure.Structure structure : structures) {
                        if (structure.place(density, pos, structureTemplateManager, world)){

                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z) {
        return null;
    }

    @Override
    public InfiniteStructureType<?> getType() {
        return InfiniteStructureTypes.DENSITY_BASED_STRUCTURE;
    }

    public record Structure(double min_inclusive, double max_inclusive, Either<Identifier, StructureTemplate> location) {

        // Copied from minecraft's source
        private static <T> DataResult<T> encodeLocation(Either<Identifier, StructureTemplate> location, DynamicOps<T> ops, T prefix) {
            Optional<Identifier> optional = location.left();
            return optional.isEmpty()
                    ? DataResult.error(() -> "Can not serialize a runtime pool element")
                    : Identifier.CODEC.encode(optional.get(), ops, prefix);
        }

        private StructureTemplate getStructure(StructureTemplateManager structureTemplateManager) {
            return this.location.map(structureTemplateManager::getTemplateOrBlank, Function.identity());
        }

        private boolean place(double density, BlockPos pos, StructureTemplateManager structureTemplateManager, ServerWorldAccess world) {
            if (density > max_inclusive || density < min_inclusive) {
                return false;
            }

            //System.out.println("Printing Structure");

            StructureTemplate structureTemplate = getStructure(structureTemplateManager);
            StructurePlacementData structurePlacementData = new StructurePlacementData();
            structurePlacementData.setUpdateNeighbors(false);
            structurePlacementData.setIgnoreEntities(true);
            boolean is = structureTemplate.place(world, pos, pos, structurePlacementData, world.getRandom(), 2);
//            if (is)
//                System.out.println("Printing Structure");

            return true;
        }
    }
}
