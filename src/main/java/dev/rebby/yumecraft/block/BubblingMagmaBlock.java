package dev.rebby.yumecraft.block;

import com.mojang.serialization.MapCodec;
import dev.rebby.yumecraft.block.entity.BubblingMagmaBE;
import dev.rebby.yumecraft.block.entity.ModBlockEntityTypes;
import dev.rebby.yumecraft.particle.ModParticles;
import dev.rebby.yumecraft.util.TickableBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BubblingMagmaBlock extends Block implements BlockEntityProvider {

    public static final MapCodec<BubblingMagmaBlock> CODEC = createCodec(BubblingMagmaBlock::new);

    public BubblingMagmaBlock(Settings settings) {
        super(settings);
    }

    @Override
    public MapCodec<BubblingMagmaBlock> getCodec() {
        return CODEC;
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return TickableBlockEntity.getTicker(world);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.BUBBLING_MAGMA_BLOCK_ENTITY.instantiate(pos, state);
    }
}
