package dev.rebby.yumecraft.world.gen.structure.generator;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureLiquidSettings;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePiecesCollector;
import net.minecraft.structure.pool.EmptyPoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.alias.StructurePoolAliasLookup;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.structure.Structure;

import java.util.Optional;

public class RepeatingStructureBasedGenerator {

    public static Optional<Structure.StructurePosition> generate(Structure.Context context, BlockPos pos,
                                                                 RegistryEntry<StructurePool> poolEntry,
                                                                 StructurePoolAliasLookup aliasLookup,
                                                                 StructureLiquidSettings liquidSettings){

        DynamicRegistryManager dynamicRegistryManager = context.dynamicRegistryManager();
        Registry<StructurePool> registry = dynamicRegistryManager.get(RegistryKeys.TEMPLATE_POOL);
        StructurePool structurePool = (StructurePool) poolEntry.getKey()
                .flatMap(key -> registry.getOrEmpty(aliasLookup.lookup(key)))
                .orElse(poolEntry.value());

        StructurePoolElement structurePoolElement = structurePool.getRandomElement(context.random());

        if (structurePoolElement == EmptyPoolElement.INSTANCE){
            return Optional.empty();
        }

        PoolStructurePiece structurePiece = new PoolStructurePiece(
                context.structureTemplateManager(),
                structurePoolElement,
                pos,
                structurePoolElement.getGroundLevelDelta(),
                BlockRotation.NONE,
                structurePoolElement.getBoundingBox(context.structureTemplateManager(), pos, BlockRotation.NONE),
                liquidSettings);

        return Optional.of(new Structure.StructurePosition(pos, collector -> collector.addPiece(
                structurePiece
        )));
    }
}
