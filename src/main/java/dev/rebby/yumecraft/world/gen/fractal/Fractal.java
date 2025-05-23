package dev.rebby.yumecraft.world.gen.fractal;

import com.mojang.serialization.Codec;

/**
 * Interface for generating fractals
 * @author rebby
 */
public interface Fractal {

    Codec<Fractal> FRACTAL_CODEC = FractalType.REGISTRY.getCodec()
            .dispatch("type", Fractal::getType, FractalType::codec);


    /**
     * Gets a value from the fractal
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     * @return value from fractal
     */
    float getValue(float x, float y, float z);

    /**
     * Checks if the particular coordinate is within a fractal.
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     * @return if in fractal
     */
    boolean inFractal(float x, float y, float z);

    float getMinValue();

    float getMaxValue();

    FractalType<?> getType();
}
