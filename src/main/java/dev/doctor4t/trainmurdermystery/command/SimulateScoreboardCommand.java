package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.doctor4t.trainmurdermystery.cca.ScoreboardRoleSelectorComponent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

public class SimulateScoreboardCommand {
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("tmm:simulateScoreboard")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("rounds", IntegerArgumentType.integer(1))
                                .executes(context -> {
                                    ScoreboardRoleSelectorComponent.KEY.get(context.getSource().getWorld().getScoreboard())
                                            .simulateScoreboard(context.getSource(), IntegerArgumentType.getInteger(context, "rounds"));
                                    return 1;
                                })
                        )
        );
    }
}