package dev.doctor4t.wathe.entity;

import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.index.WatheParticles;
import dev.doctor4t.wathe.index.WatheSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import org.joml.Vector3d;

public class FirecrackerEntity extends Entity {
    public FirecrackerEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        double angle = Math.toRadians(this.getYaw() + 110);
        Vector3d particlePos = new Vector3d(Math.cos(angle), .1f, Math.sin(angle)).mul(0.3f);
        if (!(this.getWorld() instanceof ServerWorld serverWorld)) {
            if (this.age % 5 == 0)
                this.getWorld().addParticle(ParticleTypes.SMOKE, this.getX() + particlePos.x(), this.getY() + particlePos.y(), this.getZ() + particlePos.z(), 0, 0, 0);
        } else {
            if (this.age >= GameConstants.FIRECRACKER_TIMER) {
                serverWorld.playSound(null, this.getBlockPos(), WatheSounds.ITEM_REVOLVER_SHOOT, SoundCategory.PLAYERS, 5f, 1f + this.getRandom().nextFloat() * .1f - .05f);
                serverWorld.spawnParticles(WatheParticles.EXPLOSION, this.getX(), this.getY() + .1f, this.getZ(), 1, 0, 0, 0, 0);
                serverWorld.spawnParticles(ParticleTypes.SMOKE, this.getX(), this.getY() + .1f, this.getZ(), 25, 0, 0, 0, .05f);
                this.discard();
            }
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {

    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {

    }
}
