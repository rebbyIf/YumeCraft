package dev.rebby.yumecraft.world.gen.structure;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public record InfiniteStructureType<T extends InfiniteStructure>(MapCodec<T> codec) {

    public static final Registry<InfiniteStructureType<?>> REGISTRY = new SimpleRegistry<>(RegistryKey.ofRegistry(
            Identifier.of(YumeCraft.MOD_ID, "infinite_structures")), Lifecycle.stable());
}
