package dev.rebby.yumecraft.sound;

import dev.rebby.yumecraft.YumeCraft;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;

@Environment(EnvType.CLIENT)
public class SoundsHandler {

    public static void register(){

        ClientTickEvents.START_WORLD_TICK.register((world) -> {

            if (world.isClient && world.getDimensionEntry().getKey().orElseThrow().getValue().getNamespace()
                    .equals(YumeCraft.MOD_ID)) {

                MinecraftClient.getInstance().getSoundManager().stopSounds(null, SoundCategory.MUSIC);

                //YumeCraft.LOGGER.info("Turning off music...");

            }

        });
    }

}
