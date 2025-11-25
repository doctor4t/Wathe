package dev.doctor4t.trainmurdermystery.api;

import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class GameMode {
    public final Identifier identifier;
    public final int defaultStartTime;
    public final int minPlayerCount;

    /**
     * @param identifier the game mode identifier
     * @param defaultStartTime the default time at which the timer will be set at the start of the game mode, in minutes
     * @param minPlayerCount the minimum amount of players required to start the game mode
     */
    public GameMode(Identifier identifier, int defaultStartTime, int minPlayerCount) {
        this.identifier = identifier;
        this.defaultStartTime = defaultStartTime;
        this.minPlayerCount = minPlayerCount;
    }

    public void readFromNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {

    }

    public void writeToNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {

    }

    public void tickCommonGameLoop() {}

    public void tickClientGameLoop() {}

    public abstract void tickServerGameLoop(ServerWorld serverWorld, GameWorldComponent gameWorldComponent);

    public abstract void initializeGame(ServerWorld serverWorld, GameWorldComponent gameWorldComponent, List<ServerPlayerEntity> players);

    public void finalizeGame(ServerWorld serverWorld, GameWorldComponent gameWorldComponent) {

    }
}