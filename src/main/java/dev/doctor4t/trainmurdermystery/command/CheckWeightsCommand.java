package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import dev.doctor4t.trainmurdermystery.cca.ScoreboardRoleSelectorComponent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

public class CheckWeightsCommand {
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("tmm:checkWeights")
                        .requires(source -> source.hasPermissionLevel(2))
                        .executes(context -> {
                            ScoreboardRoleSelectorComponent.KEY.get(context.getSource().getWorld().getScoreboard()).checkWeights(context.getSource());
                            return 1;
                        })
        );
    }
}
