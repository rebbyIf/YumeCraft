package dev.rebby.yumecraft.world.gen.fractal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.Vec2f;

public class Mandelbrot implements Fractal{

    public static final MapCodec<Mandelbrot> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.INT.fieldOf("max_iterations").forGetter(Mandelbrot::getMaxIter),
                    Codec.INT.fieldOf("scale").forGetter(Mandelbrot::getScale),
                    Codec.BOOL.fieldOf("extends_in_third_dimension").forGetter(Mandelbrot::extendsInThirdDimension)
            ).apply(instance, Mandelbrot::new));

    private final int maxIter;
    private final int scale;
    private final boolean extendsInThirdDimension;

    public Mandelbrot(int maxIter, int scale, boolean extendsInThirdDimension) {
        this.maxIter = maxIter;
        this.scale = scale;
        this.extendsInThirdDimension = extendsInThirdDimension;
    }

    public int getMaxIter(){
        return maxIter;
    }

    public int getScale(){
        return scale;
    }

    public boolean extendsInThirdDimension(){
        return extendsInThirdDimension;
    }

    @Override
    public float getValue(float x, float y, float z) {
        if (!extendsInThirdDimension && y != 0) {
            return 0.0f;
        }
        Vec2f pos = new Vec2f(x / scale, z / scale);
        Vec2f a = pos;
        for (int i = 0; i < maxIter; i++) {
            a = mandelbrotHelper(a, pos);
            if (Math.abs(a.x) > 2 || Math.abs(a.y) > 2) {
                return 0.0f;
            }
        }
        return Math.abs(a.x) + Math.abs(a.y);
    }

    @Override
    public boolean inFractal(float x, float y, float z) {
        if (!extendsInThirdDimension && z == 0) {
            return false;
        }
        Vec2f pos = new Vec2f(x / scale, z / scale);
        Vec2f a = pos;
        for (int i = 0; i < maxIter; i++) {
            a = mandelbrotHelper(a, pos);
            if (Math.abs(a.x) > 2 || Math.abs(a.y) > 2) {
                return false;
            }
        }
        return true;
    }

    @Override
    public float getMinValue() {
        return 0.0f;
    }

    @Override
    public float getMaxValue() {
        return 4.0f;
    }

    private Vec2f mandelbrotHelper(Vec2f a, Vec2f z) {
        Vec2f raisedToPower, sum;
        raisedToPower = squareImaginary(a);
        sum = new Vec2f(raisedToPower.x + z.x, raisedToPower.y + z.y);
        return sum;
    }

    private Vec2f squareImaginary( Vec2f i) {
        return new Vec2f( i.x * i.x - i.y * i.y, 2 * i.x * i.y);
    }

    @Override
    public FractalType<?> getType() {
        return FractalTypes.MANDELBROT;
    }
}
