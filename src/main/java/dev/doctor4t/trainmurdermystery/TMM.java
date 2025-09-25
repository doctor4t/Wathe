package dev.doctor4t.trainmurdermystery;

import com.google.common.reflect.Reflection;
import dev.doctor4t.trainmurdermystery.command.*;
import dev.doctor4t.trainmurdermystery.index.*;
import dev.doctor4t.trainmurdermystery.util.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
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
        Reflection.initialize(TMMDataComponentTypes.class);
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
            StartCommand.register(dispatcher);
            StopCommand.register(dispatcher);
            TestStartGameCommand.register(dispatcher);
            ForceStop.register(dispatcher);
        }));

        PayloadTypeRegistry.playS2C().register(ShootMuzzleS2CPayload.ID, ShootMuzzleS2CPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(PoisonUtils.PoisonOverlayPayload.ID, PoisonUtils.PoisonOverlayPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(GunDropPayload.ID, GunDropPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(KnifeStabPayload.ID, KnifeStabPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(GunShootPayload.ID, GunShootPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(StoreBuyPayload.ID, StoreBuyPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(KnifeStabPayload.ID, new KnifeStabPayload.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(GunShootPayload.ID, new GunShootPayload.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(StoreBuyPayload.ID, new StoreBuyPayload.Receiver());

        Scheduler.init();
    }

    public static boolean isSkyVisibleAdjacent(@NotNull Entity player) {
        var mutable = new BlockPos.Mutable();
        var playerPos = BlockPos.ofFloored(player.getEyePos());
        for (var x = -1; x <= 1; x+=2) {
            for (var z = -1; z <= 1; z+=2) {
                mutable.set(playerPos.getX() + x, playerPos.getY(), playerPos.getZ() + z);
                if (player.getWorld().isSkyVisible(mutable)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isExposedToWind(@NotNull Entity player) {
        var mutable = new BlockPos.Mutable();
        var playerPos = BlockPos.ofFloored(player.getEyePos());
        for (var x = 0; x <= 10; x++) {
            mutable.set(playerPos.getX() - x, player.getEyePos().getY(), playerPos.getZ());
            if (!player.getWorld().isSkyVisible(mutable)) {
                return false;
            }
        }
        return true;
    }
}

// TORECORD: Fixing the mood system
// TODO: Better tasks: mood goes down gradually, completing tasks is a single action to bring it back up
//  (new task system is more meant to make players vulnerable to the hitman in a different way from splitting them up)
// TODO: - Get a snack from restaurant task (food platter block + food items)
// TODO: - Get a drink from the bar task (drink tray block + custom drink items)
// TODO: - Sleeping task requiring you to sleep for 8s
// TODO: - Get some fresh air reduced to going walking outside for 8s
// TODO: - Change mood down effect from speed to: (to prevent players being able to innocent each other on an easily observable change)
// TODO:    - Not seeing bodies at mid mood + a little more shake
// TODO:    - Randomly seeing players as psycho + even more shake

// TORECORD: Fixing the hitman
// TORECORD: Remove target system and make the win condition a kill count, turning him into a psycho
// TORECORD: New name display system to allow anyone to know player's names, displays "psycho cohort" for other psychos, and instinct now shows other psychos instead of targets, game recognizes game
// TORECORD: Hitman item shop
// TORECORD: Fixing the knife (now with a kill indicator)
// TORECORD: New name system shows who else is the hitman allowing them to scheme together
// TODO: - Explosive for clumped up people (foils the grouping up cheese)
// TORECORD: - Poison (poisons the next food or drink item)
// TORECORD: - Scorpion (poisons the next person sleeping in the bed)
// TORECORD: Getting food poisoned and then scorpion poisoned lowers the timer
// TODO: - Gun with one bullet (allows the hitman to potentially pass as a detective / passenger with a gun on top of giving a ranged option)
// TORECORD: - Crowbar (perma opening a door should be a hitman ability, allows for creative kills where you can push off players from train doors, as well as allowing passengers to use the exterior in order to give plausible deniability to hitmen using it to relocate)
// TORECORD: - Psycho mode (wanted to have an anonymous killer originally for the horror element, this also allows the hitman to go crazy how some wanted to)
//              Ambience can be heard by all players, so you know when to run. Also gives a use to rooms as you can hide in them
// TORECORD: - Light turn off item + true darkness (increases the horror aspect + amazing scenario of lights turning off and someone being dead when they turn back on + blackout and psycho mode)
//              True darkness doesn't work that well because of skylight seeping into the block, add a light barrier block that blocks it from entering
// TODO: - Firecracker (luring people, shooting the gun in spectator often led to people rushing in from curiosity, allowing the hitman to manipulate players)
// TODO: - Note (allows the hitman to leave messages, fun for encouraging the roleplay aspect)

// TORECORD: Fixing the detective
// TORECORD: Remove revolver bullet count but make detectives drop the gun on innocent kill (to prevent detectives gunning down people and giving more weight to the choice as well as offer a chance to other players to make decisions, also sets mood to 0 as extra punishment)
// TORECORD: Make the detective drop the gun on killed (that the hitman cannot pick up, to prevent soft locking)
// done: Remove body bags so make player corpses turn into skeletons after some time (since the detective role is no longer really a role and depends on who carries the gun, it's hard to keep the body bag item)

// TORECORD: Fixing the map
// TODO: Rearrange the train cars to prevent all POIs being separated by all the sleeping cars

// TORECORD: Polish
// TODO: Train chimney smoke + ringable horn, triggers game start in lobby and end of game
// TORECORD: Players collide with each other (Amy)
// TODO: Louder footsteps
// TORECORD: System that remembers previous roles and allows cycling of roles (Amy)