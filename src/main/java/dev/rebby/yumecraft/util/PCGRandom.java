package dev.rebby.yumecraft.util;

import com.google.common.primitives.UnsignedInteger;

public class PCGRandom extends NotRandom{

    public PCGRandom(long unit) {
        super(0);

        this.setValue(unit);
    }

    @Override
    public NotRandom setValue(double value) {

        super.setValue(pcg((long) value) / UnsignedInteger.MAX_VALUE.doubleValue());

        return this;
    }

    @Override
    public int nextInt() {
        return Math.min((int) (nextDouble() * Integer.MAX_VALUE), Integer.MAX_VALUE);
    }

    @Override
    public int nextInt(int bound) {
        if (bound <= 0) {
            throw new IllegalArgumentException("Bound must be positive");
        }
        return Math.min((int) (nextDouble() * bound), bound - 1);
    }
    @Override
    public long nextLong() {
        return Math.min((long) (nextDouble() * Long.MAX_VALUE), Long.MAX_VALUE);
    }

    @Override
    public boolean nextBoolean() {
        return nextInt() % 2 == 0;
    }

    @Override
    public float nextFloat() {
        return (float) nextDouble();
    }

    private long pcg(long uInt) {
        long state1 = calculateOverflow(calculateOverflow(calculateOverflow(uInt) * 747796405L) + 2891336453L);
        long state2 = calculateOverflow((calculateOverflow(calculateOverflow(calculateOverflow(
                state1 >>> 28L) + 4L) + 4L) ^ state1) * 277803737L);
        return calculateOverflow(state2 >> 22L) ^ state2;
    }

    private long calculateOverflow(long uInt) {
        return uInt & (UnsignedInteger.MAX_VALUE.longValue());
    }
}
