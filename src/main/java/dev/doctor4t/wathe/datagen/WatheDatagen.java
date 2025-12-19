package dev.doctor4t.wathe.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.dimension.DimensionOptions;

public class WatheDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        // this is so that the dimension options can actually generate
        DynamicRegistries.register(RegistryKeys.DIMENSION, DimensionOptions.CODEC);

        FabricDataGenerator.Pack pack = dataGenerator.createPack();
        pack.addProvider(WatheModelGen::new);
        pack.addProvider(WatheBlockTagGen::new);
        pack.addProvider(WatheItemTagGen::new);
        pack.addProvider(WatheLangGen::new);
        pack.addProvider(WatheBlockLootTableGen::new);
    }
}
