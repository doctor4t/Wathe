package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.GameMode;
import dev.doctor4t.trainmurdermystery.api.TMMGameModes;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.command.argument.GameModeArgumentType;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class StartCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("tmm:start")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("gameMode", GameModeArgumentType.gameMode())
                                .then(CommandManager.argument("startTimeInMinutes", IntegerArgumentType.integer(1))
                                        .executes(context -> execute(context.getSource(), GameModeArgumentType.getGameModeArgument(context, "gameMode"), IntegerArgumentType.getInteger(context, "startTimeInMinutes")))
                                )
                                .executes(context -> {
                                    GameMode gameMode = GameModeArgumentType.getGameModeArgument(context, "gameMode");
                                    return execute(context.getSource(), gameMode, -1);
                                        }
                                )
                        )
        );
    }

    private static int execute(ServerCommandSource source, GameMode gameMode, int minutes) {
        if (GameWorldComponent.KEY.get(source.getWorld()).isRunning()) {
            source.sendError(Text.translatable("game.start_error.game_running"));
            return -1;
        }
        if (gameMode == TMMGameModes.LOOSE_ENDS || gameMode == TMMGameModes.DISCOVERY) {
            return TMM.executeSupporterCommand(source, () -> GameFunctions.startGame(source.getWorld(), gameMode, GameConstants.getInTicks(minutes >= 0 ? minutes : gameMode.defaultStartTime, 0)));
        } else  {
            GameFunctions.startGame(source.getWorld(), gameMode, GameConstants.getInTicks(minutes >= 0 ? minutes : gameMode.defaultStartTime, 0));
            return 1;
        }
    }
}
