package dev.doctor4t.wathe.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.cca.GameTimeComponent;
import dev.doctor4t.wathe.game.GameConstants;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class SetTimerCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("wathe:setTimer")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(
                                CommandManager.argument("minutes", IntegerArgumentType.integer(0, 240))
                                        .then(
                                                CommandManager.argument("seconds", IntegerArgumentType.integer(0, 59))
                                                        .executes(context -> setTimer(context.getSource(), IntegerArgumentType.getInteger(context, "minutes"), IntegerArgumentType.getInteger(context, "seconds")))
                                        )
                        )
        );
    }

    private static int setTimer(ServerCommandSource source, int minutes, int seconds) {
        return Wathe.executeSupporterCommand(source,
                () -> {
                    GameTimeComponent.KEY.get(source.getWorld()).setTime(GameConstants.getInTicks(minutes, seconds));
                }
        );
    }
}
