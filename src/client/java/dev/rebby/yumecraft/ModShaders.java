package dev.rebby.yumecraft;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gl.ShaderProgram;

@Environment(EnvType.CLIENT)
public class ModShaders {

    private static ShaderProgram INFINITE_MALL_SKY = null;

    public static void setInfiniteMallSky(ShaderProgram infiniteMallSky) {
        INFINITE_MALL_SKY = infiniteMallSky;
    }

    public static ShaderProgram getInfiniteMallSky(){
        return INFINITE_MALL_SKY;
    }
}
