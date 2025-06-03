package dev.rebby.yumecraft.world.gen.structure.check_value;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.densityfunction.DensityFunction;

public interface CheckValue {
    Codec<CheckValue> VALUE_CODEC = CheckValueType.REGISTRY.getCodec().dispatch("type",
            CheckValue::getType, CheckValueType::codec);

    boolean check(DensityFunction check, BlockPos loc, BlockPos minLoc, BlockPos maxLoc, int scale);

    boolean doesOverrideIfTrue();

    boolean doesOverrideIfFalse();

    CheckValueType<?> getType();
}
