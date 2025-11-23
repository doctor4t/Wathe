package dev.doctor4t.trainmurdermystery.mixin.client.restrictions;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin {
    @Shadow
    public abstract boolean equals(KeyBinding other);

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @Unique
    private boolean tmm$shouldSuppressKey() {
        if (TMMClient.isPlayerAliveAndInSurvival()) {
            GameOptions options = MinecraftClient.getInstance().options;
            return this.equals(options.swapHandsKey) ||
                    this.equals(options.chatKey) ||
                    this.equals(options.commandKey) ||
                    this.equals(options.jumpKey) ||
                    this.equals(options.togglePerspectiveKey) ||
                    this.equals(options.dropKey) ||
                    this.equals(options.advancementsKey);
        }
        return false;
    }

    @ModifyReturnValue(method = "wasPressed", at = @At("RETURN"))
    private boolean restrainWasPressedKeys(boolean original) {
        return !this.tmm$shouldSuppressKey() && original;
    }

    @ModifyReturnValue(method = "isPressed", at = @At("RETURN"))
    private boolean restrainIsPressedKeys(boolean original) {
        return !this.tmm$shouldSuppressKey() && original;
    }

    @ModifyReturnValue(method = "matchesKey", at = @At("RETURN"))
    private boolean restrainMatchesKey(boolean original) {
        return !this.tmm$shouldSuppressKey() && original;
    }
}
