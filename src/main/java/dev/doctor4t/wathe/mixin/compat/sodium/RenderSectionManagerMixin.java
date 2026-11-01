package dev.doctor4t.wathe.mixin.compat.sodium;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.compat.IrisHelper;
import dev.doctor4t.wathe.compat.WatheGraphState;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderSectionManager.class)
public class RenderSectionManagerMixin {
    @Shadow private boolean needsGraphUpdate;

    @Inject(method = "shouldUseOcclusionCulling",
            at = @At("HEAD"),
            remap = false,
            cancellable = true)
    private void wathe$forceNotUseOcclusionCulling(Camera camera, boolean spectator, CallbackInfoReturnable<Boolean> cir) {
        if (WatheClient.isTrainMoving()) {
            cir.setReturnValue(false);
        }
    }

    @ModifyExpressionValue(method = "getSearchDistance",
            at = @At(value = "FIELD",
                    target = "Lnet/caffeinemc/mods/sodium/client/gui/SodiumOptions$PerformanceSettings;useFogOcclusion:Z"),
            remap = false)
    private boolean wathe$forceNotUseFogOcclusion(boolean original) {
        if (WatheClient.isTrainMoving()) {
            return false;
        }
        return original;
    }

    @Inject(method = "needsUpdate", at = @At("HEAD"), remap = false)
    private void wathe$applyMainDirty(CallbackInfoReturnable<Boolean> cir) {
        if (WatheGraphState.shouldDisableDirty() && /*!IrisHelper.isShadowPass() &&*/ WatheGraphState.mainDirty) {
            WatheGraphState.mainDirty = false;
            this.needsGraphUpdate = true;
        }
    }

    @Inject(method = "onSectionAdded", at = @At("HEAD"), remap = false)
    private void wathe$sectionAdded(int x, int y, int z, CallbackInfo ci) {
        WatheGraphState.invalidateFrames = 5;
    }

    @Inject(method = "onSectionRemoved", at = @At("HEAD"), remap = false)
    private void wathe$sectionRemoved(int x, int y, int z, CallbackInfo ci) {
        WatheGraphState.invalidateFrames = 5;
    }

    @Inject(method = "needsUpdate", at = @At("HEAD"), remap = false)
    private void wathe$forceMainRebuild(CallbackInfoReturnable<Boolean> cir) {
        if (WatheGraphState.invalidateFrames > 0 && !IrisHelper.isShadowPass()) {
            this.needsGraphUpdate = true;
        }
    }
}
