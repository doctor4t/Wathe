package dev.doctor4t.trainmurdermystery.mixin.client.scenery;

import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.index.TMMParticles;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World  {
    protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Shadow public abstract void addParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ);

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "tick", at = @At("TAIL"))
    public void tmm$addSnowflakes(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (TMMClient.isTrainMoving()) {
            ClientPlayerEntity player = client.player;
            Random random = player.getRandom();
            for (int i = 0; i < 200; i++) {
                Vec3d pos = new Vec3d(player.getX() - 20f + random.nextFloat(), player.getY() + (random.nextFloat() * 2 - 1) * 10f, player.getZ() + (random.nextFloat() * 2 - 1) * 10f);
                if (this.client.world.isSkyVisible(BlockPos.ofFloored(pos))) {
                    Vec3d playerVel = player.getMovement();
                    this.addParticle(TMMParticles.SNOWFLAKE, pos.getX() + playerVel.getX(), pos.getY() + playerVel.getY(), pos.getZ() + playerVel.getZ(), 2 + playerVel.getX(), 0 + playerVel.getY(), 0 + playerVel.getZ());
                }
            }
        }
    }
}
