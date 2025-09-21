package dev.doctor4t.trainmurdermystery;

import dev.doctor4t.trainmurdermystery.command.*;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import dev.doctor4t.trainmurdermystery.index.*;
import dev.doctor4t.trainmurdermystery.util.KnifeStabPayload;
import dev.doctor4t.trainmurdermystery.util.ShootMuzzleS2CPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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
            StopGameCommand.register(dispatcher);
            ResetTrainCommand.register(dispatcher);
        }));

        PayloadTypeRegistry.playS2C().register(ShootMuzzleS2CPayload.ID, ShootMuzzleS2CPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(KnifeStabPayload.ID, KnifeStabPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(KnifeStabPayload.ID, new KnifeStabPayload.Receiver());
    }
}

// TODO: Fix sounds for showers and lights
// TODO: System that remembers previous roles and allows cycling of roles
// TODO: Train chimney smoke + ringable horn, triggers game start in lobby
// TODO: Fix spectators being shot by guns
// TODO: Better tasks: mood goes down gradually, completing tasks is a single action to bring it back up
// TODO: - Get a snack from restaurant task (need food platter block + custom food item)
// TODO: - Get a drink from the bar task (need drinks platter block + custom drink item, whiskey glass?)
// TODO: - Up sleep chances and make it a sleep 10s task
// TODO: - Get some fresh air reduced to going walking outside for 10s
// TODO: Remove target system and make the win condition a kill count
// TODO: Cabin button from inside
// TODO: Remove revolver bullet count
// TODO: Detective drops gun on innocent kill
// TODO: Make the detective drop the gun on killed
// TODO: Players collide with each other
// TODO: Louder footsteps
// TODO: Barrier panels for lobby
// TODO: Hitman item shop
// TODO: - Explosive for clumped up people
// TODO: - Poison
// TODO: - Scorpion
// TODO: - Gun with one bullet
// TODO: - Psycho mode
// TODO: - Light turn off item + true darkness
// TODO: - Crowbar
// TODO: - Firecracker