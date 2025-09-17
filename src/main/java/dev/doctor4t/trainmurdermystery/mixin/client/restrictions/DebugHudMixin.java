package dev.doctor4t.trainmurdermystery.mixin.client.restrictions;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.doctor4t.trainmurdermystery.client.TrainMurderMysteryClient;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DebugHud.class)
public class DebugHudMixin {
    @ModifyReturnValue(method = "shouldShowDebugHud", at = @At("RETURN"))
    public boolean shouldShowDebugHud(boolean original) {
        return !TrainMurderMysteryClient.isPlayerAliveAndInSurvival() && original;
    }
}
