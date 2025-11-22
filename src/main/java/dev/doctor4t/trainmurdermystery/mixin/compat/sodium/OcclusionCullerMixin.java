package dev.doctor4t.trainmurdermystery.mixin.compat.sodium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import net.caffeinemc.mods.sodium.client.render.chunk.occlusion.OcclusionCuller;
import net.caffeinemc.mods.sodium.client.render.viewport.Viewport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OcclusionCuller.class)
public class OcclusionCullerMixin {

    @WrapOperation(
            method = "isWithinFrustum",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/caffeinemc/mods/sodium/client/render/viewport/Viewport;isBoxVisible(IIIFFF)Z"
            ),
            remap = false
    )
    private static boolean wrapIsBoxVisible(
            Viewport viewport,
            int cx, int cy, int cz,
            float sx, float sy, float sz,
            Operation<Boolean> original
    ) {
        return original.call(viewport, cx, cy, cz, sx, sy, sz) || TMMClient.isTrainMoving();
    }
}
