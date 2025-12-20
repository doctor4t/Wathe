package dev.doctor4t.wathe.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.wathe.util.AdventureUsable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    @Final
    MinecraftClient client;

    @WrapOperation(method = "shouldRenderBlockOutline", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerAbilities;allowModifyWorld:Z"))
    public boolean useOnBlock(PlayerAbilities instance, Operation<Boolean> original) {
        if (this.client.getCameraEntity() instanceof LivingEntity entity && entity.getMainHandStack().getItem() instanceof AdventureUsable)
            return true;
        return original.call(instance);
    }
}