package dev.rebby.yumecraft.items;

import dev.rebby.yumecraft.YumeCraft;
import dev.rebby.yumecraft.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import java.util.Optional;

public class ModItemGroups {

    private static final Text BLOCKS_TITLE = Text.translatable("itemGroup."+YumeCraft.MOD_ID+".block_group");

    public static final ItemGroup BLOCKS_GROUP = register("block_group", FabricItemGroup.builder()
            .displayName(BLOCKS_TITLE)
            .icon(ModBlocks.WHITEBRICK.asItem()::getDefaultStack)
            .entries(((displayContext, entries) -> Registries.BLOCK.getIds()
                    .stream()
                    .filter(key -> key.getNamespace().equals(YumeCraft.MOD_ID))
                    .map(Registries.BLOCK::getOrEmpty)
                    .map(Optional::orElseThrow)
                    .forEach(entries::add)))
            .build());

    public static <T extends ItemGroup> T register(String name, T itemGroup){
        return Registry.register(Registries.ITEM_GROUP, YumeCraft.id(name), itemGroup);
    }

    public static void init(){
        YumeCraft.LOGGER.info("Registering mod item groups for "+YumeCraft.MOD_ID);
    }
}
