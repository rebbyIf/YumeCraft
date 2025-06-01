package dev.rebby.yumecraft.world.gen.density_function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.rebby.yumecraft.util.LoadingWorldHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.util.dynamic.CodecHolder;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.LocalRandom;
import net.minecraft.world.gen.densityfunction.DensityFunction;
import net.minecraft.world.gen.densityfunction.DensityFunctionTypes;

public class NoiseDensity implements DensityFunction {

    private DensityFunction.Noise noise;
    private final double xzScale;
    private final double yScale;

    public static final MapCodec<NoiseDensity> NOISE_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    DensityFunction.Noise.CODEC.fieldOf("noise").forGetter(NoiseDensity::getNoise),
                    Codec.DOUBLE.fieldOf("xz_scale").forGetter(NoiseDensity::getXzScale),
                    Codec.DOUBLE.fieldOf("y_scale").forGetter(NoiseDensity::getyScale))
                    .apply(instance, NoiseDensity::new));

    public static final CodecHolder<NoiseDensity> CODEC_HOLDER = CodecHolder.of(NOISE_CODEC);

    public NoiseDensity(DensityFunction.Noise noise, double xzScale, double yScale){
//        if (LoadingWorldHandler.seed == null) {
//            throw new NullPointerException("Seed was found null!");
//        }

        this.noise = new DensityFunction.Noise(noise.noiseData());
        this.xzScale = xzScale;
        this.yScale = yScale;

        ServerWorldEvents.LOAD.register((server, world) -> {
            DoublePerlinNoiseSampler doublePerlinNoiseSampler = DoublePerlinNoiseSampler.create(new CheckedRandom(world.getSeed()), noise.noiseData().value());
            this.noise = new DensityFunction.Noise(noise.noiseData(), doublePerlinNoiseSampler);
        });
    }

    public Noise getNoise() {
        return noise;
    }

    public double getXzScale() {
        return xzScale;
    }

    public double getyScale() {
        return yScale;
    }

    @Override
    public double sample(DensityFunction.NoisePos pos) {
        return this.noise.sample(pos.blockX() * this.xzScale, pos.blockY() * this.yScale, pos.blockZ() * this.xzScale);
    }

    @Override
    public void fill(double[] densities, DensityFunction.EachApplier applier) {
        applier.fill(densities, this);
    }

    @Override
    public DensityFunction apply(DensityFunction.DensityFunctionVisitor visitor) {
        return visitor.apply(new NoiseDensity(visitor.apply(this.noise), this.xzScale, this.yScale));
    }

    @Override
    public double minValue() {
        return -this.maxValue();
    }

    @Override
    public double maxValue() {
        return this.noise.getMaxValue();
    }

    @Override
    public CodecHolder<? extends DensityFunction> getCodecHolder() {
        return CODEC_HOLDER;
    }
}
