package dev.doctor4t.trainmurdermystery.mixin.client.items;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashMap;
import java.util.UUID;

@Mixin(HeldItemFeatureRenderer.class)
public class HeldItemFeatureRendererMixin {
    @WrapOperation(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getMainHandStack()Lnet/minecraft/item/ItemStack;"))
    public ItemStack hideNoteAndRenderPsychosisItems(LivingEntity instance, Operation<ItemStack> original) {
        ItemStack ret = original.call(instance);

        if (ret.isOf(TMMItems.NOTE)) {
            ret = ItemStack.EMPTY;
        }

        if (TMMClient.moodComponent != null && TMMClient.moodComponent.isLowerThanMid()) { // make sure it's only the main hand item that's being replaced
            HashMap<UUID, ItemStack> psychosisItems = TMMClient.moodComponent.getPsychosisItems();
            UUID uuid = instance.getUuid();
            if (psychosisItems.containsKey(uuid)) {
                ret = psychosisItems.get(uuid);
            }
        }

        return ret;
    }
}
