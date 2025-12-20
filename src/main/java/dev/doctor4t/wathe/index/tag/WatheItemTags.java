package dev.doctor4t.wathe.index.tag;

import dev.doctor4t.wathe.Wathe;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public interface WatheItemTags {

    TagKey<Item> GUNS = create("guns");
    TagKey<Item> PSYCHOSIS_ITEMS = create("psychosis_items");

    private static TagKey<Item> create(String id) {
        return TagKey.of(RegistryKeys.ITEM, Wathe.id(id));
    }
}
