package dev.doctor4t.wathe.mixin.client.restrictions;

import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.wathe.api.WatheRoles;
import dev.doctor4t.wathe.client.WatheClient;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Colors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    // changes color parameter constant
    @ModifyArg(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/Text;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I", ordinal = 1), index = 3)
    protected int renderLabelIfPresent(int color, @Local(argsOnly = true) T entity) {
        return WatheClient.gameComponent.isRole(entity.getUuid(), WatheRoles.KILLER) ? Colors.RED : color;
    }
}
