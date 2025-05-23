package dev.rebby.yumecraft.world.gen.fractal;

import com.mojang.serialization.Codec;
import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class FractalTypes {

    public static final FractalType<Mandelbrot> MANDELBROT = register("mandelbrot", new FractalType<>(Mandelbrot.CODEC));

    public static <T extends Fractal> FractalType<T> register(String id, FractalType<T> fractalType){
        return Registry.register(FractalType.REGISTRY, Identifier.of(YumeCraft.MOD_ID, id), fractalType);
    }

    public static void init(){
        Codec<FractalType<?>> fractalTypeCodec = FractalType.REGISTRY.getCodec();

        Codec<Fractal> fractalCodec = fractalTypeCodec.dispatch("type", Fractal::getType, FractalType::codec);

        YumeCraft.LOGGER.info("Registering Fractals for " + YumeCraft.MOD_ID);
    }

}
