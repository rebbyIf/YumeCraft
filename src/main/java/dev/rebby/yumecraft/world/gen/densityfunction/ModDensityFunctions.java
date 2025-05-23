package dev.rebby.yumecraft.world.gen.densityfunction;

import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModDensityFunctions {

    public static void init(){
        Registry.register(Registries.DENSITY_FUNCTION_TYPE, Identifier.of(YumeCraft.MOD_ID, "fractal_density"), FractalDensity.CODEC_HOLDER.codec());

        YumeCraft.LOGGER.info("Registering Density Functions for " + YumeCraft.MOD_ID);
    }

}
