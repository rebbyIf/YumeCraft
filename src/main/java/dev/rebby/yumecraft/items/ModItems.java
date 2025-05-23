package dev.rebby.yumecraft.items;

import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {

    public static <T extends Item> T register(String name, T item) {
        return Registry.register(Registries.ITEM, YumeCraft.id(name),item);
    }

    public static void init() {
        YumeCraft.LOGGER.info("Registering Items for " + YumeCraft.MOD_ID);
    }
}
