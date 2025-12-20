package dev.doctor4t.wathe.api;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public abstract class MapEffect {
    public final Identifier identifier;

    /**
     * @param identifier the map effect identifier
     */
    public MapEffect(Identifier identifier) {
        this.identifier = identifier;
    }

    public abstract void initializeMapEffects(ServerWorld serverWorld, List<ServerPlayerEntity> players);

    public abstract void finalizeMapEffects(ServerWorld serverWorld, List<ServerPlayerEntity> players);
}