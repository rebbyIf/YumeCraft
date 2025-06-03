package dev.rebby.yumecraft.world.gen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.rebby.yumecraft.util.NotRandom;
import dev.rebby.yumecraft.util.PCGRandom;
import dev.rebby.yumecraft.world.gen.structure.check_value.CheckValue;
import dev.rebby.yumecraft.world.gen.structure.piece.StructurePiece;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.text.DecimalFormat;
import java.util.*;

public final class DensityBasedInfiniteStructure implements InfiniteStructure {

    public static final MapCodec<DensityBasedInfiniteStructure> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.INT.fieldOf("min_y").forGetter(DensityBasedInfiniteStructure::minY),
                            Codec.INT.fieldOf("max_y").forGetter(DensityBasedInfiniteStructure::maxY),
                            Codec.intRange(2, 4).fieldOf("factor").forGetter(DensityBasedInfiniteStructure::fac),
                            DensityFunction.REGISTRY_ENTRY_CODEC.fieldOf("density_function").forGetter(DensityBasedInfiniteStructure::densityFunctionEntry),
                            Codec.unboundedMap(Codec.STRING, CheckValue.VALUE_CODEC).fieldOf("values").forGetter(DensityBasedInfiniteStructure::values),
                            StructurePiece.STRUCTURE_PIECE_CODEC.listOf().fieldOf("structures").forGetter(DensityBasedInfiniteStructure::structures))
                    .apply(instance, DensityBasedInfiniteStructure::new));

    public static final String STRUCTURE_ALREADY_PLACED = "This structure has already been placed!";
    public static final String STRUCTURE_CAN_BE_PLACED = "Structure can be placed!";

    private final int minY;
    private final int maxY;
    private final int fac;
    private final RegistryEntry<DensityFunction> densityFunctionEntry;
    private final Map<String, CheckValue> values;
    private final List<StructurePiece> structures;
    private final NotRandom setRandom;

    public DensityBasedInfiniteStructure(int minY, int maxY, int fac, RegistryEntry<DensityFunction> densityFunctionEntry,
                                         Map<String, CheckValue> values, List<StructurePiece> structures) {
        this.minY = minY;
        this.maxY = maxY;
        this.fac = fac;
        this.densityFunctionEntry = densityFunctionEntry;
        this.values = values;
        this.structures = structures;
        this.setRandom = new PCGRandom(0);
    }

    @Override
    public void generate(NoiseConfig noiseConfig, StructureTemplateManager structureTemplateManager, ServerWorldAccess world, Chunk chunk) {

//        int rand1 = (int) setRandom.setValue(world.toServerWorld().getSeed()).nextLong();
//        int rand2 = (int) setRandom.setValue(rand1 + chunk.getPos().x).nextLong();
//        int rand3 = setRandom.setValue(rand2 + chunk.getPos().z).nextInt(2);
//
//        System.out.println("Random set at "+rand3);

        DensityFunction densityFunction = densityFunctionEntry.value();

        int scale = (int) Math.pow(2, fac);

        boolean [][][] completed = new boolean[16/scale][(maxY-minY)/scale][16/scale];

        for (int s = 0; s < structures.size(); s++) {

            StructurePiece structure = structures.get(s);

            BlockPos size = structure.getSize();

            int i = 0;
            for (int x = 0; x < 16; x += scale) {
                int j = 0;
                for (int y = minY; y < maxY; y += scale) {
                    int k = 0;
                    for (int z = 0; z < 16; z += scale) {

                        label : {
                            if (i + size.getX() > completed.length ||
                                    j + size.getY() > completed[0].length ||
                                    k + size.getZ() > completed[0][0].length) {
                                break label;
                            }

                            for (int xp = 0; xp < size.getX(); xp++) {
                                for (int yp = 0; yp < size.getY(); yp++) {
                                    for (int zp = 0; zp < size.getZ(); zp++) {
                                        if (completed[i + xp][j + yp][k + zp]) {
                                            break label;
                                        }
                                    }
                                }
                            }

                            BlockPos pos = chunk.getPos().getStartPos().add(x, y, z);

                            if (structure.place(scale, pos, densityFunction, structureTemplateManager, world, values, setRandom)) {

                                for (int xp = 0; xp < size.getX(); xp++) {
                                    for (int yp = 0; yp < size.getY(); yp++) {
                                        for (int zp = 0; zp < size.getZ(); zp++) {
                                            completed[i + xp][j + yp][k + zp] = true;
                                        }
                                    }
                                }

                            }
                        }
                        k++;
                    }
                    j++;
                }
                i++;

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
    public void generateDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos, StructureTemplateManager structureTemplateManager, long seed) {
        DecimalFormat decimalFormat = new DecimalFormat("0.000");
        text.add("Infinite structure density: "+ decimalFormat.format(getSample(pos)));

        int scale = (int) Math.pow(2, fac);
        BlockPos startPos = new BlockPos(pos.getX() / scale, pos.getY() / scale, pos.getZ() / scale).multiply(scale);

        String output = "No structures!";
        String name = "unnamed";
        for (StructurePiece structure : structures) {
             output = structure.canPlace(scale, startPos, densityFunctionEntry.value(),
                    structureTemplateManager, seed, values, setRandom);
             name = structure.getName();

            if (output.equals(STRUCTURE_ALREADY_PLACED) || output.equals(STRUCTURE_CAN_BE_PLACED)) {
                break;
            }
        }

        text.add(name + ": " + output);
    }

    @Override
    public MultiNoiseUtil.MultiNoiseSampler returnNoiseSampler() {
        DensityFunction densityFunction = densityFunctionEntry.value();
        return new MultiNoiseUtil.MultiNoiseSampler(densityFunction, densityFunction, densityFunction,
                densityFunction, densityFunction, densityFunction, new ArrayList<MultiNoiseUtil.NoiseHypercube>());
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

    public Map<String, CheckValue> values() {
        return values;
    }

    public List<StructurePiece> structures() {
        return structures;
    }


}
