package dev.doctor4t.trainmurdermystery.mixin.client.restrictions;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.doctor4t.trainmurdermystery.client.TrainMurderMysteryClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin {
    @Shadow
    public abstract boolean equals(KeyBinding other);

    @Unique
    private boolean shouldSuppressKey() {
        if (TrainMurderMysteryClient.isPlayerAliveAndInSurvival()) {
            return this.equals(MinecraftClient.getInstance().options.swapHandsKey) ||
                    this.equals(MinecraftClient.getInstance().options.chatKey) ||
                    this.equals(MinecraftClient.getInstance().options.commandKey) ||
                    this.equals(MinecraftClient.getInstance().options.jumpKey) ||
                    this.equals(MinecraftClient.getInstance().options.playerListKey) ||
//                    this.equals(MinecraftClient.getInstance().options.togglePerspectiveKey) ||
                    this.equals(MinecraftClient.getInstance().options.advancementsKey);
        }
        return false;
    }

    @ModifyReturnValue(method = "wasPressed", at = @At("RETURN"))
    private boolean tmm$restrainWasPressedKeys(boolean original) {
        if (shouldSuppressKey()) return false; else return original;
    }

    @ModifyReturnValue(method = "isPressed", at = @At("RETURN"))
    private boolean tmm$restrainIsPressedKeys(boolean original) {
        if (shouldSuppressKey()) return false; else return original;
    }
}
