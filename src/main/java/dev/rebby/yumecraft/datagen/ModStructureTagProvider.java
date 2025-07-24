package dev.rebby.yumecraft.datagen;

import dev.rebby.yumecraft.YumeCraft;
import dev.rebby.yumecraft.tag.ModStructureTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.gen.structure.Structure;

import java.util.concurrent.CompletableFuture;

public class ModStructureTagProvider extends FabricTagProvider<Structure> {


    public ModStructureTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.STRUCTURE, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
//        getOrCreateTagBuilder(ModStructureTags.INFINITE_MALL_MALL)
//                .add(YumeCraft.id("infinite_mall/mall"));
    }
}
