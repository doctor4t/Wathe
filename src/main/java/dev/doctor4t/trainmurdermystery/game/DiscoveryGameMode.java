package dev.doctor4t.trainmurdermystery.game;

import dev.doctor4t.trainmurdermystery.api.GameMode;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.cca.GameRoundEndComponent;
import dev.doctor4t.trainmurdermystery.cca.GameTimeComponent;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.TrainWorldComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public class DiscoveryGameMode extends GameMode {
    public DiscoveryGameMode(Identifier identifier) {
        super(identifier, 10, 1);
    }

    @Override
    public void initializeGame(ServerWorld serverWorld, GameWorldComponent gameWorldComponent, List<ServerPlayerEntity> players) {
        TrainWorldComponent.KEY.get(serverWorld).setTimeOfDay(TrainWorldComponent.TimeOfDay.DAY);

        for (ServerPlayerEntity player : players) {
            gameWorldComponent.addRole(player, TMMRoles.DISCOVERY_CIVILIAN);
        }
    }

    @Override
    public void tickServerGameLoop(ServerWorld serverWorld, GameWorldComponent gameWorldComponent) {
        // stop game if ran out of time
        if (!GameTimeComponent.KEY.get(serverWorld).hasTime())
            GameFunctions.stopGame(serverWorld);
    }
}
