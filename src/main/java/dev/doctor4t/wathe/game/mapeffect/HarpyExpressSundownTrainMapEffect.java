package dev.doctor4t.wathe.game.mapeffect;

import dev.doctor4t.wathe.cca.TrainWorldComponent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public class HarpyExpressSundownTrainMapEffect extends HarpyExpressTrainMapEffect {
    public HarpyExpressSundownTrainMapEffect(Identifier identifier) {
        super(identifier);
    }

    @Override
    public void initializeMapEffects(ServerWorld serverWorld, List<ServerPlayerEntity> players) {
        super.initializeMapEffects(serverWorld, players);
        TrainWorldComponent.KEY.get(serverWorld).setTimeOfDay(TrainWorldComponent.TimeOfDay.SUNDOWN);
    }
}
