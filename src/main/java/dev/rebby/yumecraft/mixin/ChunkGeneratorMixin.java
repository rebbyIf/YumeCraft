package dev.rebby.yumecraft.mixin;

import com.mojang.datafixers.util.Pair;
import dev.rebby.yumecraft.world.gen.structure.placement.GridStructurePlacement;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import net.minecraft.world.gen.chunk.placement.StructurePlacementCalculator;
import net.minecraft.world.gen.structure.Structure;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.Set;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {

    @Shadow
    @Nullable
    private static Pair<BlockPos, RegistryEntry<Structure>> locateStructure(Set<RegistryEntry<Structure>> structures, WorldView world, StructureAccessor structureAccessor, boolean skipReferencedStructures, StructurePlacement placement, ChunkPos pos) {
        return null;
    }

    @Inject(at = @At("HEAD"), method = "locateStructure(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/registry/entry/RegistryEntryList;Lnet/minecraft/util/math/BlockPos;IZ)Lcom/mojang/datafixers/util/Pair;", cancellable = true)
    public void locateStructure(
            ServerWorld world, RegistryEntryList<Structure> structures, BlockPos center, int radius, boolean skipReferencedStructures, CallbackInfoReturnable<Pair<BlockPos, RegistryEntry<Structure>>> cir
    ) {
        StructurePlacementCalculator structurePlacementCalculator = world.getChunkManager().getStructurePlacementCalculator();
        Map<StructurePlacement, Set<RegistryEntry<Structure>>> map = new Object2ObjectArrayMap<>();

        for (RegistryEntry<Structure> registryEntry : structures) {
            for (StructurePlacement structurePlacement : structurePlacementCalculator.getPlacements(registryEntry)) {
                (map.computeIfAbsent(structurePlacement, placement -> new ObjectArraySet<>())).add(registryEntry);
            }
        }

        if (map.isEmpty()) {
            cir.setReturnValue(null);
        } else {
            Pair<BlockPos, RegistryEntry<Structure>> pair;
            double d = Double.MAX_VALUE;
            StructureAccessor structureAccessor = world.getStructureAccessor();

            for (Map.Entry<StructurePlacement, Set<RegistryEntry<Structure>>> entry : map.entrySet()) {
                StructurePlacement structurePlacement2 = entry.getKey();
                if (structurePlacement2 instanceof GridStructurePlacement gridStructurePlacement) {
                    ChunkPos chunkPos = new ChunkPos(ChunkSectionPos.getSectionCoord(center.getX()), ChunkSectionPos.getSectionCoord(center.getZ()));
                    pair = locateGridPlacementStructure(entry.getValue(), world, structureAccessor, skipReferencedStructures, gridStructurePlacement, radius, chunkPos);
                    if (pair != null) {
                        BlockPos blockPos = pair.getFirst();
                        double e = center.getSquaredDistance(blockPos);
                        if (e < d) {
                            cir.setReturnValue(pair);
                        }
                    }
                }
            }
        }
    }

    @Unique
    private static Pair<BlockPos, RegistryEntry<Structure>> locateGridPlacementStructure(Set<RegistryEntry<Structure>> structures, WorldView world, StructureAccessor structureAccessor, boolean skipReferencedStructures, GridStructurePlacement placement, int radius, ChunkPos centerChunk) {
        int i = placement.getSeparation();

        for (int r = 0; r <= radius; r++) {
            for (int x = -r; x <= r; x++){
                for (int z = -r; z <= r; z++){
                    if (Math.abs(x) == r || Math.abs(z) == r) {
                        ChunkPos chunkPos = new ChunkPos(centerChunk.x + x, centerChunk.z + z);
                        if (chunkPos.x % i == 0 && chunkPos.z % i == 0) {
                            Pair<BlockPos, RegistryEntry<Structure>>pair = locateStructure(structures, world, structureAccessor, skipReferencedStructures, placement, chunkPos);
                            if (pair != null) {
                                return pair;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}
