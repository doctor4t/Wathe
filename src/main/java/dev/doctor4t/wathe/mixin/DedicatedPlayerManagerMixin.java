package dev.doctor4t.wathe.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.dedicated.DedicatedPlayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DedicatedPlayerManager.class)
public class DedicatedPlayerManagerMixin {
    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/dedicated/DedicatedPlayerManager;setViewDistance(I)V"))
    public void wathe$forceServerViewDistance(DedicatedPlayerManager instance, int i, Operation<Void> original) {
        original.call(instance, 32);
    }
}