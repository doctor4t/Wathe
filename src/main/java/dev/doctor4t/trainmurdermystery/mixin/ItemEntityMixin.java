package dev.doctor4t.trainmurdermystery.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.index.tag.TMMItemTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow
    public abstract @Nullable Entity getOwner();

    @Shadow
    private @Nullable UUID throwerUuid;

    @Shadow
    public abstract ItemStack getStack();

    @WrapMethod(method = "onPlayerCollision")
    public void tmm$preventGunPickup(PlayerEntity player, Operation<Void> original) {
        if (player.isCreative() || !this.getStack().isIn(TMMItemTags.GUNS) || (GameWorldComponent.KEY.get(player.getWorld()).isInnocent(player) && !player.equals(this.getOwner()) && !player.getInventory().contains(itemStack -> itemStack.isIn(TMMItemTags.GUNS)))) {
            original.call(player);
        }
    }
}
