package dev.rebby.yumecraft.world.gen.structure.check_value;

import com.mojang.serialization.Codec;
import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.Registry;

public class CheckValueTypes {
    public static final CheckValueType<CheckDefaultValue> DEFAULT = register("default",
            new CheckValueType<>(CheckDefaultValue.DEFAULT_VALUE_CODEC));
    public static final CheckValueType<CheckPlaneForSingleValue> PLANE_FOR_SINGLE_VALUE = register("plane_for_value",
            new CheckValueType<>(CheckPlaneForSingleValue.PLANE_SINGLE_VALUE_CODEC));
    public static final CheckValueType<CheckDefaultValue.CheckAxisForSingleValue> AXIS_FOR_SINGLE_VALUE = register("axis_for_value",
            new CheckValueType<>(CheckDefaultValue.CheckAxisForSingleValue.AXIS_SINGLE_VALUE_CODEC));

    public static <T extends CheckValue> CheckValueType<T> register(String id, CheckValueType<T> type) {
        return Registry.register(CheckValueType.REGISTRY, YumeCraft.id(id), type);
    }

    public static void init() {
        Codec<CheckValueType<?>> valueTypeCodec = CheckValueType.REGISTRY.getCodec();

        Codec<CheckValue> valueCodec = valueTypeCodec.dispatch("type", CheckValue::getType,
                CheckValueType::codec);

        YumeCraft.LOGGER.info("Registering Infinite Structure Values for " + YumeCraft.MOD_ID);
    }
}
