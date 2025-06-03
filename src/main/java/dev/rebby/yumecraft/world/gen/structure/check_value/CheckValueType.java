package dev.rebby.yumecraft.world.gen.structure.check_value;

import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.SimpleRegistry;

public record CheckValueType<T extends CheckValue>(MapCodec<T> codec) {

    public static final Registry<CheckValueType<?>> REGISTRY =
            new SimpleRegistry<>(RegistryKey.ofRegistry(YumeCraft.id("check_values")),
                    Lifecycle.stable());

}
