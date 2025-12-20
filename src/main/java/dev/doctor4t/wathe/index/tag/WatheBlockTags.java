package dev.doctor4t.wathe.index.tag;

import dev.doctor4t.wathe.Wathe;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public interface WatheBlockTags {

    TagKey<Block> BRANCHES = create("branches");
    TagKey<Block> VENT_SHAFTS = create("vent_shafts");
    TagKey<Block> VENT_HATCHES = create("vent_hatches");
    TagKey<Block> WALKWAYS = create("walkways");
    TagKey<Block> SPRINKLERS = create("sprinklers");

    private static TagKey<Block> create(String id) {
        return TagKey.of(RegistryKeys.BLOCK, Wathe.id(id));
    }
}
