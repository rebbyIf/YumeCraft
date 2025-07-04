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

public record AlternatingStructure(String name, DataPool<Either<Identifier, StructureTemplate>> structurePool,
                                   BlockPos boundingSize,
                                   BlockPos size, BlockPos location, List<String> grid3D) implements StructurePiece {

    public static final MapCodec<AlternatingStructure> ALTERNATING_STRUCTURE_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codec.STRING.optionalFieldOf("name", "unnamed").forGetter(AlternatingStructure::name),
                            DataPool.createCodec(StructurePiece.STRUCTURE_TEMPLATE_CODEC).fieldOf("elements").forGetter(AlternatingStructure::structurePool),
                            BlockPos.CODEC.fieldOf("bounding_size").forGetter(AlternatingStructure::boundingSize),
                            BlockPos.CODEC.optionalFieldOf("size", new BlockPos(1, 1, 1)).forGetter(AlternatingStructure::size),
                            BlockPos.CODEC.fieldOf("location").forGetter(AlternatingStructure::location),
                            Codec.STRING.listOf().fieldOf("grid").forGetter(AlternatingStructure::grid3D))
                    .apply(instance, AlternatingStructure::new));


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
        if (boundingSize.getX() * boundingSize.getY() * boundingSize.getZ() > grid3D.size()) {
            return "Bounding Size out of bounds!";
        }


        BlockPos boundingSize = size.multiply(scale);

        BlockPos distanceFromOrigin = new BlockPos(Math.abs(pos.getX()) % boundingSize.getX(),
                Math.abs(pos.getY()) % boundingSize.getY(),
                Math.abs(pos.getZ()) % boundingSize.getZ());


        int rand1 = random.setValue(seed).nextInt();
        int rand2 = random.setValue(rand1 + pos.getX()).nextInt();
        int offset = random.setValue(rand2 + pos.getZ()).nextInt(size.getY()) * scale;

        distanceFromOrigin = new BlockPos(distanceFromOrigin.getX(),
                (distanceFromOrigin.getY() + offset) % boundingSize.getY(),
                distanceFromOrigin.getZ());


        if (!distanceFromOrigin.equals(BlockPos.ORIGIN)) {

            if (!checkPlace(pos.subtract(distanceFromOrigin), scale, values, densityFunction)) {
                return "Origin does not match the grid!";
            }

            if (!StructurePiece.checkStructure(pos.subtract(distanceFromOrigin), seed,
                    random, structurePool))
                return "Original structure is empty!";

            return DensityBasedInfiniteStructure.STRUCTURE_ALREADY_PLACED;
        }

        if (!checkPlace(pos, scale, values, densityFunction)) {
            return "This place does not match the grid!";
        }

        if (!StructurePiece.checkStructure(pos.subtract(distanceFromOrigin), seed,
                random, structurePool)) {
            return "No structure found!";
        }

        return DensityBasedInfiniteStructure.STRUCTURE_CAN_BE_PLACED;
    }

    public boolean place(int scale, BlockPos pos, DensityFunction densityFunction,
                         StructureTemplateManager structureTemplateManager, ServerWorldAccess world,
                         Map<String, CheckValue> values, NotRandom random) {

        if (boundingSize.getX() * boundingSize.getY() * boundingSize.getZ() > grid3D.size()) {
            throw new IndexOutOfBoundsException("Scaling out of bounds for structure " + getName());
        }

        BlockPos boundingSize = size.multiply(scale);

        BlockPos distanceFromOrigin = new BlockPos(Math.abs(pos.getX()) % boundingSize.getX(),
                Math.abs(pos.getY()) % boundingSize.getY(),
                Math.abs(pos.getZ()) % boundingSize.getZ());

        int rand1 = random.setValue(world.toServerWorld().getSeed()).nextInt();
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

        if (!StructurePiece.checkStructure(pos, world.toServerWorld().getSeed(),
                random, structurePool)) {
            return false;
        }

        StructureTemplate structureTemplate = StructurePiece.getStructure(structureTemplateManager, pos, world.toServerWorld().getSeed(), random, structurePool);


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
        return StructurePieceTypes.ALTERNATING;
    }
}
