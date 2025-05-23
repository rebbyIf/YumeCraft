package dev.rebby.yumecraft.world.gen.fractal;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;

public record FractalType<T extends Fractal>(MapCodec<T> codec) {

    public static final Registry<FractalType<?>> REGISTRY = new SimpleRegistry<>(
            RegistryKey.ofRegistry(Identifier.of(YumeCraft.MOD_ID, "fractal_types")), Lifecycle.stable());
}
