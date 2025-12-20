package dev.doctor4t.wathe.mixin.client.scenery;

import dev.doctor4t.wathe.WatheConfig;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.index.WatheBlocks;
import dev.doctor4t.wathe.index.WatheParticles;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
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
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
    protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Shadow
    public abstract void addParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ);

    @Shadow
    @Final
    private MinecraftClient client;

    @Final
    @Shadow
    @Mutable
    private static Set<Item> BLOCK_MARKER_ITEMS;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void wathe$addCustomBlockMarkers(ClientPlayNetworkHandler networkHandler, ClientWorld.Properties properties, RegistryKey registryRef, RegistryEntry dimensionTypeEntry, int loadDistance, int simulationDistance, Supplier profiler, WorldRenderer worldRenderer, boolean debugWorld, long seed, CallbackInfo ci) {
        BLOCK_MARKER_ITEMS = new HashSet<>(BLOCK_MARKER_ITEMS);
        BLOCK_MARKER_ITEMS.add(WatheBlocks.BARRIER_PANEL.asItem());
        BLOCK_MARKER_ITEMS.add(WatheBlocks.LIGHT_BARRIER.asItem());
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void wathe$addSnowflakes(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        if (WatheClient.isTrainMoving() && WatheClient.getTrainComponent().isSnowing() && WatheConfig.snowOptLevel != WatheConfig.SnowModeConfig.TURN_OFF) {
            ClientPlayerEntity player = client.player;
            Random random = player.getRandom();
            for (int i = 0; i < 200; i++) {
                Vec3d playerVel = player.getMovement();
                Vec3d pos = new Vec3d(player.getX() - 20f + random.nextFloat() + playerVel.getX(), player.getY() + (random.nextFloat() * 2 - 1) * 10f + playerVel.getY(), player.getZ() + (random.nextFloat() * 2 - 1) * 10f + playerVel.getZ());
                if (this.client.world.isSkyVisible(BlockPos.ofFloored(pos))) {
                    this.addParticle(WatheParticles.SNOWFLAKE, pos.getX(), pos.getY(), pos.getZ(), 2 + playerVel.getX(), playerVel.getY(), playerVel.getZ());
                }
            }
        }
    }
}
