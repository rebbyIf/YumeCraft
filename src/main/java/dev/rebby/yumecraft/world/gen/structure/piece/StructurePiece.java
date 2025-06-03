package dev.rebby.yumecraft.world.gen.structure.piece;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import dev.rebby.yumecraft.util.NotRandom;
import dev.rebby.yumecraft.world.gen.structure.check_value.CheckValue;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public interface StructurePiece {

    Codec<StructurePiece> STRUCTURE_PIECE_CODEC = StructurePieceType.REGISTRY.getCodec().dispatch("type",
            StructurePiece::getType, StructurePieceType::codec);
    Codec<Either<Identifier, StructureTemplate>> STRUCTURE_TEMPLATE_CODEC = Codec.of(
                    StructurePiece::encodeLocation, Identifier.CODEC.map(Either::left));

    static boolean checkStructure(BlockPos pos, long seed, NotRandom random, DataPool<Either<Identifier, StructureTemplate>> dataPool) {
        int rand1 = (int) random.setValue(seed).nextLong();
        int rand2 = (int) random.setValue(rand1 + pos.getX()).nextLong();
        int rand3 = (int) random.setValue(rand2 + pos.getY()).nextLong();
        random.setValue(rand3 + pos.getZ());

        Optional<Either<Identifier, StructureTemplate>> optionalStructure = dataPool.getDataOrEmpty(random);
        return optionalStructure.filter(identifierStructureTemplateEither -> !identifierStructureTemplateEither.orThrow().getPath().equals("empty")).isPresent();

    }

    @Nullable
    static StructureTemplate getStructure(StructureTemplateManager structureTemplateManager, BlockPos pos, long seed, NotRandom random, DataPool<Either<Identifier, StructureTemplate>> dataPool) {
        int rand1 = (int) random.setValue(seed).nextLong();
        int rand2 = (int) random.setValue(rand1 + pos.getX()).nextLong();
        int rand3 = (int) random.setValue(rand2 + pos.getY()).nextLong();
        random.setValue(rand3 + pos.getZ());

        Optional<Either<Identifier, StructureTemplate>> optionalStructure = dataPool.getDataOrEmpty(random);
        return optionalStructure.map(identifierStructureTemplateEither -> identifierStructureTemplateEither.map(structureTemplateManager::getTemplateOrBlank, Function.identity())).orElse(null);
    }

    // Copied from minecraft's source
    static <T> DataResult<T> encodeLocation(Either<Identifier, StructureTemplate> location, DynamicOps<T> ops, T prefix) {
        Optional<Identifier> optional = location.left();
        return optional.isEmpty()
                ? DataResult.error(() -> "Can not serialize a runtime pool element")
                : Identifier.CODEC.encode(optional.get(), ops, prefix);
    }

    String canPlace(int scale, BlockPos pos, DensityFunction densityFunction,
                    StructureTemplateManager structureTemplateManager, long seed,
                    Map<String, CheckValue> values, NotRandom random);

    boolean place(int scale, BlockPos pos, DensityFunction densityFunction,
                  StructureTemplateManager structureTemplateManager, ServerWorldAccess world,
                  Map<String, CheckValue> values, NotRandom random);

    String getName();

    @NotNull
    BlockPos getSize();

    StructurePieceType<?> getType();
}
