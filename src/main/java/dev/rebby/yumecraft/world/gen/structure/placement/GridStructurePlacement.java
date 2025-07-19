package dev.rebby.yumecraft.world.gen.structure.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator;
import net.minecraft.world.gen.chunk.placement.StructurePlacementType;

import java.util.Optional;

public class GridStructurePlacement extends StructurePlacement {

    public static final MapCodec<GridStructurePlacement> CODEC = RecordCodecBuilder.mapCodec(
            instance -> buildCodec(instance).and(
                    Codec.intRange(1, 4096).optionalFieldOf("separation", 1).forGetter(GridStructurePlacement::getSeparation)
            ).apply(instance, GridStructurePlacement::new));

    private final int separation;

    public GridStructurePlacement(Vec3i locateOffset, FrequencyReductionMethod frequencyReductionMethod,
                                  float frequency, int salt, Optional<StructurePlacement.ExclusionZone> exclusionZone,
                                  int separation) {
        super(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone);
        this.separation = separation;
    }

    public int getSeparation() {
        return separation;
    }

    @Override
    protected boolean isStartChunk(StructurePlacementCalculator calculator, int chunkX, int chunkZ) {
        return chunkX % separation == 0 && chunkZ % separation == 0;
    }

    @Override
    public StructurePlacementType<?> getType() {
        return ModStructurePlacementTypes.GRID;
    }
}
