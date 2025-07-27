package dev.rebby.yumecraft.world.gen.structure.piece;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.rebby.yumecraft.util.NotRandom;
import dev.rebby.yumecraft.world.gen.structure.check_value.CheckValue;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.alias.StructurePoolAliasLookup;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.structure.Structure;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public record AlternatingDensityStructure(String name, RegistryEntry<StructurePool> pool,
                                          BlockPos boundingSize,
                                          BlockPos size, BlockPos location, List<String> grid3D) implements DensityStructurePiece {

    public static final MapCodec<AlternatingDensityStructure> ALTERNATING_STRUCTURE_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.STRING.optionalFieldOf("name", "unnamed").forGetter(AlternatingDensityStructure::name),
                            StructurePool.REGISTRY_CODEC.fieldOf("elements").forGetter(structure -> structure.pool),
                            BlockPos.CODEC.fieldOf("bounding_size").forGetter(AlternatingDensityStructure::boundingSize),
                            BlockPos.CODEC.optionalFieldOf("size", new BlockPos(1, 1, 1)).forGetter(AlternatingDensityStructure::size),
                            BlockPos.CODEC.fieldOf("location").forGetter(AlternatingDensityStructure::location),
                            Codec.STRING.listOf().fieldOf("grid").forGetter(AlternatingDensityStructure::grid3D))
                    .apply(instance, AlternatingDensityStructure::new));


    private boolean checkPlace(BlockPos pos, int scale, Map<String, CheckValue> values, DensityFunction densityFunction) {
        BlockPos min = pos.subtract(location.multiply(scale));
        BlockPos max = min.add(boundingSize.multiply(scale));

        int index = 0;
        for (int j = 0; j < boundingSize.getY(); j++) {
            for (int i = 0; i < boundingSize.getX(); i++) {
                for (int k = 0; k < boundingSize.getZ(); k++) {
                    CheckValue value = values.get(grid3D.get(index));
                    if (value != null) {
                        BlockPos gridLoc = location.multiply(-1).add(i, j, k).multiply(scale);
                        BlockPos loc = pos.add(gridLoc);
                        boolean result = value.check(densityFunction, loc, min, max, scale);
                        if (!result && value.doesOverrideIfFalse()) {
                            return false;
                        }

                        if (result && value.doesOverrideIfTrue()) {
                            return true;
                        }


                    }

                    index++;
                }
            }
        }
        return true;
    }

    @Override
    public boolean place(List<PoolStructurePiece> pieces, int scale, BlockPos pos, DensityFunction densityFunction,
                         StructureTemplateManager structureTemplateManager, Structure.Context context,
                         Map<String, CheckValue> values, NotRandom random, StructurePoolAliasLookup aliasLookup,
                         StructureLiquidSettings liquidSettings) {

        DynamicRegistryManager dynamicRegistryManager = context.dynamicRegistryManager();
        Registry<StructurePool> registry = dynamicRegistryManager.get(RegistryKeys.TEMPLATE_POOL);
        StructurePool structurePool = (StructurePool) pool.getKey()
                .flatMap(key -> registry.getOrEmpty(aliasLookup.lookup(key)))
                .orElse(pool.value());

        if (boundingSize.getX() * boundingSize.getY() * boundingSize.getZ() > grid3D.size()) {
            throw new IndexOutOfBoundsException("Scaling out of bounds for structure " + getName());
        }

        BlockPos boundingSize = size.multiply(scale);

        BlockPos distanceFromOrigin = new BlockPos(Math.abs(pos.getX()) % boundingSize.getX(),
                Math.abs(pos.getY()) % boundingSize.getY(),
                Math.abs(pos.getZ()) % boundingSize.getZ());

        int rand1 = random.setValue(context.seed()).nextInt();
        int rand2 = random.setValue(rand1 + pos.getX()).nextInt();
        int offset = random.setValue(rand2 + pos.getZ()).nextInt(size.getY()) * scale;

        distanceFromOrigin = new BlockPos(distanceFromOrigin.getX(),
                (distanceFromOrigin.getY() + offset) % boundingSize.getY(),
                distanceFromOrigin.getZ());


        if (!distanceFromOrigin.equals(BlockPos.ORIGIN)) {

            return false;

//            return checkPlace(pos.subtract(distanceFromOrigin), scale, values, densityFunction) &&
//                    StructurePiece.checkStructure(pos.subtract(distanceFromOrigin), world.toServerWorld().getSeed(),
//                            random, structurePool);
        }

        if (!checkPlace(pos, scale, values, densityFunction)) {
            return false;
        }

        if (!DensityStructurePiece.checkStructure(pos, context.seed(),
                random, structurePool)) {
            return false;
        }

        StructurePoolElement structurePoolElement = DensityStructurePiece.getStructure(pos, context.seed(), random, structurePool);

        pieces.add(new PoolStructurePiece(
                context.structureTemplateManager(),
                structurePoolElement,
                pos,
                structurePoolElement.getGroundLevelDelta(),
                BlockRotation.NONE,
                structurePoolElement.getBoundingBox(context.structureTemplateManager(), pos, BlockRotation.NONE),
                liquidSettings));

        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public @NotNull BlockPos getSize() {
        return size;
    }

    @Override
    public StructurePieceType<?> getType() {
        return StructurePieceTypes.ALTERNATING;
    }
}
