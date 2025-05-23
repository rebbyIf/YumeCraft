package dev.rebby.yumecraft.mixin;

import dev.rebby.yumecraft.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.TwistingVinesFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TwistingVinesFeature.class)
public class TwistingVinesFeatureMixin {
	@Inject(at = @At("TAIL"), method = "isNotSuitable", cancellable = true)
	private static void isNotSuitable(WorldAccess world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		BlockState blockState = world.getBlockState(pos.down());
		if (blockState.isOf(ModBlocks.BLUE_COBBLE)) {
			cir.setReturnValue(false);
		}
	}
}