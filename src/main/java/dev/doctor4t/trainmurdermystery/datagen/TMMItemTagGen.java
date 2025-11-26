package dev.doctor4t.trainmurdermystery.datagen;

import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.index.tag.TMMItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class TMMItemTagGen extends FabricTagProvider.ItemTagProvider {

    public TMMItemTagGen(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(TMMItemTags.GUNS)
                .add(TMMItems.REVOLVER)
                .add(TMMItems.DERRINGER);

        this.getOrCreateTagBuilder(TMMItemTags.PSYCHOSIS_ITEMS)
                .add(Items.AIR)
                .add(TMMItems.LETTER)
                .add(TMMItems.FIRECRACKER)
                .add(TMMItems.KNIFE)
                .add(TMMItems.REVOLVER)
                .add(TMMItems.GRENADE)
                .add(TMMItems.POISON_VIAL)
                .add(TMMItems.SCORPION)
                .add(TMMItems.LOCKPICK)
                .add(TMMItems.CROWBAR)
                .add(TMMItems.BODY_BAG);
    }
}
