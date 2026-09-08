package dev.doctor4t.wathe.item;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.index.WatheCosmetics;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;

public class RevolverItem extends PistolItem implements SkinnedItem {

    public RevolverItem(Settings settings) {
        super(settings);
    }

    @Override
    public WatheCosmetics.ItemSkinsManager getSkinManager() {
        return WatheCosmetics.REVOLVER_SKINS_MANAGER;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && otherStack.isEmpty()) {
            if (Wathe.isSupporter(player)) {
                WatheCosmetics.ItemSkinsManager.Skin currentSkin = this.getSkinManager().fromString(WatheCosmetics.getSkin(stack));
                WatheCosmetics.setSkin(player, stack, this.getSkinManager().getNext(currentSkin).getName());
            }

            return true;
        } else return false;
    }
}
