package dev.doctor4t.wathe.mixin.client.scenery;

import dev.doctor4t.wathe.client.render.WatheShake;
import net.minecraft.client.render.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @ModifyArg(method = "renderWorld", index = 5,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/render/RenderTickCounter;ZLnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/GameRenderer;Lnet/minecraft/client/render/LightmapTextureManager;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V"))
    private Matrix4f wathe$worldShake(Matrix4f view) {
        if (!WatheShake.active) return view;

        float pitch = (float) Math.toRadians(WatheShake.camPitchDeg);
        float pitchOff = (float) Math.toRadians(WatheShake.pitchOffDeg);
        float yawOff = (float) Math.toRadians(WatheShake.yawOffDeg);

        return new Matrix4f()
                .rotateX(pitch + pitchOff).rotateY(yawOff).rotateX(-pitch)
                .mul(view)
                .translate(0f, -WatheShake.offY, -WatheShake.offZ);
    }
}
