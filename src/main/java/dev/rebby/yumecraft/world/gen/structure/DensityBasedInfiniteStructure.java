package dev.rebby.yumecraft.world.gen.structure;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.rebby.yumecraft.util.NotRandom;
import dev.rebby.yumecraft.util.PCGRandom;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public final class DensityBasedInfiniteStructure implements InfiniteStructure {

    public static final Codec<Either<Identifier, StructureTemplate>> STRUCTURE_TEMPLATE_CODEC = Codec.of(
            Structure::encodeLocation, Identifier.CODEC.map(Either::left));

    public static final MapCodec<Structure> STRUCTURE_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            DataPool.createCodec(STRUCTURE_TEMPLATE_CODEC).fieldOf("elements").forGetter(Structure::structurePool),
                            BlockPos.CODEC.fieldOf("bounding_size").forGetter(Structure::boundingSize),
                            BlockPos.CODEC.optionalFieldOf("size", new BlockPos(1,1,1)).forGetter(Structure::size),
                            BlockPos.CODEC.fieldOf("location").forGetter(Structure::location),
                            Codec.BOOL.optionalFieldOf("alternate", false).forGetter(Structure::alternate),
                            Codec.STRING.listOf().fieldOf("grid").forGetter(Structure::grid3D))
                    .apply(instance, Structure::new));

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
    private final int minY;
    private final int maxY;
    private final int fac;
    private final RegistryEntry<DensityFunction> densityFunctionEntry;
    private final Map<String, Pair<Double, Double>> values;
    private final List<Structure> structures;
    private final NotRandom setRandom;

    public DensityBasedInfiniteStructure(int minY, int maxY, int fac, RegistryEntry<DensityFunction> densityFunctionEntry,
                                         Map<String, Pair<Double, Double>> values, List<Structure> structures) {
        this.minY = minY;
        this.maxY = maxY;
        this.fac = fac;
        this.densityFunctionEntry = densityFunctionEntry;
        this.values = values;
        this.structures = structures;
        this.setRandom = new PCGRandom(0);
    }

    @Override
    public void generate(StructureTemplateManager structureTemplateManager, ServerWorldAccess world, Chunk chunk) {

        int scale = (int) Math.pow(2, fac);

//        setRandom.setValue(chunk.getPos().x);
//        int random1 = setRandom.nextInt();
//        setRandom.setValue(chunk.getPos().z + random1);
        //System.out.println("Random value: "+setRandom.nextInt());

        for (int x = 0; x < 16; x += scale) {
            for (int y = minY; y < maxY; y += scale) {
                for (int z = 0; z < 16; z += scale) {
                    BlockPos pos = chunk.getPos().getStartPos().add(x, y, z);
                    double density = densityFunctionEntry.value().sample(new DensityFunction.UnblendedNoisePos(pos.getX(), pos.getY(), pos.getZ()));
                    //System.out.println("Y = "+pos.getY()+", density: "+density);
                    for (Structure structure : structures) {
                        if (structure.place(scale, pos, densityFunctionEntry.value(), structureTemplateManager, world, values, setRandom)) {

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

    public int minY() {
        return minY;
    }

    public int maxY() {
        return maxY;
    }

    public int fac() {
        return fac;
    }

    public RegistryEntry<DensityFunction> densityFunctionEntry() {
        return densityFunctionEntry;
    }

    public Map<String, Pair<Double, Double>> values() {
        return values;
    }

    public List<Structure> structures() {
        return structures;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DensityBasedInfiniteStructure) obj;
        return this.minY == that.minY &&
                this.maxY == that.maxY &&
                this.fac == that.fac &&
                Objects.equals(this.densityFunctionEntry, that.densityFunctionEntry) &&
                Objects.equals(this.values, that.values) &&
                Objects.equals(this.structures, that.structures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minY, maxY, fac, densityFunctionEntry, values, structures);
    }

    @Override
    public String toString() {
        return "DensityBasedInfiniteStructure[" +
                "minY=" + minY + ", " +
                "maxY=" + maxY + ", " +
                "fac=" + fac + ", " +
                "densityFunctionEntry=" + densityFunctionEntry + ", " +
                "values=" + values + ", " +
                "structures=" + structures + ']';
    }


    public record Structure(DataPool<Either<Identifier, StructureTemplate>> structurePool, BlockPos boundingSize,
                            BlockPos size, BlockPos location, boolean alternate, List<String> grid3D) {

        // Copied from minecraft's source
        private static <T> DataResult<T> encodeLocation(Either<Identifier, StructureTemplate> location, DynamicOps<T> ops, T prefix) {
            Optional<Identifier> optional = location.left();
            return optional.isEmpty()
                    ? DataResult.error(() -> "Can not serialize a runtime pool element")
                    : Identifier.CODEC.encode(optional.get(), ops, prefix);
        }

        @Nullable
        private StructureTemplate getStructure(StructureTemplateManager structureTemplateManager, BlockPos pos, long seed, NotRandom random) {
            int rand1 = (int) random.setValue(seed).nextLong();
            int rand2 = (int) random.setValue(rand1 + pos.getX()).nextLong();
            int rand3 = (int) random.setValue(rand2 + pos.getY()).nextLong();
            random.setValue(rand3 + pos.getZ());

            Optional<Either<Identifier, StructureTemplate>> optionalStructure = structurePool.getDataOrEmpty(random);
            return optionalStructure.map(identifierStructureTemplateEither -> identifierStructureTemplateEither.map(structureTemplateManager::getTemplateOrBlank, Function.identity())).orElse(null);
        }

        private boolean checkPlace(BlockPos pos, int scale, Map<String, Pair<Double, Double>> values, DensityFunction densityFunction) {
            int index = 0;
            for (int j = 0; j < boundingSize.getY(); j++) {
                for (int i = 0; i < boundingSize.getX(); i++) {
                    for (int k = 0; k < boundingSize.getZ(); k++) {
                        Pair<Double, Double> range = values.get(grid3D.get(index));
                        if (range != null) {
                            if (range.getFirst() > range.getSecond()) {
                                return false;
                            }

                            BlockPos gridLoc = location.multiply(-1).add(i, j, k);
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
            return true;
        }

        private boolean place(int scale, BlockPos pos, DensityFunction densityFunction,
                              StructureTemplateManager structureTemplateManager, ServerWorldAccess world,
                              Map<String, Pair<Double, Double>> values, NotRandom random) {

            if (boundingSize.getX() * boundingSize.getX() * boundingSize.getZ() > grid3D.size()) {
                return false;
            }

//            for (Weighted.Present<Either<Identifier, StructureTemplate>> entry : structurePool.getEntries()) {
//                if (entry.data().right().isPresent()) {
//                    StructureTemplate template = entry.data().right().get();
//                    BlockPos boundingSize = new BlockPos(Math.ceilDiv(template.getSize().getX(), scale),
//                            Math.ceilDiv(template.getSize().getY(), scale),
//                            Math.ceilDiv(template.getSize().getZ(), scale))
//                            .multiply(scale);
//
//                    BlockPos distanceFromOrigin = new BlockPos(pos.getX() % boundingSize.getX(),
//                            pos.getY() % boundingSize.getY(),
//                            pos.getZ() % boundingSize.getZ());
//
//                    BlockPos origin = pos.subtract(distanceFromOrigin);
//                } else {
//                    System.out.println("Error: No entry found!");
//                }
//            }



            BlockPos boundingSize = size.multiply(scale);

            BlockPos distanceFromOrigin = new BlockPos(pos.getX() % boundingSize.getX(),
                    pos.getY() % boundingSize.getY(),
                    pos.getZ() % boundingSize.getZ());

            if (alternate) {
                int rand1 = random.setValue(world.toServerWorld().getSeed()).nextInt();
                int rand2 = random.setValue(rand1 + pos.getX()).nextInt();
                int offset = random.setValue(rand2 + pos.getZ()).nextInt(size.getY()) * scale;

                distanceFromOrigin = new BlockPos(distanceFromOrigin.getX(),
                        (distanceFromOrigin.getY() + offset) % boundingSize.getY(),
                        distanceFromOrigin.getZ());
            }


            if (!distanceFromOrigin.equals(BlockPos.ORIGIN)) {
                StructureTemplate originStructure = getStructure(structureTemplateManager, pos.subtract(distanceFromOrigin), world.toServerWorld().getSeed(), random);

                //                    System.out.println("Distance from origin: "+ distanceFromOrigin);
                //                    if (originStructure == null) {
                //                        System.out.println("Original Structure null!");
                //                    } else {
                //                        System.out.println("Original structure size: " + originStructure.getSize());
                //                    }
                return checkPlace(pos.subtract(distanceFromOrigin), scale, values, densityFunction) &&
                        originStructure != null && !originStructure.getSize().equals(Vec3i.ZERO);
            }

            if (!checkPlace(pos, scale, values, densityFunction)) {
                return false;
            }

            StructureTemplate structureTemplate = getStructure(structureTemplateManager, pos, world.toServerWorld().getSeed(), random);

            if (structureTemplate == null) {
                System.out.println("ERROR: No structure found!");
                return false;
            }

            if (structureTemplate.getSize().equals(Vec3i.ZERO)) {
                return false;
            }


            //System.out.println("Printing Structure");
            StructurePlacementData structurePlacementData = new StructurePlacementData();
            structurePlacementData.setUpdateNeighbors(false);
            structurePlacementData.setIgnoreEntities(true);

            boolean is = structureTemplate.place(world, pos, pos, structurePlacementData, world.getRandom(), 2);

            return is;
        }
    }
}
