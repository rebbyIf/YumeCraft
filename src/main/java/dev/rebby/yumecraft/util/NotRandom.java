package dev.rebby.yumecraft.util;

import net.minecraft.util.math.random.GaussianGenerator;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.random.RandomSplitter;

/**
 * Used for cheating the random system in net.minecraft.util.math.random,
 * particularly the DataPool class.
 * @author Rebby
 */
public class NotRandom implements Random {

    private double value;
    private final GaussianGenerator gaussianGenerator = new GaussianGenerator(this);

    /**
     * Creates non-random based upon a single value
     * @param value value to set
     */
    public NotRandom(double value) {
        this.value = value;
    }

    public NotRandom setValue(double value) {
        this.value = value;
        return this;
    }

    @Override
    public Random split() {
        return new NotRandom(value);
    }

    @Override
    public RandomSplitter nextSplitter() {
        return new Splitter(value);
    }

    @Override
    public void setSeed(long seed) {

    }

    /**
     * Returns the integer form of the non-random value
     * @return double
     */
    @Override
    public int nextInt() {
        return (int) value;
    }

    /**
     * Returns either the
     * @param bound
     * @return
     */
    @Override
    public int nextInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("Bound must be positive");
        }
        return (int) (value * bound);
    }

    @Override
    public long nextLong() {
        return (long) value;
    }

    @Override
    public boolean nextBoolean() {
        return ((int) value) % 2 == 0;
    }

    @Override
    public float nextFloat() {
        return (float) value;
    }

    @Override
    public double nextDouble() {
        return value;
    }

    @Override
    public double nextGaussian() {
        return gaussianGenerator.next();
    }

    public static class Splitter implements RandomSplitter{

        private final double value;

        public Splitter(double value) {
            this.value = value;
        }

        @Override
        public Random split(String seed) {
            return new NotRandom(value);
        }

        @Override
        public Random split(long seed) {
            return new NotRandom(value);
        }

        @Override
        public Random split(int x, int y, int z) {
            return new NotRandom(value);
        }

        @Override
        public void addDebugInfo(StringBuilder info) {
            info.append("NonRandomSetValue{").append(value).append("}");
        }
    }
}
