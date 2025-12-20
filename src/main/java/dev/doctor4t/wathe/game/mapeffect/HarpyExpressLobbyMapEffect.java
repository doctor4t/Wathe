package dev.doctor4t.wathe.game.mapeffect;

import dev.doctor4t.wathe.api.MapEffect;
import dev.doctor4t.wathe.cca.TrainWorldComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public class HarpyExpressLobbyMapEffect extends MapEffect {
    public HarpyExpressLobbyMapEffect(Identifier identifier) {
        super(identifier);
    }

    @Override
    public void initializeMapEffects(ServerWorld serverWorld, List<ServerPlayerEntity> players) {
        TrainWorldComponent trainWorldComponent = TrainWorldComponent.KEY.get(serverWorld);
        trainWorldComponent.setSnow(false);
        trainWorldComponent.setFog(true);
        trainWorldComponent.setHud(true);
        trainWorldComponent.setSpeed(0);
        trainWorldComponent.setTime(0);
    }

    @Override
    public void finalizeMapEffects(ServerWorld serverWorld, List<ServerPlayerEntity> players) {

    }
}


