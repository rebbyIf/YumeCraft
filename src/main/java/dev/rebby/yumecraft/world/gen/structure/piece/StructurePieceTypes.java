package dev.rebby.yumecraft.world.gen.structure.piece;

import com.mojang.serialization.Codec;
import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.Registry;

public class StructurePieceTypes {
    public static final StructurePieceType<DefaultStructure> DEFAULT =
            register("default", new StructurePieceType<>(
                    DefaultStructure.DEFAULT_STRUCTURE_CODEC));

    public static final StructurePieceType<AlternatingStructure> ALTERNATING =
            register("alternating", new StructurePieceType<>(
                    AlternatingStructure.ALTERNATING_STRUCTURE_CODEC));

    public static final StructurePieceType<LayeredGridStructure> LAYERED_GRID =
            register("layered_grid", new StructurePieceType<>(
                    LayeredGridStructure.LAYERED_GRID_STRUCTURE_CODEC));

    public static <T extends StructurePiece> StructurePieceType<T> register(String id, StructurePieceType<T> type) {
        return Registry.register(StructurePieceType.REGISTRY, YumeCraft.id(id), type);
    }

    public static void init() {
        Codec<StructurePieceType<?>> structurePieceTypeCodec = StructurePieceType.REGISTRY.getCodec();

        Codec<StructurePiece> structurePieceCodec = structurePieceTypeCodec.dispatch("type",
                StructurePiece::getType, StructurePieceType::codec);

        YumeCraft.LOGGER.info("Registering Infinite Structure Pieces for " + YumeCraft.MOD_ID);
    }
}
