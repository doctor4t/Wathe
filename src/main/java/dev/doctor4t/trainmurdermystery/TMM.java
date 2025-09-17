package dev.doctor4t.trainmurdermystery;

import dev.doctor4t.trainmurdermystery.command.GiveRoomKeyCommand;
import dev.doctor4t.trainmurdermystery.command.SetTrainSpeedCommand;
import dev.doctor4t.trainmurdermystery.command.StartGameCommand;
import dev.doctor4t.trainmurdermystery.game.TMMGameLoop;
import dev.doctor4t.trainmurdermystery.index.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TMM implements ModInitializer {
    public static final String MOD_ID = "trainmurdermystery";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String name) {
        return Identifier.of(MOD_ID, name);
    }

    @Override
    public void onInitialize() {
        // Registry initializers
        TMMDataComponentTypes.initialize();
        TMMSounds.initialize();
        TMMEntities.initialize();
        TMMBlocks.initialize();
        TMMItems.initialize();
        TMMBlockEntities.initialize();
        TMMParticles.initialize();

        // Register commands
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            GiveRoomKeyCommand.register(dispatcher);
            SetTrainSpeedCommand.register(dispatcher);
            StartGameCommand.register(dispatcher);
        }));

        // Game loop tick
        ServerTickEvents.START_WORLD_TICK.register(TMMGameLoop::tick);
    }

}

// TODO: Add tasks
// TODO: Add wheels and tracks
// TODO: Map reset system
// TODO: Add lobby
// TODO: Nicer starting phase + UI
// TODO: System that remembers previous roles and allows cycling of roles
// TODO: Limit spectators to a radius around the train