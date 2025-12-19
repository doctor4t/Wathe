package dev.doctor4t.wathe.mixin;


import dev.doctor4t.wathe.index.WatheDataComponentTypes;
import dev.doctor4t.wathe.index.WatheItems;
import dev.doctor4t.wathe.item.ItemWithSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void arsenal$setTridentOwner(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if (stack.getItem() instanceof ItemWithSkin && entity instanceof PlayerEntity player) {
            stack.set(WatheDataComponentTypes.OWNER, player.getUuidAsString());
        }
    }
}
