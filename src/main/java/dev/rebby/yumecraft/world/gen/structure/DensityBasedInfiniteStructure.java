package dev.rebby.yumecraft.world.gen.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
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
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.densityfunction.DensityFunction;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public record DensityBasedInfiniteStructure(int minY, int maxY, int fac, RegistryEntry<DensityFunction> densityFunctionEntry,
                                            Map<String, Pair<Double, Double>> values, List<DensityBasedInfiniteStructure.Structure> structures) implements InfiniteStructure{

    public static final Codec<Either<Identifier, StructureTemplate>> LOCATION_CODEC = Codec.of(
            DensityBasedInfiniteStructure.Structure::encodeLocation, Identifier.CODEC.map(Either::left));

    public static final MapCodec<DensityBasedInfiniteStructure.Structure> STRUCTURE_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    LOCATION_CODEC.fieldOf("location").forGetter(DensityBasedInfiniteStructure.Structure::location),
                    BlockPos.CODEC.fieldOf("size").forGetter(DensityBasedInfiniteStructure.Structure::size),
                    BlockPos.CODEC.fieldOf("offset").forGetter(DensityBasedInfiniteStructure.Structure::offset),
                    Codec.STRING.listOf().fieldOf("grid").forGetter(DensityBasedInfiniteStructure.Structure::grid3D))
                    .apply(instance, DensityBasedInfiniteStructure.Structure::new));

    public static final MapCodec<DensityBasedInfiniteStructure> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("min_y").forGetter(DensityBasedInfiniteStructure::minY),
                    Codec.INT.fieldOf("max_y").forGetter(DensityBasedInfiniteStructure::maxY),
                    Codec.intRange(2, 4).fieldOf("factor").forGetter(DensityBasedInfiniteStructure::fac),
                    DensityFunction.REGISTRY_ENTRY_CODEC.fieldOf("density_function").forGetter(DensityBasedInfiniteStructure::densityFunctionEntry),
                    Codec.unboundedMap(Codec.STRING,
                            Codec.pair(Codec.DOUBLE.fieldOf("minimum").codec(),
                                    Codec.DOUBLE.fieldOf("maximum").codec()))
                            .fieldOf("values").forGetter(DensityBasedInfiniteStructure::values),
                    STRUCTURE_CODEC.codec().listOf().fieldOf("structures").forGetter(DensityBasedInfiniteStructure::structures))
                    .apply(instance, DensityBasedInfiniteStructure::new));

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
                        if (structure.place(scale, pos, densityFunctionEntry.value(), structureTemplateManager, world, values)){

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
    public double getSample(BlockPos pos) {
        return densityFunctionEntry.value().sample(new DensityFunction.UnblendedNoisePos(pos.getX(), pos.getY(), pos.getZ()));
    }

    @Override
    public InfiniteStructureType<?> getType() {
        return InfiniteStructureTypes.DENSITY_BASED_STRUCTURE;
    }

    public record Structure(Either<Identifier, StructureTemplate> location, BlockPos size, BlockPos offset, List<String> grid3D) {

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

        private boolean place(int scale, BlockPos pos, DensityFunction densityFunction, StructureTemplateManager structureTemplateManager, ServerWorldAccess world, Map<String, Pair<Double, Double>> values) {
            if (size.getX() * size.getX() * size.getZ() > grid3D.size()) {
                return false;
            }

            int index = 0;
            for (int j = 0; j < size.getY(); j++) {
                for (int i = 0; i < size.getX(); i++) {
                    for (int k = 0; k < size.getZ(); k++) {
                        Pair<Double, Double> range = values.get(grid3D.get(index));
                        if (range != null) {
                            if (range.getFirst() > range.getSecond()) {
                                return false;
                            }

                            BlockPos gridLoc = offset.add(i, j, k);
                            BlockPos loc = pos.add(gridLoc.getX() * scale, gridLoc.getY() * scale, gridLoc.getZ() * scale);
                            double density = densityFunction.sample(new DensityFunction.UnblendedNoisePos(loc.getX(), loc.getY(), loc.getZ()));
                            if (density < range.getFirst() || density >= range.getSecond()) {
                                return false;
                            }
                        }

                        index++;
                    }
                }
            }


            //System.out.println("Printing Structure");

            StructureTemplate structureTemplate = getStructure(structureTemplateManager);
            StructurePlacementData structurePlacementData = new StructurePlacementData();
            structurePlacementData.setUpdateNeighbors(false);
            structurePlacementData.setIgnoreEntities(true);
            boolean is = structureTemplate.place(world, pos, pos, structurePlacementData, world.getRandom(), 2);
//            if (is)
//                System.out.println("Printing Structure");

            return is;
        }
    }
}
