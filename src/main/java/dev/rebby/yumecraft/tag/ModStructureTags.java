package dev.rebby.yumecraft.tag;

import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.gen.structure.Structure;

public interface ModStructureTags {

    TagKey<Structure> INFINITE_MALL_MALL = of("infinite_mall/mall");

    private static TagKey<Structure> of (String id) {
        return TagKey.of(RegistryKeys.STRUCTURE, YumeCraft.id(id));
    }

}
