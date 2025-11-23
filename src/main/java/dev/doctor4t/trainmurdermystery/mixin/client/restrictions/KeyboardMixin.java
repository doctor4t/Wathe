package dev.doctor4t.trainmurdermystery.mixin.client.restrictions;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {

    @Shadow
    protected abstract void debugLog(String key, Object... args);

    @WrapOperation(method = "processF3", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private void disableF3Keybinds(MinecraftClient instance, Screen screen, Operation<Void> original) {
        if (!TMMClient.isPlayerAliveAndInSurvival()) {
            original.call(instance, screen);
        }
        this.debugLog("debug.gamemodes.error");
    }
}
