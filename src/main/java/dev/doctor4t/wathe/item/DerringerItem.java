package dev.doctor4t.wathe.item;

import dev.doctor4t.wathe.index.WatheDataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class DerringerItem extends RevolverItem {
    public DerringerItem(Settings settings) {
        super(settings);
    }

    public boolean hasBeenUsed(ItemStack stack, PlayerEntity user) {
        return stack.getOrDefault(WatheDataComponentTypes.USED, false);
    }

    public float getRange(PlayerEntity user, ItemStack stack) {
        return 7f;
    }
}
