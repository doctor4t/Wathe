package dev.doctor4t.wathe.game.mapeffect;

import dev.doctor4t.wathe.api.MapEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public class GenericMapEffect extends MapEffect {
    public GenericMapEffect(Identifier identifier) {
        super(identifier);
    }

    @Override
    public void initializeMapEffects(ServerWorld serverWorld, List<ServerPlayerEntity> players) {
    }

    @Override
    public void finalizeMapEffects(ServerWorld serverWorld, List<ServerPlayerEntity> players) {
    }
}
