package dev.doctor4t.trainmurdermystery.mixin.client.scenery;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.TMMConfig;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.noise.PerlinNoiseSampler;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow
    public abstract void setRotation(float yaw, float pitch);

    @Shadow
    public abstract void setPos(Vec3d pos);

    @Shadow
    public abstract Vec3d getPos();

    @Shadow
    public abstract float getYaw();

    @Shadow
    public abstract float getPitch();

    @Unique
    private static final PerlinNoiseSampler tmm$sampler = new PerlinNoiseSampler(Random.create());

    @Unique
    private static float tmm$randomizeOffset(int offset) {
        float intensity = 0.2f;

        float min = -intensity * 2;
        float max = intensity * 2;
        float sampled = (float) tmm$sampler.sample((MinecraftClient.getInstance().world.getTime() % 24000L + MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(false)) / intensity, offset, 0) * 1.5f;
        return min >= max ? min : sampled * max;
    }

    @Inject(method = "update", at = @At("RETURN"))
    private void doScreenshake(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (!TMMClient.isTrainMoving() || TMMConfig.disableScreenShake) {
            return;
        }

        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        int age = player.age;
        float v = (1 + (1 - TMMClient.moodComponent.getMood())) * 2.5f;
        float amplitude = .0025f;
        float strength = 0.5f;

        float yawOffset = 0;
        float pitchOffset = 0;

        if (TMM.isSkyVisibleAdjacent(player)) {
            amplitude = .01f;
            strength = 1f;

            if (TMM.isExposedToWind(player)) {
                yawOffset = 1.5f * tmm$randomizeOffset(10);
                pitchOffset = 1.5f * tmm$randomizeOffset(-10);
            }
        }

        amplitude *= v;

        this.setRotation(this.getYaw() + yawOffset, this.getPitch() + pitchOffset);
        this.setPos(this.getPos().add(0, Math.sin((age + tickDelta) * strength) / 2f * amplitude, Math.cos((age + tickDelta) * strength) * amplitude));
    }
}
