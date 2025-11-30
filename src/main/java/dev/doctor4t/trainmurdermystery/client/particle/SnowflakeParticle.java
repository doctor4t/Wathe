package dev.doctor4t.trainmurdermystery.client.particle;

import dev.doctor4t.trainmurdermystery.TMMConfig;
import dev.doctor4t.trainmurdermystery.cca.AreasWorldComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;

public class SnowflakeParticle extends SpriteBillboardParticle {
    private final float yRand;
    private final float zRand;

    private float angleX;
    private float angleY;
    private float angleZ;
    private float prevAngleX;
    private float prevAngleY;
    private float prevAngleZ;
    private final float angleRandX;
    private final float angleRandY;
    private final float angleRandZ;

    public SnowflakeParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);

        if (TMMConfig.snowOptLevel == TMMConfig.SnowModeConfig.STRONG_OPTIMIZATION) collidesWithWorld = false;

        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;

        this.zRand = world.random.nextFloat() * 2 - 1;
        this.yRand = world.random.nextFloat() * 2 - 1;

        this.angleRandX = (world.random.nextFloat() * 2 - 1) * .1f;
        this.angleRandY = (world.random.nextFloat() * 2 - 1) * .1f;
        this.angleRandZ = (world.random.nextFloat() * 2 - 1) * .1f;

        this.maxAge = 40 + world.random.nextInt(20);
        this.scale = .1f + world.random.nextFloat() * .1f;
        this.alpha = 0f;

        this.setSprite(spriteProvider.getSprite(world.random));
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    public void tick() {
        super.tick();
        this.alpha += 0.01f;

        float v = .2f;
        this.velocityZ = MathHelper.sin(this.zRand + this.age / 2f + MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true)) * v;
        this.velocityY = -.1f + MathHelper.sin(this.yRand + this.age / 2f + MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true)) * v;

        this.prevAngleX = angleX;
        this.prevAngleY = angleY;
        this.prevAngleZ = angleZ;

        this.angleX += angleRandX;
        this.angleY += angleRandY;
        this.angleZ += angleRandZ;

        if (TMMConfig.snowOptLevel == TMMConfig.SnowModeConfig.STRONG_OPTIMIZATION && AreasWorldComponent.KEY.get(world).getParticleColliderArea().contains(x, y, z))
            markDead();
        if (this.onGround || this.velocityX == 0) {
            this.markDead();
        }
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Quaternionf quaternionf = new Quaternionf();
        this.getRotator().setRotation(quaternionf, camera, tickDelta);
        quaternionf.rotateXYZ(
                MathHelper.lerp(tickDelta, this.prevAngleX, this.angleX),
                MathHelper.lerp(tickDelta, this.prevAngleY, this.angleY),
                MathHelper.lerp(tickDelta, this.prevAngleZ, this.angleZ)
        );

        this.method_60373(vertexConsumer, camera, quaternionf, tickDelta);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new SnowflakeParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
        }
    }
}
