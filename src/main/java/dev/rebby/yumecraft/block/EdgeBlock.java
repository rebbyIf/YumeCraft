package dev.rebby.yumecraft.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Partially copied code from minecraft's source
 */
public class EdgeBlock extends TranslucentBlock {

    public static final MapCodec<EdgeBlock> CODEC = createCodec(EdgeBlock::new);

    public static final Map<Direction, BooleanProperty> FACING_PROPERTIES = ImmutableMap.copyOf(Util.make(Maps.newEnumMap(Direction.class), directions -> {
        directions.put(Direction.NORTH, Properties.NORTH);
        directions.put(Direction.EAST, Properties.EAST);
        directions.put(Direction.SOUTH, Properties.SOUTH);
        directions.put(Direction.WEST, Properties.WEST);
        directions.put(Direction.UP, Properties.UP);
        directions.put(Direction.DOWN, Properties.DOWN);
    }));

    public EdgeBlock(Settings settings) {
        super(settings);
        this.setDefaultState(
                this.stateManager.getDefaultState().with(Properties.NORTH, false).with(Properties.EAST, false).with(Properties.SOUTH, false).with(Properties.WEST, false).with(Properties.UP, false).with(Properties.DOWN, false)
        );
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        ctx.getWorld().updateNeighborsAlways(ctx.getBlockPos(), this.asBlock());
        return withConnectionProperties(ctx.getWorld(), ctx.getBlockPos(), this.getDefaultState());
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!oldState.isOf(state.getBlock())) {
            world.setBlockState(pos, withConnectionProperties(world, pos, state), Block.NOTIFY_ALL);

            BlockState up = world.getBlockState(pos.up());
            BlockState down = world.getBlockState(pos.down());
            BlockState north = world.getBlockState(pos.north());
            BlockState south = world.getBlockState(pos.south());
            BlockState east = world.getBlockState(pos.east());
            BlockState west = world.getBlockState(pos.west());

            if (up.isOf(state.getBlock())) {
                world.setBlockState(pos.up(), withConnectionProperties(world, pos.up(), up), Block.NOTIFY_ALL);
            }
            if (down.isOf(state.getBlock())) {
                world.setBlockState(pos.down(), withConnectionProperties(world, pos.down(), down), Block.NOTIFY_ALL);
            }
            if (north.isOf(state.getBlock())) {
                world.setBlockState(pos.north(), withConnectionProperties(world, pos.north(), north), Block.NOTIFY_ALL);
            }
            if (south.isOf(state.getBlock())) {
                world.setBlockState(pos.south(), withConnectionProperties(world, pos.south(), south), Block.NOTIFY_ALL);
            }
            if (east.isOf(state.getBlock())) {
                world.setBlockState(pos.east(), withConnectionProperties(world, pos.east(), east), Block.NOTIFY_ALL);
            }
            if (west.isOf(state.getBlock())) {
                world.setBlockState(pos.west(), withConnectionProperties(world, pos.west(), west), Block.NOTIFY_ALL);
            }
        }
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        BlockState up = world.getBlockState(pos.up());
        BlockState down = world.getBlockState(pos.down());
        BlockState north = world.getBlockState(pos.north());
        BlockState south = world.getBlockState(pos.south());
        BlockState east = world.getBlockState(pos.east());
        BlockState west = world.getBlockState(pos.west());

        if (up.isOf(state.getBlock())) {
            world.setBlockState(pos.up(), withConnectionProperties(world, pos.up(), up), Block.NOTIFY_ALL);
        }
        if (down.isOf(state.getBlock())) {
            world.setBlockState(pos.down(), withConnectionProperties(world, pos.down(), down), Block.NOTIFY_ALL);
        }
        if (north.isOf(state.getBlock())) {
            world.setBlockState(pos.north(), withConnectionProperties(world, pos.north(), north), Block.NOTIFY_ALL);
        }
        if (south.isOf(state.getBlock())) {
            world.setBlockState(pos.south(), withConnectionProperties(world, pos.south(), south), Block.NOTIFY_ALL);
        }
        if (east.isOf(state.getBlock())) {
            world.setBlockState(pos.east(), withConnectionProperties(world, pos.east(), east), Block.NOTIFY_ALL);
        }
        if (west.isOf(state.getBlock())) {
            world.setBlockState(pos.west(), withConnectionProperties(world, pos.west(), west), Block.NOTIFY_ALL);
        }
    }

    protected static BlockState withConnectionProperties(BlockView world, BlockPos pos, BlockState state) {
        BlockState blockState = world.getBlockState(pos.down());
        BlockState blockState2 = world.getBlockState(pos.up());
        BlockState blockState3 = world.getBlockState(pos.north());
        BlockState blockState4 = world.getBlockState(pos.east());
        BlockState blockState5 = world.getBlockState(pos.south());
        BlockState blockState6 = world.getBlockState(pos.west());
        Block block = state.getBlock();
        return state.withIfExists(Properties.DOWN, blockState.isOf(block))
                .withIfExists(Properties.UP, blockState2.isOf(block))
                .withIfExists(Properties.NORTH, blockState3.isOf(block))
                .withIfExists(Properties.EAST, blockState4.isOf(block))
                .withIfExists(Properties.SOUTH, blockState5.isOf(block))
                .withIfExists(Properties.WEST, blockState6.isOf(block));
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!world.isClient() && neighborState.equals(state)){
            return withConnectionProperties(world, pos, state);
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.NORTH, Properties.EAST, Properties.SOUTH, Properties.WEST, Properties.UP, Properties.DOWN);
    }

    @Override
    protected MapCodec<EdgeBlock> getCodec() {
        return CODEC;
    }


}
