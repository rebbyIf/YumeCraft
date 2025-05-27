package dev.rebby.yumecraft.world.gen.chunk_generator;

import com.mojang.serialization.Codec;
import dev.rebby.yumecraft.YumeCraft;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModChunkGenerators {

    public static void register(){
        Registry.register(Registries.CHUNK_GENERATOR, Identifier.of(YumeCraft.MOD_ID, "fractal"), FractalChunkGenerator.CODEC);
        Registry.register(Registries.CHUNK_GENERATOR, Identifier.of(YumeCraft.MOD_ID, "structure"), StructureChunkGenerator.CODEC);
    }

}
