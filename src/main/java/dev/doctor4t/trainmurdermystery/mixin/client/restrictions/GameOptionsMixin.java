package dev.doctor4t.trainmurdermystery.mixin.client.restrictions;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @ModifyReturnValue(method = "getPerspective", at = @At("RETURN"))
    public Perspective getPerspective(Perspective original) {
        if (GameFunctions.isPlayerAliveAndSurvival(MinecraftClient.getInstance().player)) {
            return Perspective.FIRST_PERSON;
        } else {
            return original;
        }
    }
}
