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

public record LayeredGridDensityStructure(String name, RegistryEntry<StructurePool> basePool,
                                          RegistryEntry<StructurePool> middlePool,
                                          RegistryEntry<StructurePool> topPool, BlockPos boundingSize,
                                          BlockPos size, BlockPos location, int minY, int maxY, int yOffset,
                                          List<String> grid3D) implements DensityStructurePiece {

    public static final MapCodec<LayeredGridDensityStructure> LAYERED_GRID_STRUCTURE_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.STRING.optionalFieldOf("name", "unnamed").forGetter(LayeredGridDensityStructure::name),
                            StructurePool.REGISTRY_CODEC.fieldOf("base_elements").forGetter(structure -> structure.basePool),
                            StructurePool.REGISTRY_CODEC.fieldOf("middle_elements").forGetter(structure -> structure.middlePool),
                            StructurePool.REGISTRY_CODEC.fieldOf("top_elements").forGetter(structure -> structure.topPool),
                            BlockPos.CODEC.fieldOf("bounding_size").forGetter(LayeredGridDensityStructure::boundingSize),
                            BlockPos.CODEC.optionalFieldOf("size", new BlockPos(1, 1, 1)).forGetter(LayeredGridDensityStructure::size),
                            BlockPos.CODEC.fieldOf("location").forGetter(LayeredGridDensityStructure::location),
                            Codec.INT.optionalFieldOf("min_y", 0).forGetter(LayeredGridDensityStructure::minY),
                            Codec.INT.fieldOf("max_y").forGetter(LayeredGridDensityStructure::maxY),
                            Codec.INT.optionalFieldOf("y_offset", 0).forGetter(LayeredGridDensityStructure::yOffset),
                            Codec.STRING.listOf().fieldOf("grid").forGetter(LayeredGridDensityStructure::grid3D))
                    .apply(instance, LayeredGridDensityStructure::new));





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
        StructurePool baseStructurePool = (StructurePool) basePool.getKey()
                .flatMap(key -> registry.getOrEmpty(aliasLookup.lookup(key)))
                .orElse(basePool.value());
        StructurePool middleStructurePool = (StructurePool) middlePool.getKey()
                .flatMap(key -> registry.getOrEmpty(aliasLookup.lookup(key)))
                .orElse(middlePool.value());
        StructurePool topStructurePool = (StructurePool) topPool.getKey()
                .flatMap(key -> registry.getOrEmpty(aliasLookup.lookup(key)))
                .orElse(topPool.value());

        BlockPos boundingScale = size.multiply(scale);

        if (boundingSize.getX() * boundingSize.getY() * boundingSize.getZ() > grid3D.size() ||
                maxY % boundingScale.getY() != 0 || minY % boundingScale.getY() != 0 ||
                maxY - boundingScale.getY() * 3 - yOffset * scale <= minY + yOffset * scale ||
                yOffset >= size.getY()) {
            throw new IndexOutOfBoundsException("Scaling out of bounds for structure " + getName());
        }

        if (pos.getY() > maxY - boundingScale.getY() - yOffset*scale || pos.getY() < minY + yOffset*scale) {
            return false;
        }

        BlockPos distanceFromOrigin = new BlockPos(Math.abs(pos.getX()) % boundingScale.getX(),
                Math.abs(pos.getY() + yOffset*scale) % boundingScale.getY(),
                Math.abs(pos.getZ()) % boundingScale.getZ());



        BlockPos origin = pos.subtract(distanceFromOrigin);

        BlockPos gridDistance = new BlockPos(Math.abs(origin.getX()) % (boundingScale.getX() * 2),
                0,
                Math.abs(origin.getZ()) % (boundingScale.getZ() * 2));

        if (!gridDistance.equals(BlockPos.ORIGIN) && !gridDistance.equals(boundingScale.add(0, -boundingScale.getY(), 0))) {
            return false;
        }

        boolean hasBase = false;
        BlockPos.Mutable basePos = new BlockPos.Mutable(origin.getX(),
                maxY - boundingScale.getY() * 3 - yOffset*scale, origin.getZ());

        for (int y = maxY - boundingScale.getY() * 3 - yOffset*scale; y >= minY + yOffset*scale; y -= boundingScale.getY()) {
            basePos.setY(y);
            if (checkPlace(basePos.toImmutable(), scale, values, densityFunction)) {

                if (DensityStructurePiece.checkStructure(basePos.toImmutable(), context.seed(),
                        random, baseStructurePool)) {
                    hasBase = true;
                    break;
                }
            }
        }

        if (!hasBase) {
            return false;
        }

        int rand1 = random.setValue(context.seed()).nextInt();
        int rand2 = random.setValue(rand1 + origin.getX()).nextInt();
        int setY = (random.setValue(rand2 + origin.getZ()).nextInt((maxY - basePos.getY() - yOffset*scale) / boundingScale.getY())+3) * boundingScale.getY();

        if (origin.getY() > setY + basePos.getY() || origin.getY() < basePos.getY()) {
            return false;
        }

        StructurePool templatePool = middleStructurePool;
        if (basePos.equals(origin)) {
            templatePool = baseStructurePool;
        } else if (setY + basePos.getY() == origin.getY() || origin.getY() == maxY - boundingScale.getY() - yOffset*scale) {
            templatePool = topStructurePool;
        }


        if (!distanceFromOrigin.equals(BlockPos.ORIGIN)) {

            return false;

//            return StructurePiece.checkStructure(origin, world.toServerWorld().getSeed(),
//                    random, templatePool);
        }

        if (!DensityStructurePiece.checkStructure(basePos.toImmutable(), context.seed(),
                random, templatePool)) {
            return false;
        }

        StructurePoolElement structurePoolElement = DensityStructurePiece.getStructure(pos, context.seed(), random, templatePool);

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
        return StructurePieceTypes.LAYERED_GRID;
    }
}
