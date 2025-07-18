package dev.rebby.yumecraft.world.gen.structure.piece;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import dev.rebby.yumecraft.util.NotRandom;
import dev.rebby.yumecraft.world.gen.structure.check_value.CheckValue;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureLiquidSettings;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.pool.EmptyPoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.alias.StructurePoolAliasLookup;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.structure.Structure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public interface DensityStructurePiece {

    Codec<DensityStructurePiece> STRUCTURE_PIECE_CODEC = StructurePieceType.REGISTRY.getCodec().dispatch("type",
            DensityStructurePiece::getType, StructurePieceType::codec);

    static boolean checkStructure(BlockPos pos, long seed, NotRandom random, StructurePool dataPool) {
        int rand1 = (int) random.setValue(seed).nextLong();
        int rand2 = (int) random.setValue(rand1 + pos.getX()).nextLong();
        int rand3 = (int) random.setValue(rand2 + pos.getY()).nextLong();
        random.setValue(rand3 + pos.getZ());



        return dataPool.getRandomElement(random) != EmptyPoolElement.INSTANCE;
    }

    static StructurePoolElement getStructure(BlockPos pos, long seed, NotRandom random, StructurePool dataPool) {
        int rand1 = (int) random.setValue(seed).nextLong();
        int rand2 = (int) random.setValue(rand1 + pos.getX()).nextLong();
        int rand3 = (int) random.setValue(rand2 + pos.getY()).nextLong();
        random.setValue(rand3 + pos.getZ());

        return dataPool.getRandomElement(random);
    }

    boolean place(List<PoolStructurePiece> pieces, int scale, BlockPos pos, DensityFunction densityFunction,
                  StructureTemplateManager structureTemplateManager, Structure.Context context,
                  Map<String, CheckValue> values, NotRandom random, StructurePoolAliasLookup aliasLookup,
                  StructureLiquidSettings liquidSettings);

    String getName();

    @NotNull
    BlockPos getSize();

    StructurePieceType<?> getType();
}
