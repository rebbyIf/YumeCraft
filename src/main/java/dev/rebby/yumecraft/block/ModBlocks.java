package dev.rebby.yumecraft.block;

import dev.rebby.yumecraft.YumeCraft;
import dev.rebby.yumecraft.items.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

public class ModBlocks {

    public static final Block BLUESTONE = registerWithItem("bluestone",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.0f, 6.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.CALCITE)));

    public static final Block BLUE_COBBLE = registerWithItem("blue_cobble",
            new Block(AbstractBlock.Settings.create()
                    .strength(3.0f, 12.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)));

    public static <T extends Block> T register(String name, T block) {
        return Registry.register(Registries.BLOCK, YumeCraft.id(name),block);
    }

    public static <T extends Block> T registerWithItem(String name, T block, Item.Settings settings) {
        T registered = register(name, block);
        ModItems.register(name, new BlockItem(registered, settings));
        return registered;
    }

    public static <T extends Block> T registerWithItem(String name, T block) {
        return registerWithItem(name, block, new Item.Settings());
    }

    public static void init() {
        YumeCraft.LOGGER.info("Registering blocks for " + YumeCraft.MOD_ID);
    }
}
