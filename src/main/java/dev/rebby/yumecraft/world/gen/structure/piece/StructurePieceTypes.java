package dev.rebby.yumecraft.world.gen.structure.piece;

import com.mojang.serialization.Codec;
import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.Registry;

public class StructurePieceTypes {
    public static final StructurePieceType<DefaultDensityStructure> DEFAULT =
            register("default", new StructurePieceType<>(
                    DefaultDensityStructure.DEFAULT_STRUCTURE_CODEC));

    public static final StructurePieceType<AlternatingDensityStructure> ALTERNATING =
            register("alternating", new StructurePieceType<>(
                    AlternatingDensityStructure.ALTERNATING_STRUCTURE_CODEC));

    public static final StructurePieceType<LayeredGridDensityStructure> LAYERED_GRID =
            register("layered_grid", new StructurePieceType<>(
                    LayeredGridDensityStructure.LAYERED_GRID_STRUCTURE_CODEC));

    public static <T extends DensityStructurePiece> StructurePieceType<T> register(String id, StructurePieceType<T> type) {
        return Registry.register(StructurePieceType.REGISTRY, YumeCraft.id(id), type);
    }

    public static void init() {
        Codec<StructurePieceType<?>> structurePieceTypeCodec = StructurePieceType.REGISTRY.getCodec();

        Codec<DensityStructurePiece> structurePieceCodec = structurePieceTypeCodec.dispatch("type",
                DensityStructurePiece::getType, StructurePieceType::codec);

        YumeCraft.LOGGER.info("Registering Infinite Structure Pieces for " + YumeCraft.MOD_ID);
    }
}
