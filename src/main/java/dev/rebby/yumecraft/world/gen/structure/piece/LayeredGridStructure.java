package dev.rebby.yumecraft.world.gen.structure.piece;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.rebby.yumecraft.util.NotRandom;
import dev.rebby.yumecraft.world.gen.structure.DensityBasedInfiniteStructure;
import dev.rebby.yumecraft.world.gen.structure.check_value.CheckValue;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public record LayeredGridStructure(String name, DataPool<Either<Identifier, StructureTemplate>> basePool,
                                   DataPool<Either<Identifier, StructureTemplate>> middlePool,
                                   DataPool<Either<Identifier, StructureTemplate>> topPool, BlockPos boundingSize,
                                   BlockPos size, BlockPos location, int minY, int maxY, int yOffset,
                                   List<String> grid3D) implements StructurePiece {

    public static final MapCodec<LayeredGridStructure> LAYERED_GRID_STRUCTURE_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.STRING.optionalFieldOf("name", "unnamed").forGetter(LayeredGridStructure::name),
                            DataPool.createCodec(StructurePiece.STRUCTURE_TEMPLATE_CODEC).fieldOf("base_elements").forGetter(LayeredGridStructure::basePool),
                            DataPool.createCodec(StructurePiece.STRUCTURE_TEMPLATE_CODEC).fieldOf("middle_elements").forGetter(LayeredGridStructure::middlePool),
                            DataPool.createCodec(StructurePiece.STRUCTURE_TEMPLATE_CODEC).fieldOf("top_elements").forGetter(LayeredGridStructure::topPool),
                            BlockPos.CODEC.fieldOf("bounding_size").forGetter(LayeredGridStructure::boundingSize),
                            BlockPos.CODEC.optionalFieldOf("size", new BlockPos(1, 1, 1)).forGetter(LayeredGridStructure::size),
                            BlockPos.CODEC.fieldOf("location").forGetter(LayeredGridStructure::location),
                            Codec.INT.optionalFieldOf("min_y", 0).forGetter(LayeredGridStructure::minY),
                            Codec.INT.fieldOf("max_y").forGetter(LayeredGridStructure::maxY),
                            Codec.INT.optionalFieldOf("y_offset", 0).forGetter(LayeredGridStructure::yOffset),
                            Codec.STRING.listOf().fieldOf("grid").forGetter(LayeredGridStructure::grid3D))
                    .apply(instance, LayeredGridStructure::new));





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

    public String canPlace(int scale, BlockPos pos, DensityFunction densityFunction,
                           StructureTemplateManager structureTemplateManager, long seed,
                           Map<String, CheckValue> values, NotRandom random) {

        BlockPos boundingScale = size.multiply(scale);

        if (boundingSize.getX() * boundingSize.getY() * boundingSize.getZ() > grid3D.size() ||
                maxY % boundingScale.getY() != 0 || minY % boundingScale.getY() != 0 ||
                maxY - boundingScale.getY() * 3 - yOffset * scale <= minY + yOffset * scale ||
                yOffset >= size.getY()) {
            throw new IndexOutOfBoundsException("Scaling out of bounds for structure " + getName());
        }

        if (pos.getY() > maxY - boundingScale.getY() - yOffset*scale || pos.getY() < minY + yOffset*scale) {
            return "Structure is out of bounds!";
        }

        BlockPos distanceFromOrigin = new BlockPos(Math.abs(pos.getX()) % boundingScale.getX(),
                Math.abs(pos.getY() + yOffset*scale) % boundingScale.getY(),
                Math.abs(pos.getZ()) % boundingScale.getZ());



        BlockPos origin = pos.subtract(distanceFromOrigin);

        BlockPos gridDistance = new BlockPos(Math.abs(origin.getX()) % (boundingScale.getX() * 2),
                0,
                Math.abs(origin.getZ()) % (boundingScale.getZ() * 2));

        if (!gridDistance.equals(BlockPos.ORIGIN) && !gridDistance.equals(boundingScale.add(0, -boundingScale.getY(), 0))) {
            return "Structure not on the grid!";
        }

        boolean hasBase = false;
        BlockPos.Mutable basePos = new BlockPos.Mutable(origin.getX(),
                maxY - boundingScale.getY() * 3 - yOffset*scale, origin.getZ());

        for (int y = maxY - boundingScale.getY() * 3 - yOffset*scale; y >= minY + yOffset*scale; y -= boundingScale.getY()) {
            basePos.setY(y);
            if (checkPlace(basePos.toImmutable(), scale, values, densityFunction)) {

                if (StructurePiece.checkStructure(basePos.toImmutable(), seed,
                        random, basePool)) {
                    hasBase = true;
                    break;
                }
            }
        }

        if (!hasBase) {
            return "Base not found for structure";
        }

        int rand1 = random.setValue(seed).nextInt();
        int rand2 = random.setValue(rand1 + origin.getX()).nextInt();
        int setY = (random.setValue(rand2 + origin.getZ()).nextInt((maxY - basePos.getY() - yOffset*scale) / boundingScale.getY())+3) * boundingScale.getY();

        if (origin.getY() > setY + basePos.getY() || origin.getY() < basePos.getY()) {
            return "Structure piece not in range!";
        }

        DataPool<Either<Identifier, StructureTemplate>> templatePool = middlePool;
        if (basePos.equals(origin)) {
            templatePool = basePool;
        } else if (setY + basePos.getY() == origin.getY() || origin.getY() == maxY - boundingScale.getY() - yOffset*scale) {
            templatePool = topPool;
        }


        if (!distanceFromOrigin.equals(BlockPos.ORIGIN)) {

            if (!StructurePiece.checkStructure(basePos.toImmutable(), seed,
                    random, templatePool)) {
                return "Structure is not generated!";
            }

            return DensityBasedInfiniteStructure.STRUCTURE_ALREADY_PLACED;
        }

        if (!StructurePiece.checkStructure(basePos.toImmutable(), seed,
                random, templatePool)) {
            return "No structure found!";
        }

        return DensityBasedInfiniteStructure.STRUCTURE_CAN_BE_PLACED;
    }

    public boolean place(int scale, BlockPos pos, DensityFunction densityFunction,
                         StructureTemplateManager structureTemplateManager, ServerWorldAccess world,
                         Map<String, CheckValue> values, NotRandom random) {

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

                if (StructurePiece.checkStructure(basePos.toImmutable(), world.toServerWorld().getSeed(),
                        random, basePool)) {
                    hasBase = true;
                    break;
                }
            }
        }

        if (!hasBase) {
            return false;
        }

        int rand1 = random.setValue(world.toServerWorld().getSeed()).nextInt();
        int rand2 = random.setValue(rand1 + origin.getX()).nextInt();
        int setY = (random.setValue(rand2 + origin.getZ()).nextInt((maxY - basePos.getY() - yOffset*scale) / boundingScale.getY())+3) * boundingScale.getY();

        if (origin.getY() > setY + basePos.getY() || origin.getY() < basePos.getY()) {
            return false;
        }

        DataPool<Either<Identifier, StructureTemplate>> templatePool = middlePool;
        if (basePos.equals(origin)) {
            templatePool = basePool;
        } else if (setY + basePos.getY() == origin.getY() || origin.getY() == maxY - boundingScale.getY() - yOffset*scale) {
            templatePool = topPool;
        }


        if (!distanceFromOrigin.equals(BlockPos.ORIGIN)) {

            return false;

//            return StructurePiece.checkStructure(origin, world.toServerWorld().getSeed(),
//                    random, templatePool);
        }

        if (!StructurePiece.checkStructure(basePos.toImmutable(), world.toServerWorld().getSeed(),
                random, templatePool)) {
            return false;
        }

        StructureTemplate structureTemplate = StructurePiece.getStructure(structureTemplateManager, pos, world.toServerWorld().getSeed(), random, templatePool);


        //System.out.println("Printing Structure");
        StructurePlacementData structurePlacementData = new StructurePlacementData();
        structurePlacementData.setUpdateNeighbors(false);
        structurePlacementData.setIgnoreEntities(true);

        return structureTemplate.place(world, pos, pos, structurePlacementData, world.getRandom(), 1);
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
