package dev.doctor4t.trainmurdermystery.game;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import dev.doctor4t.trainmurdermystery.util.Carriage;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface TMMGameConstants {
    // Logistics
    int FADE_TIME = 40;
    int FADE_PAUSE = 20;

    // Blocks
    int DOOR_AUTOCLOSE_TIME = getInTicks(0, 5);

    // Items
    int KNIFE_COOLDOWN = getInTicks(1, 0);
    int JAMMED_DOOR_TIME = getInTicks(1, 0);
    int LOCKPICK_JAM_COOLDOWN = getInTicks(2, 0);

    // Sprint
    int MAX_SPRINTING_TICKS = getInTicks(0, 10);

    // Game areas
    Box READY_AREA = new Box(-981, -1, -364, -813, 3, -358);
    BlockPos PLAY_POS = new BlockPos(-19, 122, -539);
    Consumer<ServerPlayerEntity> SPECTATOR_TP = serverPlayerEntity -> serverPlayerEntity.teleport(serverPlayerEntity.getServerWorld(), -68 ,133, -535.5, -90, 15);

    Box PLAY_AREA = new Box(-140, 118, -535.5f - 15, 230, 200, -535.5f + 15);
    Box BACKUP_TRAIN_LOCATION = new Box(-57, 64, -531, 177, 74, -540);
    Box TRAIN_LOCATION = BACKUP_TRAIN_LOCATION.offset(0, 55, 0);

    // Task Variables
    List<Carriage> CARRIAGES = new ArrayList<>(List.of(
            new Carriage(List.of(
                    new Box(-17, 121, -533, 3, 125, -539),
                    new Box(31, 121, -533, 51, 125, -539)
            ), "Restaurant"),
            new Carriage(List.of(
                    new Box(7, 121, -539, 27, 125, -533)
            ), "Bar"),
            new Carriage(List.of(
                    new Box(127, 121, -539, 147, 125, -533)
            ), "Library")
    ));
    float MOOD_DRAIN = 1f / getInTicks(1, 0);
    float MOOD_GAIN = 1f / getInTicks(0, 15);
    int MIN_PREFERENCE_COOLDOWN = getInTicks(0, 30);
    int MAX_PREFERENCE_COOLDOWN = getInTicks(0, 30);
    int TIME_TO_FIRST_TASK = getInTicks(0, 10);

    static int getInTicks(int minutes, int seconds) {
        return (minutes * 60 + seconds) * 20;
    }
}