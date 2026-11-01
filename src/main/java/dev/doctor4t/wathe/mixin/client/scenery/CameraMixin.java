package dev.doctor4t.wathe.mixin.client.scenery;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.WatheConfig;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.client.render.WatheShake;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {
    @Unique
    private static final PerlinNoiseSampler sampler = new PerlinNoiseSampler(Random.create());

    @Unique
    private static float randomizeOffset(int offset) {
        float intensity = 0.2f;

        float min = -intensity * 2;
        float max = intensity * 2;
        float sampled = (float) sampler.sample((MinecraftClient.getInstance().world.getTime() % 24000L + MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(false)) / intensity, offset, 0) * 1.5f;
        return min >= max ? min : sampled * max;
    }

    @Inject(method = "update", at = @At("RETURN"))
    private void wathe$doScreenshake(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        WatheShake.active = false;
        if (!(WatheClient.isTrainMoving() && !WatheConfig.disableScreenShake)) return;

        Camera camera = (Camera) (Object) this;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        assert player != null;
        int age = player.age;
        float v = (1 + (1 - WatheClient.moodComponent.getMood())) * 2.5f;
        float amplitude = .0025f;
        float strength = 0.5f;
        float yawOffset = 0, pitchOffset = 0;

        if (Wathe.isSkyVisibleAdjacent(player)) {
            amplitude = .01f;
            strength = 1f;
            if (Wathe.isExposedToWind(player)) {
                yawOffset = 1.5f * randomizeOffset(10);
                pitchOffset = 1.5f * randomizeOffset(-10);
            }
        }
        amplitude *= v;

        WatheShake.offY = (float) (Math.sin((age + tickDelta) * strength) / 2f * amplitude);
        WatheShake.offZ = (float) (Math.cos((age + tickDelta) * strength) * amplitude);
        WatheShake.yawOffDeg = yawOffset;
        WatheShake.pitchOffDeg = pitchOffset;
        WatheShake.camPitchDeg = camera.getPitch();
        WatheShake.active = true;
    }
}
