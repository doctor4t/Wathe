package dev.doctor4t.wathe.mixin.client.restrictions;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.doctor4t.wathe.client.WatheClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @ModifyReturnValue(method = "getPerspective", at = @At("RETURN"))
    public Perspective wathe$getPerspective(Perspective original) {
        if (WatheClient.isPlayerAliveAndInSurvival() && (WatheClient.gameComponent != null && WatheClient.gameComponent.isRunning())) {
            return Perspective.FIRST_PERSON;
        } else {
            return original;
        }
    }
}
