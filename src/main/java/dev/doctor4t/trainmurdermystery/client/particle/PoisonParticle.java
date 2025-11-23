package dev.doctor4t.trainmurdermystery.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;

public class PoisonParticle extends SpriteBillboardParticle {
    protected PoisonParticle(ClientWorld clientWorld,
                             double x, double y, double z, double vx, double vy, double vz) {
        super(clientWorld, x, y, z, vx, vy, vz);

        this.maxAge = 16;
        this.scale = 0.2f;
        this.setVelocity(
                vx + (world.getRandom().nextBetween(0, 100) / 4000f) * (world.getRandom().nextBoolean() ? -1 : 1),
                vy,
                vz + (world.getRandom().nextBetween(0, 100) / 4000f) * (world.getRandom().nextBoolean() ? -1 : 1));

        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
        this.alpha = 1f;

        this.velocityMultiplier = 0.96f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        super.tick();
        this.alpha = 1.0f - ((float) this.age / this.maxAge);
    }

    public static class Factory<DefaultParticleType extends ParticleEffect> implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(DefaultParticleType type, ClientWorld world,
                                       double x, double y, double z,
                                       double vx, double vy, double vz) {
            PoisonParticle particle = new PoisonParticle(world, x, y, z, vx, vy, vz);
            particle.setSprite(this.sprites);
            return particle;
        }
    }
}
