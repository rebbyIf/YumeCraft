package dev.rebby.yumecraft.config;

import com.google.common.collect.ImmutableList;
import dev.rebby.yumecraft.YumeCraft;
import dev.rebby.yumecraft.util.ModDimensions;
import io.wispforest.endec.impl.BuiltInEndecs;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;
import io.wispforest.owo.serialization.endec.MinecraftEndecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Modmenu(modId = YumeCraft.MOD_ID)
@Config(name = "yumecraft-config", wrapperName = "YumeCraftConfig")
public class YumeCraftConfigModel {


    @Sync(Option.SyncMode.INFORM_SERVER)
    @SectionHeader("sleepingTeleportation")
    public Map<Identifier, Integer> sleepingTeleportation = getDefaultSleepingTeleportation();

    public static Map<Identifier, Integer> getDefaultSleepingTeleportation() {
        return new HashMap<>(
                Map.of(
                        Identifier.ofVanilla("empty"), 2,
                        ModDimensions.POINT_NEMO, 1,
                        ModDimensions.VERDANT_TEMPLE, 1,
                        ModDimensions.INFINITE_MALL, 1
                )
        );
    }
}
