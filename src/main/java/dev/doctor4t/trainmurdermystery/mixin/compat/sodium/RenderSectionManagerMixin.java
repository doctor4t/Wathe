package dev.doctor4t.trainmurdermystery.mixin.compat.sodium;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import net.caffeinemc.mods.sodium.client.gui.SodiumGameOptions;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderSectionManager.class)
public class RenderSectionManagerMixin {

    @Inject(method = "shouldUseOcclusionCulling",
            at = @At("HEAD"),
            remap = false,
            cancellable = true)
    private void tmm$forceNotUseOcclusionCulling(Camera camera, boolean spectator, CallbackInfoReturnable<Boolean> cir) {
        if (TMMClient.isTrainMoving()) {
            cir.setReturnValue(false);
        }
    }

    @ModifyExpressionValue(method = "getSearchDistance",
            at = @At(value = "FIELD",
                    target = "Lnet/caffeinemc/mods/sodium/client/gui/SodiumGameOptions$PerformanceSettings;useFogOcclusion:Z"),
            remap = false)
    private boolean tmm$forceNotUseFogOcclusion(boolean original) {
        if (TMMClient.isTrainMoving()) {
            return false;
        }
        return original;
    }
}
