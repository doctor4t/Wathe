package dev.doctor4t.trainmurdermystery.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.trainmurdermystery.cca.PlayerPsychoComponent;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.index.tag.TMMItemTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @ModifyReturnValue(method = "hasOutline", at = @At("RETURN"))
    public boolean tmm$hasInstinctOutline(boolean original, @Local(argsOnly = true) Entity entity) {
        if (TMMClient.getInstinctHighlight(entity) != -1) return true;
        return original;
    }

    @WrapWithCondition(method = "doItemUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;resetEquipProgress(Lnet/minecraft/util/Hand;)V"
            ))
    private boolean tmm$cancelRevolverUpdateAnimation(HeldItemRenderer instance, Hand hand) {
        return !MinecraftClient.getInstance().player.getStackInHand(hand).isIn(TMMItemTags.GUNS);
    }

    @WrapOperation(method = "handleInputEvents", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I"))
    private void tmm$invalid(@NotNull PlayerInventory instance, int value, Operation<Void> original) {
        int oldSlot = instance.selectedSlot;
        PlayerPsychoComponent component = PlayerPsychoComponent.KEY.get(instance.player);
        if (component.getPsychoTicks() > 0 &&
                (instance.getStack(oldSlot).isOf(TMMItems.BAT)) &&
                (!instance.getStack(value).isOf(TMMItems.BAT))
        ) return;
        original.call(instance, value);
    }
}
