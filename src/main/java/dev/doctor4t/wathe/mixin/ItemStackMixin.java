package dev.doctor4t.wathe.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.wathe.util.AdventureUsable;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @WrapOperation(method = "useOnBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerAbilities;allowModifyWorld:Z"))
    public boolean useOnBlock(PlayerAbilities instance, Operation<Boolean> original) {
        if (this.getItem() instanceof AdventureUsable) return true;
        return original.call(instance);
    }
}