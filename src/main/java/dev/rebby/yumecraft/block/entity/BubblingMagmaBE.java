package dev.rebby.yumecraft.block.entity;

import dev.rebby.yumecraft.particle.ModParticles;
import dev.rebby.yumecraft.util.TickableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class BubblingMagmaBE extends BlockEntity implements TickableBlockEntity {
    public BubblingMagmaBE(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.BUBBLING_MAGMA_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void tick() {
        if (this.world == null || this.world.isClient){
            return;
        }

        ((ServerWorld) world).spawnParticles(ModParticles.LINGERING_BUBBLE, pos.getX() + 0.5, pos.up().getY() + 0.5,
                pos.getZ() + 0.5, 2, 0, 1, 0, 0.5);
    }
}
