package dev.doctor4t.wathe.datagen;

import dev.doctor4t.wathe.index.WatheItems;
import dev.doctor4t.wathe.index.tag.WatheItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class WatheItemTagGen extends FabricTagProvider.ItemTagProvider {

    public WatheItemTagGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(WatheItemTags.GUNS)
                .add(WatheItems.REVOLVER)
                .add(WatheItems.DERRINGER);

        this.getOrCreateTagBuilder(WatheItemTags.PSYCHOSIS_ITEMS)
                .add(Items.AIR)
                .add(WatheItems.LETTER)
                .add(WatheItems.FIRECRACKER)
                .add(WatheItems.KNIFE)
                .add(WatheItems.REVOLVER)
                .add(WatheItems.GRENADE)
                .add(WatheItems.POISON_VIAL)
                .add(WatheItems.SCORPION)
                .add(WatheItems.LOCKPICK)
                .add(WatheItems.CROWBAR)
                .add(WatheItems.BODY_BAG);
    }
}
