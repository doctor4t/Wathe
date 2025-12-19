package dev.doctor4t.wathe.entity;

import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.doctor4t.wathe.index.WatheEntities;
import dev.doctor4t.wathe.index.WatheItems;
import dev.doctor4t.wathe.index.WatheParticles;
import dev.doctor4t.wathe.index.WatheSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class GrenadeEntity extends ThrownItemEntity {
    public GrenadeEntity(EntityType<?> ignored, World world) {
        super(WatheEntities.GRENADE, world);
    }

    @Override
    protected Item getDefaultItem() {
        return WatheItems.THROWN_GRENADE;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (this.getWorld() instanceof ServerWorld world) {
            // Consider sending this in one payload to reduce packets sent - SkyNotTheLimit
            world.playSound(null, this.getBlockPos(), WatheSounds.ITEM_GRENADE_EXPLODE, SoundCategory.PLAYERS, 5f, 1f + this.getRandom().nextFloat() * .1f - .05f);
            world.spawnParticles(WatheParticles.BIG_EXPLOSION, this.getX(), this.getY() + .1f, this.getZ(), 1, 0, 0, 0, 0);
            world.spawnParticles(ParticleTypes.SMOKE, this.getX(), this.getY() + .1f, this.getZ(), 100, 0, 0, 0, .2f);
            world.spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, this.getDefaultItem().getDefaultStack()), this.getX(), this.getY() + .1f, this.getZ(), 100, 0, 0, 0, 1f);

            for (ServerPlayerEntity player : world.getPlayers(serverPlayerEntity ->
                    this.getBoundingBox().expand(3f).contains(serverPlayerEntity.getPos()) &&
                            GameFunctions.isPlayerAliveAndSurvival(serverPlayerEntity))) {
                GameFunctions.killPlayer(player, true, this.getOwner() instanceof PlayerEntity playerEntity ? playerEntity : null, GameConstants.DeathReasons.GRENADE);
            }

            this.discard();
        }
    }
}