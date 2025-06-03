package dev.rebby.yumecraft.world.gen.structure.piece;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;

public record StructurePieceType<T extends StructurePiece>(MapCodec<T> codec) {

    public static final Registry<StructurePieceType<?>> REGISTRY =
            new SimpleRegistry<>(RegistryKey.ofRegistry(YumeCraft.id("structure_pieces")),
                    Lifecycle.stable());
}
