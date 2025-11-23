package dev.doctor4t.trainmurdermystery.index.tag;

import dev.doctor4t.trainmurdermystery.TMM;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public interface TMMItemTags {

    TagKey<Item> GUNS = create("guns");
    TagKey<Item> PSYCHOSIS_ITEMS = create("psychosis_items");

    private static TagKey<Item> create(String id) {
        return TagKey.of(RegistryKeys.ITEM, TMM.id(id));
    }
}
