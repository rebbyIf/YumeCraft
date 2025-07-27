package dev.rebby.yumecraft.block;

import com.google.common.collect.ImmutableList;
import dev.rebby.yumecraft.YumeCraft;
import dev.rebby.yumecraft.items.ModItems;
import dev.rebby.yumecraft.sound.ModSounds;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
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

    public static final WallBlock WHITEBRICK_WALL = registerWithItem("whitebrick_wall",
            new WallBlock(AbstractBlock.Settings.copy(WHITEBRICK).solid()));

    public static final WallBlock CHISELED_WHITEBRICK_WALL = registerWithItem("chiseled_whitebrick_wall",
            new WallBlock(AbstractBlock.Settings.copy(WHITEBRICK)));

    // Reinforced Iron

    public static final Block REINFORCED_IRON = registerWithItem("reinforced_iron",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .requiresTool()
                    .strength(25f, 10f)
                    .sounds(BlockSoundGroup.COPPER)));

    public static final Block REINFORCED_IRON_WALL = registerWithItem("reinforced_iron_wall",
            new WallBlock(AbstractBlock.Settings.copy(REINFORCED_IRON)));

    // Copper Walls

    public static final Block COPPER_WALL = registerWithItem("copper_wall",
            new OxidizableWallBlock(Oxidizable.OxidationLevel.UNAFFECTED,
                    AbstractBlock.Settings.copy(Blocks.COPPER_BLOCK)));

    public static final Block EXPOSED_COPPER_WALL = registerWithItem("exposed_copper_wall",
            new OxidizableWallBlock(Oxidizable.OxidationLevel.EXPOSED,
                    AbstractBlock.Settings.copy(Blocks.EXPOSED_COPPER)));

    public static final Block WEATHERED_COPPER_WALL = registerWithItem("weathered_copper_wall",
            new OxidizableWallBlock(Oxidizable.OxidationLevel.WEATHERED,
                    AbstractBlock.Settings.copy(Blocks.WEATHERED_COPPER)));

    public static final Block OXIDIZED_COPPER_WALL = registerWithItem("oxidized_copper_wall",
            new OxidizableWallBlock(Oxidizable.OxidationLevel.OXIDIZED,
                    AbstractBlock.Settings.copy(Blocks.OXIDIZED_COPPER)));

    public static final Block WAXED_COPPER_WALL = registerWithItem("waxed_copper_wall",
            new WallBlock(AbstractBlock.Settings.copy(COPPER_WALL)));

    public static final Block WAXED_EXPOSED_COPPER_WALL = registerWithItem("waxed_exposed_copper_wall",
            new WallBlock(AbstractBlock.Settings.copy(EXPOSED_COPPER_WALL)));

    public static final Block WAXED_WEATHERED_COPPER_WALL = registerWithItem("waxed_weathered_copper_wall",
            new WallBlock(AbstractBlock.Settings.copy(WEATHERED_COPPER_WALL)));

    public static final Block WAXED_OXIDIZED_COPPER_WALL = registerWithItem("waxed_oxidized_copper_wall",
            new WallBlock(AbstractBlock.Settings.copy(OXIDIZED_COPPER_WALL)));

    /*
     * Group for Concrete Alt Blocks
     */

    // White

    public static final StairsBlock WHITE_CONCRETE_STAIRS = registerWithItem("white_concrete_stairs",
            new StairsBlock(Blocks.WHITE_CONCRETE.getDefaultState(),
                    AbstractBlock.Settings.copy(Blocks.BLUE_CONCRETE)));

    public static final SlabBlock WHITE_CONCRETE_SLABS = registerWithItem("white_concrete_slab",
            new SlabBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE)));

    public static final EdgeBlock WHITE_CONCRETE_EDGE = registerWithItem("white_concrete_edge",
            new EdgeBlock(AbstractBlock.Settings.copy(Blocks.WHITE_CONCRETE)));

    // Light Gray

    public static final StairsBlock LIGHT_GRAY_CONCRETE_STAIRS = registerWithItem("light_gray_concrete_stairs",
            new StairsBlock(Blocks.LIGHT_GRAY_CONCRETE.getDefaultState(),
                    AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_CONCRETE)));

    public static final SlabBlock LIGHT_GRAY_CONCRETE_SLABS = registerWithItem("light_gray_concrete_slab",
            new SlabBlock(AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_CONCRETE)));

    public static final EdgeBlock LIGHT_GRAY_CONCRETE_EDGE = registerWithItem("light_gray_concrete_edge",
            new EdgeBlock(AbstractBlock.Settings.copy(Blocks.LIGHT_GRAY_CONCRETE)));

    // Gray

    public static final StairsBlock GRAY_CONCRETE_STAIRS = registerWithItem("gray_concrete_stairs",
            new StairsBlock(Blocks.GRAY_CONCRETE.getDefaultState(),
                    AbstractBlock.Settings.copy(Blocks.GRAY_CONCRETE)));

    public static final SlabBlock GRAY_CONCRETE_SLABS = registerWithItem("gray_concrete_slab",
            new SlabBlock(AbstractBlock.Settings.copy(Blocks.GRAY_CONCRETE)));

    public static final EdgeBlock GRAY_CONCRETE_EDGE = registerWithItem("gray_concrete_edge",
            new EdgeBlock(AbstractBlock.Settings.copy(Blocks.GRAY_CONCRETE)));

    // Black

    public static final StairsBlock BLACK_CONCRETE_STAIRS = registerWithItem("black_concrete_stairs",
            new StairsBlock(Blocks.BLACK_CONCRETE.getDefaultState(),
                    AbstractBlock.Settings.copy(Blocks.BLACK_CONCRETE)));

    public static final SlabBlock BLACK_CONCRETE_SLABS = registerWithItem("black_concrete_slab",
            new SlabBlock(AbstractBlock.Settings.copy(Blocks.BLACK_CONCRETE)));

    public static final EdgeBlock BLACK_CONCRETE_EDGE = registerWithItem("black_concrete_edge",
            new EdgeBlock(AbstractBlock.Settings.copy(Blocks.BLACK_CONCRETE)));

    // ORANGE
    public static final StairsBlock ORANGE_CONCRETE_STAIRS = registerWithItem("orange_concrete_stairs",
            new StairsBlock(Blocks.ORANGE_CONCRETE.getDefaultState(),
                    AbstractBlock.Settings.copy(Blocks.ORANGE_CONCRETE)));

    public static final SlabBlock ORANGE_CONCRETE_SLABS = registerWithItem("orange_concrete_slab",
            new SlabBlock(AbstractBlock.Settings.copy(Blocks.ORANGE_CONCRETE)));

    public static final EdgeBlock ORANGE_CONCRETE_EDGE = registerWithItem("orange_concrete_edge",
            new EdgeBlock(AbstractBlock.Settings.copy(Blocks.ORANGE_CONCRETE)));

    // Magenta
    public static final StairsBlock MAGENTA_CONCRETE_STAIRS = registerWithItem("magenta_concrete_stairs",
            new StairsBlock(Blocks.MAGENTA_CONCRETE.getDefaultState(),
                    AbstractBlock.Settings.copy(Blocks.MAGENTA_CONCRETE)));

    public static final SlabBlock MAGENTA_CONCRETE_SLABS = registerWithItem("magenta_concrete_slab",
            new SlabBlock(AbstractBlock.Settings.copy(Blocks.MAGENTA_CONCRETE)));

    public static final EdgeBlock MAGENTA_CONCRETE_EDGE = registerWithItem("magenta_concrete_edge",
            new EdgeBlock(AbstractBlock.Settings.copy(Blocks.MAGENTA_CONCRETE)));

    // Light Blue
    public static final StairsBlock LIGHT_BLUE_CONCRETE_STAIRS = registerWithItem("light_blue_concrete_stairs",
            new StairsBlock(Blocks.LIGHT_BLUE_CONCRETE.getDefaultState(),
                    AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_CONCRETE)));

    public static final SlabBlock LIGHT_BLUE_CONCRETE_SLABS = registerWithItem("light_blue_concrete_slab",
            new SlabBlock(AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_CONCRETE)));

    public static final EdgeBlock LIGHT_BLUE_CONCRETE_EDGE = registerWithItem("light_blue_concrete_edge",
            new EdgeBlock(AbstractBlock.Settings.copy(Blocks.LIGHT_BLUE_CONCRETE)));

    // YELLOW
    public static final StairsBlock YELLOW_CONCRETE_STAIRS = registerWithItem("yellow_concrete_stairs",
            new StairsBlock(Blocks.YELLOW_CONCRETE.getDefaultState(),
                    AbstractBlock.Settings.copy(Blocks.YELLOW_CONCRETE)));

    public static final SlabBlock YELLOW_CONCRETE_SLABS = registerWithItem("yellow_concrete_slab",
            new SlabBlock(AbstractBlock.Settings.copy(Blocks.YELLOW_CONCRETE)));

    public static final EdgeBlock YELLOW_CONCRETE_EDGE = registerWithItem("yellow_concrete_edge",
            new EdgeBlock(AbstractBlock.Settings.copy(Blocks.YELLOW_CONCRETE)));

    // Lime
    public static final StairsBlock LIME_CONCRETE_STAIRS = registerWithItem("lime_concrete_stairs",
            new StairsBlock(Blocks.LIME_CONCRETE.getDefaultState(),
                    AbstractBlock.Settings.copy(Blocks.LIME_CONCRETE)));

    public static final SlabBlock LIME_CONCRETE_SLABS = registerWithItem("lime_concrete_slab",
            new SlabBlock(AbstractBlock.Settings.copy(Blocks.LIME_CONCRETE)));

    public static final EdgeBlock LIME_CONCRETE_EDGE = registerWithItem("lime_concrete_edge",
            new EdgeBlock(AbstractBlock.Settings.copy(Blocks.LIME_CONCRETE)));

    // Pink
    public static final StairsBlock PINK_CONCRETE_STAIRS = registerWithItem("pink_concrete_stairs",
            new StairsBlock(Blocks.PINK_CONCRETE.getDefaultState(),
                    AbstractBlock.Settings.copy(Blocks.PINK_CONCRETE)));

    public static final SlabBlock PINK_CONCRETE_SLABS = registerWithItem("pink_concrete_slab",
            new SlabBlock(AbstractBlock.Settings.copy(Blocks.PINK_CONCRETE)));

    public static final EdgeBlock PINK_CONCRETE_EDGE = registerWithItem("pink_concrete_edge",
            new EdgeBlock(AbstractBlock.Settings.copy(Blocks.PINK_CONCRETE)));

    // Cyan
    public static final StairsBlock CYAN_CONCRETE_STAIRS = registerWithItem("cyan_concrete_stairs",
            new StairsBlock(Blocks.CYAN_CONCRETE.getDefaultState(),
                    AbstractBlock.Settings.copy(Blocks.CYAN_CONCRETE)));

    public static final SlabBlock CYAN_CONCRETE_SLABS = registerWithItem("cyan_concrete_slab",
            new SlabBlock(AbstractBlock.Settings.copy(Blocks.CYAN_CONCRETE)));

    public static final EdgeBlock CYAN_CONCRETE_EDGE = registerWithItem("cyan_concrete_edge",
            new EdgeBlock(AbstractBlock.Settings.copy(Blocks.CYAN_CONCRETE)));

    // Purple
    public static final StairsBlock PURPLE_CONCRETE_STAIRS = registerWithItem("purple_concrete_stairs",
            new StairsBlock(Blocks.PURPLE_CONCRETE.getDefaultState(),
                    AbstractBlock.Settings.copy(Blocks.PURPLE_CONCRETE)));

    public static final SlabBlock PURPLE_CONCRETE_SLABS = registerWithItem("purple_concrete_slab",
            new SlabBlock(AbstractBlock.Settings.copy(Blocks.PURPLE_CONCRETE)));

    public static final EdgeBlock PURPLE_CONCRETE_EDGE = registerWithItem("purple_concrete_edge",
            new EdgeBlock(AbstractBlock.Settings.copy(Blocks.PURPLE_CONCRETE)));

    // Blue
    public static final StairsBlock BLUE_CONCRETE_STAIRS = registerWithItem("blue_concrete_stairs",
            new StairsBlock(Blocks.BLUE_CONCRETE.getDefaultState(),
                    AbstractBlock.Settings.copy(Blocks.BLUE_CONCRETE)));

    public static final SlabBlock BLUE_CONCRETE_SLABS = registerWithItem("blue_concrete_slab",
            new SlabBlock(AbstractBlock.Settings.copy(Blocks.BLUE_CONCRETE)));

    public static final EdgeBlock BLUE_CONCRETE_EDGE = registerWithItem("blue_concrete_edge",
            new EdgeBlock(AbstractBlock.Settings.copy(Blocks.BLUE_CONCRETE)));

    // Brown
    public static final StairsBlock BROWN_CONCRETE_STAIRS = registerWithItem("brown_concrete_stairs",
            new StairsBlock(Blocks.BROWN_CONCRETE.getDefaultState(),
                    AbstractBlock.Settings.copy(Blocks.BROWN_CONCRETE)));

    public static final SlabBlock BROWN_CONCRETE_SLABS = registerWithItem("brown_concrete_slab",
            new SlabBlock(AbstractBlock.Settings.copy(Blocks.BROWN_CONCRETE)));

    public static final EdgeBlock BROWN_CONCRETE_EDGE = registerWithItem("brown_concrete_edge",
            new EdgeBlock(AbstractBlock.Settings.copy(Blocks.BROWN_CONCRETE)));

    // Green
    public static final StairsBlock GREEN_CONCRETE_STAIRS = registerWithItem("green_concrete_stairs",
            new StairsBlock(Blocks.GREEN_CONCRETE.getDefaultState(),
                    AbstractBlock.Settings.copy(Blocks.GREEN_CONCRETE)));

    public static final SlabBlock GREEN_CONCRETE_SLABS = registerWithItem("green_concrete_slab",
            new SlabBlock(AbstractBlock.Settings.copy(Blocks.GREEN_CONCRETE)));

    public static final EdgeBlock GREEN_CONCRETE_EDGE = registerWithItem("green_concrete_edge",
            new EdgeBlock(AbstractBlock.Settings.copy(Blocks.GREEN_CONCRETE)));

    // Red
    public static final StairsBlock RED_CONCRETE_STAIRS = registerWithItem("red_concrete_stairs",
            new StairsBlock(Blocks.RED_CONCRETE.getDefaultState(),
                    AbstractBlock.Settings.copy(Blocks.RED_CONCRETE)));

    public static final SlabBlock RED_CONCRETE_SLABS = registerWithItem("red_concrete_slab",
            new SlabBlock(AbstractBlock.Settings.copy(Blocks.RED_CONCRETE)));

    public static final EdgeBlock RED_CONCRETE_EDGE = registerWithItem("red_concrete_edge",
            new EdgeBlock(AbstractBlock.Settings.copy(Blocks.RED_CONCRETE)));

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

    private static void registerOxidizableBlocks() {
        OxidizableBlocksRegistry.registerOxidizableBlockPair(COPPER_WALL, EXPOSED_COPPER_WALL);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(EXPOSED_COPPER_WALL, WEATHERED_COPPER_WALL);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(WEATHERED_COPPER_WALL, OXIDIZED_COPPER_WALL);

        OxidizableBlocksRegistry.registerWaxableBlockPair(COPPER_WALL, WAXED_COPPER_WALL);
        OxidizableBlocksRegistry.registerWaxableBlockPair(EXPOSED_COPPER_WALL, WAXED_EXPOSED_COPPER_WALL);
        OxidizableBlocksRegistry.registerWaxableBlockPair(WEATHERED_COPPER_WALL, WAXED_WEATHERED_COPPER_WALL);
        OxidizableBlocksRegistry.registerWaxableBlockPair(OXIDIZED_COPPER_WALL, WAXED_OXIDIZED_COPPER_WALL);
    }

    public static void init() {
        registerOxidizableBlocks();
        YumeCraft.LOGGER.info("Registering blocks for " + YumeCraft.MOD_ID);
    }
}
