package dev.doctor4t.wathe.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.api.GameMode;
import dev.doctor4t.wathe.api.MapEffect;
import dev.doctor4t.wathe.api.WatheGameModes;
import dev.doctor4t.wathe.api.WatheMapEffects;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.command.argument.GameModeArgumentType;
import dev.doctor4t.wathe.command.argument.MapEffectArgumentType;
import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.game.GameFunctions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class StartCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("wathe:start")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("gameMode", GameModeArgumentType.gameMode())
                                .then(CommandManager.argument("mapEffect", MapEffectArgumentType.mapEffect())
                                        .then(CommandManager.argument("startTimeInMinutes", IntegerArgumentType.integer(1))
                                                .executes(context -> execute(context.getSource(), GameModeArgumentType.getGameModeArgument(context, "gameMode"), MapEffectArgumentType.getMapEffectArgument(context, "mapEffect"), IntegerArgumentType.getInteger(context, "startTimeInMinutes")))
                                        )
                                        .executes(context -> execute(
                                                        context.getSource(),
                                                        GameModeArgumentType.getGameModeArgument(context, "gameMode"),
                                                        MapEffectArgumentType.getMapEffectArgument(context, "mapEffect"),
                                                        -1
                                                )
                                        )
                                )
                        )
        );
    }

    private static int execute(ServerCommandSource source, GameMode gameMode, MapEffect mapEffect, int minutes) {
        if (GameWorldComponent.KEY.get(source.getWorld()).isRunning()) {
            source.sendError(Text.translatable("game.start_error.game_running"));
            return -1;
        }
        if (gameMode == WatheGameModes.LOOSE_ENDS || gameMode == WatheGameModes.DISCOVERY || mapEffect == WatheMapEffects.HARPY_EXPRESS_SUNDOWN || mapEffect == WatheMapEffects.HARPY_EXPRESS_DAY) {
            return Wathe.executeSupporterCommand(source, () -> GameFunctions.startGame(source.getWorld(), gameMode, mapEffect, GameConstants.getInTicks(minutes >= 0 ? minutes : gameMode.defaultStartTime, 0)));
        } else  {
            GameFunctions.startGame(source.getWorld(), gameMode, mapEffect, GameConstants.getInTicks(minutes >= 0 ? minutes : gameMode.defaultStartTime, 0));
            return 1;
        }
    }
}
