package dev.doctor4t.wathe.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class FlashParticle extends SpriteBillboardParticle {
    protected FlashParticle(ClientWorld world, double x, double y, double z,
                            double vx, double vy, double vz,
                            float scale) {
        super(world, x, y, z, vx, vy, vz);

        this.maxAge = 3;
        this.scale = scale;
        this.setVelocity(vx, vy, vz);

        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
        this.alpha = 1f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getBrightness(float tint) {
        return 0xF000F0;
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = 1.0f - ((float) this.age / this.maxAge);
    }

    public static class GunshotFactory extends Factory<SimpleParticleType> {
        public GunshotFactory(SpriteProvider sprites) {
            super(sprites);
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return super.createParticle(parameters, world, x, y, z, velocityX, velocityY, velocityZ, 0.2f);
        }
    }

    public static class ExplosionFactory extends Factory<SimpleParticleType> {
        public ExplosionFactory(SpriteProvider sprites) {
            super(sprites);
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return super.createParticle(parameters, world, x, y, z, velocityX, velocityY, velocityZ, 0.4f);
        }
    }

    public static class BigExplosionFactory extends Factory<SimpleParticleType> {
        public BigExplosionFactory(SpriteProvider sprites) {
            super(sprites);
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return super.createParticle(parameters, world, x, y, z, velocityX, velocityY, velocityZ, .8f);
        }
    }

    public static abstract class Factory<DefaultParticleType extends ParticleEffect> implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(DefaultParticleType type, ClientWorld world,
                                       double x, double y, double z,
                                       double vx, double vy, double vz,
                                       float scale) {
            FlashParticle particle = new FlashParticle(world, x, y, z, vx, vy, vz, scale);
            particle.setSprite(this.sprites);
            return particle;
        }
    }
}
