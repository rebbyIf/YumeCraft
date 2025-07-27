package dev.rebby.yumecraft.world.gen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.rebby.yumecraft.util.NotRandom;
import dev.rebby.yumecraft.util.PCGRandom;
import dev.rebby.yumecraft.world.gen.structure.check_value.CheckValue;
import dev.rebby.yumecraft.world.gen.structure.piece.DensityStructurePiece;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureLiquidSettings;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.pool.alias.StructurePoolAliasLookup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DensityBasedStructure extends Structure {

    public static final MapCodec<DensityBasedStructure> CODEC = RecordCodecBuilder.<DensityBasedStructure>mapCodec(
            instance -> instance.group(
                    configCodecBuilder(instance),
                            Codec.INT.fieldOf("min_y").forGetter(structure -> structure.minY),
                            Codec.INT.fieldOf("max_y").forGetter(structure -> structure.maxY),
                            Codec.intRange(2, 4).fieldOf("factor").forGetter(structure -> structure.fac),
                            DensityFunction.FUNCTION_CODEC.fieldOf("density_function").forGetter(structure -> structure.densityFunction),
                            Codec.unboundedMap(Codec.STRING, CheckValue.VALUE_CODEC).fieldOf("values").forGetter(structure -> structure.values),
                            DensityStructurePiece.STRUCTURE_PIECE_CODEC.listOf().fieldOf("structures").forGetter(structure -> structure.structures),
                            StructureLiquidSettings.codec.optionalFieldOf("liquid_settings",
                                    StructureLiquidSettings.APPLY_WATERLOGGING).forGetter(structure -> structure.liquidSettings))
                    .apply(instance, DensityBasedStructure::new))
            .validate(DensityBasedStructure::validate);

    private static final Identifier WORLDGEN_REGION_RANDOM_ID = Identifier.ofVanilla("worldgen_region_random");

    private static DataResult<DensityBasedStructure> validate(DensityBasedStructure structure) {
        if (structure.minY >= structure.maxY) {
            return DataResult.error(() -> "Structure's min value must not be greater than or equal to it's max");
        }

        for (DensityStructurePiece piece : structure.structures) {
            BlockPos scale = piece.getSize().multiply((int) Math.pow(2, structure.fac));

            if (scale.getX() > 16 || scale.getZ() > 16 || scale.getY() > structure.maxY - structure.minY) {
                return DataResult.error(() -> "Piece "+piece.getName()+" was too big");
            }
        }

        return DataResult.success(structure);
    }

    private final int minY;
    private final int maxY;
    private final int fac;
    private final DensityFunction densityFunction;
    private DensityFunction densityFunctionImpl = null;
    private final Map<String, CheckValue> values;
    private final List<DensityStructurePiece> structures;
    private final NotRandom setRandom;
    private final StructureLiquidSettings liquidSettings;


    public DensityBasedStructure(Config config, int minY, int maxY, int fac,
                                 DensityFunction densityFunction, Map<String, CheckValue> values,
                                 List<DensityStructurePiece> structures, StructureLiquidSettings liquidSettings) {
        super(config);
        this.minY = minY;
        this.maxY = maxY;
        this.fac = fac;
        this.densityFunction = densityFunction;
        this.values = values;
        this.structures = structures;
        this.setRandom = new PCGRandom(0);
        this.liquidSettings = liquidSettings;
    }

    @Override
    public Optional<Structure.StructurePosition> getValidStructurePosition(Structure.Context context) {
        return getStructurePosition(context);
    }

    @Override
    protected Optional<StructurePosition> getStructurePosition(Context context) {
        ChunkPos chunkPos = context.chunkPos();
        BlockPos pos = new BlockPos(chunkPos.getStartX(), minY, chunkPos.getStartZ());
        return generate(context, context.structureTemplateManager(), pos);
    }

    private Optional<StructurePosition> generate(Context context, StructureTemplateManager structureTemplateManager, BlockPos startPos) {




        List<PoolStructurePiece> pieces = Lists.newArrayList();

        if (densityFunctionImpl == null) {
            DensityFunction.DensityFunctionVisitor visitor = new DensityFunction.DensityFunctionVisitor() {
                @Override
                public DensityFunction apply(DensityFunction densityFunction) {
                    return densityFunction instanceof DensityFunctionTypes.RegistryEntryHolder(
                            RegistryEntry<DensityFunction> function
                    ) ? function.value() : densityFunction;
                }

                public DensityFunction.Noise apply(DensityFunction.Noise noiseDensityFunction) {
                    RegistryEntry<DoublePerlinNoiseSampler.NoiseParameters> data = noiseDensityFunction.noiseData();
                    //Random random = context.noiseConfig().getOrCreateRandomDeriver(WORLDGEN_REGION_RANDOM_ID).split(context.chunkPos().getStartPos());

                    return new DensityFunction.Noise(data, context.noiseConfig().getOrCreateSampler(data.getKey().orElseThrow()));
                }
            };

            densityFunctionImpl = densityFunction.apply(visitor);
        }

        int scale = (int) Math.pow(2, fac);

        boolean [][][] completed = new boolean[16/scale][(maxY-minY)/scale][16/scale];

        boolean cantPlace = true;
        int i = 0;
        for (int x = 0; x < 16; x += scale) {
            int j = 0;
            for (int y = 0; y < maxY - minY; y += scale) {
                int k = 0;
                for (int z = 0; z < 16; z += scale) {
                    BlockPos pos = startPos.add(x,y,z);
                    boolean biomeTest = context.biomePredicate().test(
                            context.biomeSource().getBiome(
                                    BiomeCoords.fromBlock(pos.getX()),
                                    BiomeCoords.fromBlock(pos.getY()),
                                    BiomeCoords.fromBlock(pos.getZ()),
                                    context.noiseConfig().getMultiNoiseSampler()
                            )
                    );
                    if (biomeTest) {
                        cantPlace = false;
                    }

                    completed[i][j][k] = !biomeTest;

                    k++;
                }
                j++;

            }
            i++;
        }

        if (cantPlace) {
            return Optional.empty();
        }

        for (DensityStructurePiece structure : structures) {

            BlockPos size = structure.getSize();

            i = 0;
            for (int x = 0; x < 16; x += scale) {
                int j = 0;
                for (int y = 0; y < maxY - minY; y += scale) {
                    int k = 0;
                    for (int z = 0; z < 16; z += scale) {

                        label:
                        {
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

                            BlockPos pos = startPos.add(x, y, z);

                            if (structure.place(pieces, scale, pos, densityFunctionImpl, structureTemplateManager,
                                    context, values, setRandom, StructurePoolAliasLookup.create(List.of(), pos,
                                            context.seed()), liquidSettings)) {

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

        if (pieces.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new StructurePosition(startPos, collector -> {
            for (PoolStructurePiece piece : pieces) {
                collector.addPiece(piece);
            }
        }));
    }

    @Override
    public StructureType<?> getType() {
        return ModStructureTypes.DENSITY_BASED_STRUCTURE;
    }
}
