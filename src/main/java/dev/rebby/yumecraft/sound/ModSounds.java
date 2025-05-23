package dev.rebby.yumecraft.sound;

import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent MUS_000 = registerSoundEvent("music.000");
    public static final SoundEvent MUS_001 = registerSoundEvent("music.001");
    public static final SoundEvent MUS_001_SLOWED_90 = registerSoundEvent("music.001.slowed90");

    private static SoundEvent registerSoundEvent(String name){
        Identifier id = Identifier.of(YumeCraft.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init(){
        YumeCraft.LOGGER.info("Registering sounds for " + YumeCraft.MOD_ID);
    }
}
