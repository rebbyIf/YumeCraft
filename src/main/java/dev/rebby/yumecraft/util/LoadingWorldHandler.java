package dev.rebby.yumecraft.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;

public class LoadingWorldHandler {

    public static Long seed = null;

    public static void init(){
        ServerWorldEvents.LOAD.register((server, world) -> {
            seed = world.getSeed();
        });
    }

}
