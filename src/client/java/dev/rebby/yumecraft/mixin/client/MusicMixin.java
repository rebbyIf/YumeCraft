package dev.rebby.yumecraft.mixin.client;

import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.sound.MusicSound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author RevoIDE
 * @author rebbyIf
 * Thank RevoIDE for helping me stop minecraft's music.
 */

@Mixin(MusicTracker.class)
public class MusicMixin {

    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    private void cancelMusic(MusicSound type, CallbackInfo ci) {

        //YumeCraft.LOGGER.info("Minecraft is playing music...");

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null && player.clientWorld.getDimensionEntry().getKey().orElseThrow().getValue()
                .getNamespace().equals(YumeCraft.MOD_ID)) {

            //YumeCraft.LOGGER.info("Music successfully canceled!");

            ci.cancel();

        }
    }
}