package dev.doctor4t.wathe.mixin;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin {
    @Shadow
    private int foodLevel;

    @Shadow
    private float saturationLevel;

    @Shadow
    private float exhaustion;

    @Inject(method = "update", at = @At("HEAD"))
    public void wathe$overrideFood(PlayerEntity player, CallbackInfo ci) {
        this.foodLevel = 20;
        this.saturationLevel = 0;
        this.exhaustion = 0;
    }
}
