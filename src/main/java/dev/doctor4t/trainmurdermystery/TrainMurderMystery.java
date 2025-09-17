package dev.doctor4t.trainmurdermystery;

import dev.doctor4t.trainmurdermystery.command.GiveRoomKeyCommand;
import dev.doctor4t.trainmurdermystery.command.SetTrainSpeedCommand;
import dev.doctor4t.trainmurdermystery.command.StartGameCommand;
import dev.doctor4t.trainmurdermystery.game.GameLoop;
import dev.doctor4t.trainmurdermystery.index.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrainMurderMystery implements ModInitializer {
    public static final String MOD_ID = "trainmurdermystery";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }

    @Override
    public void onInitialize() {
        // Registry initializers
        TMMDataComponentTypes.initialize();
        TrainMurderMysterySounds.initialize();
        TrainMurderMysteryEntities.initialize();
        TrainMurderMysteryBlocks.initialize();
        TrainMurderMysteryItems.initialize();
        TrainMurderMysteryBlockEntities.initialize();
        TrainMurderMysteryParticles.initialize();

        // Register commands
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            GiveRoomKeyCommand.register(dispatcher);
            SetTrainSpeedCommand.register(dispatcher);
            StartGameCommand.register(dispatcher);
        }));

        // Game loop tick
        ServerTickEvents.START_WORLD_TICK.register(GameLoop::tick);
    }

    public static boolean isPlayerAliveAndSurvival(PlayerEntity player) {
        return player != null && !player.isSpectator() && !player.isCreative();
    }

}

// TODO: Add train doors that can't be blasted
// TODO: Add tasks
// TODO: Nicer starting phase + UI
// TODO: System that remembers previous roles and allows cycling of roles
// TODO: Map reset system
// TODO: Add wheels and tracks
// TODO: Add lobby