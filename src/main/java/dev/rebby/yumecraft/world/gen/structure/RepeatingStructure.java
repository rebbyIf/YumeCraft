package dev.rebby.yumecraft.world.gen.structure;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.rebby.yumecraft.world.gen.structure.generator.RepeatingStructureBasedGenerator;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructureLiquidSettings;
import net.minecraft.structure.StructurePiecesList;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.alias.StructurePoolAliasLookup;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.HeightContext;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.List;
import java.util.Optional;

public class RepeatingStructure extends Structure {

    public static final MapCodec<RepeatingStructure> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    configCodecBuilder(instance),
                    StructurePool.REGISTRY_CODEC.fieldOf("pool").forGetter(structure -> structure.pool),
                    HeightProvider.CODEC.fieldOf("height").forGetter(structure -> structure.height),
                    StructureLiquidSettings.codec.optionalFieldOf("liquid_settings",
                            StructureLiquidSettings.APPLY_WATERLOGGING).forGetter(structure -> structure.liquidSettings)
            ).apply(instance, RepeatingStructure::new)
    );

    private final RegistryEntry<StructurePool> pool;
    private final HeightProvider height;
    private final StructureLiquidSettings liquidSettings;

    protected RepeatingStructure(Config config, RegistryEntry<StructurePool> pool, HeightProvider height,
                                 StructureLiquidSettings liquidSettings) {
        super(config);
        this.pool = pool;
        this.height = height;
        this.liquidSettings = liquidSettings;
    }

    @Override
    protected Optional<StructurePosition> getStructurePosition(Context context) {
        ChunkPos chunkPos = context.chunkPos();
        int i = height.get(context.random(), new HeightContext(context.chunkGenerator(), context.world()));
        BlockPos pos = new BlockPos(chunkPos.getStartX(), i, chunkPos.getStartZ());
        return RepeatingStructureBasedGenerator.generate(
                context,
                pos,
                pool,
                StructurePoolAliasLookup.create(List.of(), pos, context.seed()),
                liquidSettings);
    }

    @Override
    public StructureType<?> getType() {
        return ModStructureTypes.REPEATING_STRUCTURE;
    }
}
