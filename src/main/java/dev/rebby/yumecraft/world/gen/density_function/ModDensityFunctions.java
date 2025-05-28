package dev.rebby.yumecraft.world.gen.density_function;

import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.densityfunction.DensityFunction;

public class ModDensityFunctions {

    public static final RegistryKey<DensityFunction> AMARANTH_TEMPLE_FINAL = of("amaranth_temple/final");

    private static RegistryKey<DensityFunction> of(String id){
        return RegistryKey.of(RegistryKeys.DENSITY_FUNCTION, YumeCraft.id(id));
    }

    public static void init(){
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(YumeCraft.MOD_ID, "fractal_density"), FractalDensity.CODEC_HOLDER.codec());
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(YumeCraft.MOD_ID, "noise"), NoiseDensity.CODEC_HOLDER.codec());

        YumeCraft.LOGGER.info("Registering Density Functions for " + YumeCraft.MOD_ID);
    }

}
