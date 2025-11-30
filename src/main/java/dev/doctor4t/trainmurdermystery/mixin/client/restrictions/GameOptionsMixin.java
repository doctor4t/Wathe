package dev.doctor4t.trainmurdermystery.mixin.client.restrictions;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @ModifyReturnValue(method = "getPerspective", at = @At("RETURN"))
    public Perspective getPerspective(Perspective original) {
        if (TMMClient.isPlayerAliveAndInSurvival() && (TMMClient.gameComponent != null && TMMClient.gameComponent.isRunning())) {
            return Perspective.FIRST_PERSON;
        } else {
            return original;
        }
    }
}
