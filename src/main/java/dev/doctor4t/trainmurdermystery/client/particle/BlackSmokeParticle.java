package dev.doctor4t.trainmurdermystery.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;

public class BlackSmokeParticle extends SpriteBillboardParticle {
    BlackSmokeParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z);
        this.scale = 1f;
        this.setBoundingBoxSpacing(0.25F, 0.25F);
        this.maxAge = 100;

        this.gravityStrength = 3.0E-6F;
        this.velocityY = .5f;
        this.velocityX = 0;

        float col = world.random.nextBetween(30, 60) / 255f;
        this.setColor(col, col, col);
    }

    @Override
    public void tick() {
        this.scale += 0.05f;

        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ < this.maxAge && !(this.alpha <= 0.0F)) {
            this.velocityY *= .95f;
            this.velocityX = MathHelper.clamp(this.velocityX + .1f, 0, 1);
            this.move(this.velocityX, this.velocityY, this.velocityZ);

            if (this.age >= this.maxAge - 60 && this.alpha > 0.01F) {
                this.alpha -= 0.015F;
            }
        } else {
            this.markDead();
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            BlackSmokeParticle BlackSmokeParticle = new BlackSmokeParticle(clientWorld, d, e, f, g, h, i);
            BlackSmokeParticle.setAlpha(0.95F);
            BlackSmokeParticle.setSprite(this.spriteProvider);
            return BlackSmokeParticle;
        }
    }
}
