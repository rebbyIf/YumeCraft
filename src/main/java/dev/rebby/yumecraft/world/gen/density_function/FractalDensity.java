package dev.rebby.yumecraft.world.gen.density_function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.rebby.yumecraft.world.gen.fractal.Fractal;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.world.gen.densityfunction.DensityFunction;

public record FractalDensity(Fractal fractal, double outside_value) implements DensityFunction {

    public static final MapCodec<FractalDensity> FRACTAL_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Fractal.FRACTAL_CODEC.fieldOf("fractal").forGetter(FractalDensity::fractal),
                    Codec.DOUBLE.fieldOf("outside_value").forGetter(FractalDensity::outside_value)
            ).apply(instance, FractalDensity::new));

    public static final CodecHolder<FractalDensity> CODEC_HOLDER = CodecHolder.of(FRACTAL_CODEC);


    @Override
    public double sample(NoisePos pos) {
        if (!fractal.inFractal(pos.blockX(), pos.blockY(), pos.blockZ())) {
            return outside_value;
        }
        return fractal.getValue(pos.blockX(), pos.blockY(), pos.blockZ());
    }

    @Override
    public void fill(double[] densities, EachApplier applier) {
        applier.fill(densities, this);
    }

    @Override
    public DensityFunction apply(DensityFunctionVisitor visitor) {
        return visitor.apply(new FractalDensity(this.fractal, this.outside_value));
    }

    @Override
    public double minValue() {
        return fractal.getMinValue();
    }

    @Override
    public double maxValue() {
        return fractal.getMaxValue();
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC_HOLDER;
    }
}
