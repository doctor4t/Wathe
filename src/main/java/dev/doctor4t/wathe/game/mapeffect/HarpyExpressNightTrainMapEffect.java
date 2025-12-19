package dev.doctor4t.wathe.game.mapeffect;

import dev.doctor4t.wathe.cca.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public class HarpyExpressNightTrainMapEffect extends HarpyExpressTrainMapEffect {
    public HarpyExpressNightTrainMapEffect(Identifier identifier) {
        super(identifier);
    }

    @Override
    public void initializeMapEffects(ServerWorld serverWorld, List<ServerPlayerEntity> players) {
        super.initializeMapEffects(serverWorld, players);
        TrainWorldComponent.KEY.get(serverWorld).setTimeOfDay(TrainWorldComponent.TimeOfDay.NIGHT);
    }
}
