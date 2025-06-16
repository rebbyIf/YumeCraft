package dev.rebby.yumecraft.util;

import dev.rebby.yumecraft.YumeCraft;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TeleportTarget;
import org.jetbrains.annotations.NotNull;

public class PlayerHandler {

    public static void init(){
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, amount) -> {
            if (entity instanceof ServerPlayerEntity player &&
                    player.getWorld().getRegistryKey().getValue().getNamespace().equals(YumeCraft.MOD_ID)){

                player.setHealth(20.0f);

                MinecraftServer server = ((ServerWorld)player.getWorld()).getServer();
                ServerWorld worldTo = server.getWorld(player.getSpawnPointDimension());

                TeleportTarget target = getTeleportTarget(server, player, worldTo);

                player.teleportTo(target);

                player.fallDistance = 0;

                return false;
            }

            return true;
        });
    }

    private static @NotNull TeleportTarget getTeleportTarget(MinecraftServer server, ServerPlayerEntity player, ServerWorld worldTo) {
        BlockPos spawnPos = server.getOverworld().getSpawnPos();

        if (player.getSpawnPointPosition() != null) {
            spawnPos = player.getSpawnPointPosition();
        }

        return new TeleportTarget(worldTo,
                spawnPos.toCenterPos(),
                player.getVelocity(),
                player.getYaw(),
                player.getPitch(),
                TeleportTarget.NO_OP);
    }
}
