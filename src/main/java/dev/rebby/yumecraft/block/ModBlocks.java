package dev.rebby.yumecraft.block;

import dev.rebby.yumecraft.YumeCraft;
import dev.rebby.yumecraft.items.ModItems;
import dev.rebby.yumecraft.sound.ModSounds;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

public class ModBlocks {

    public static final Block BLUESTONE = registerWithItem("bluestone",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.5f, 6.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.CALCITE)));

    public static final Block BLUE_COBBLE = registerWithItem("blue_cobble",
            new Block(AbstractBlock.Settings.create()
                    .strength(4.0f, 9.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)));

    public static final Block BUBBLING_MAGMA_BLOCK = registerWithItem("bubbling_magma",
            new BubblingMagmaBlock(AbstractBlock.Settings.create()
                    .strength(1.0f, 9.0f)
                    .requiresTool()
                    .ticksRandomly()));

    public static final Block WHITEBRICK = registerWithItem("whitebrick",
            new Block(AbstractBlock.Settings.create()
                    .sounds(ModSounds.WHITEBRICK)
                    .strength(3.0f,9.0f)
                    .requiresTool()));

    public static final Block CHISELED_WHITEBRICK = registerWithItem("chiseled_whitebrick",
            new Block(AbstractBlock.Settings.copy(WHITEBRICK)));

    public static final StairsBlock WHITEBRICK_STAIRS = registerWithItem("whitebrick_stairs",
            new StairsBlock(WHITEBRICK.getDefaultState(), AbstractBlock.Settings.copy(WHITEBRICK)));

    public static final SlabBlock WHITEBRICK_SLAB = registerWithItem("whitebrick_slab",
            new SlabBlock(AbstractBlock.Settings.copy(WHITEBRICK)));

    public static final SlabBlock CHISELED_WHITEBRICK_SLAB = registerWithItem("chiseled_whitebrick_slab",
            new SlabBlock(AbstractBlock.Settings.copy(WHITEBRICK)));

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
