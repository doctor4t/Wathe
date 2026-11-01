package dev.doctor4t.wathe.mixin.compat.sodium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.wathe.compat.WatheGraphState;
import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSectionManager;
import net.caffeinemc.mods.sodium.client.render.viewport.Viewport;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SodiumWorldRenderer.class)
public class SodiumWorldRendererMixin {
    @Unique private int wathe$secX = Integer.MIN_VALUE, wathe$secY, wathe$secZ;
    @Unique private boolean wathe$wasActive;

    @Inject(method = "setupTerrain", at = @At("HEAD"), remap = false)
    private void wathe$detectChange(Camera camera, Viewport viewport, boolean spectator, boolean updateChunksImmediately, CallbackInfo ci) {
        if (WatheGraphState.invalidateFrames > 0) WatheGraphState.invalidateFrames--;

        boolean active = WatheGraphState.shouldDisableDirty();
        BlockPos p = camera.getBlockPos();
        int sectionX = p.getX() >> 4;
        int sectionY = p.getY() >> 4;
        int sectionZ = p.getZ() >> 4;

        if (sectionX != wathe$secX || sectionY  != wathe$secY || sectionZ != wathe$secZ || active != wathe$wasActive)
            WatheGraphState.mainDirty = true;

        // This is also weird and horrible, but sodium essentially does the exact same thing, except
        // it checks for camera position, world position and camera rotation, so I don't care. It's faster,
        // and that's all that matters.
        wathe$secX = sectionX;
        wathe$secY = sectionY;
        wathe$secZ = sectionZ;
        wathe$wasActive = active;
    }

    @WrapOperation(method = "setupTerrain", remap = false,
            at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/RenderSectionManager;markGraphDirty()V"))
    private void wathe$limitGraphDirty(RenderSectionManager instance, Operation<Void> original) {
        if (!WatheGraphState.shouldDisableDirty()) original.call(instance);
    }
}
