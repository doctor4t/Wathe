package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import dev.doctor4t.trainmurdermystery.cca.ScoreboardRoleSelectorComponent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

public class ResetWeightsCommand {
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tmm:resetWeights").requires(source -> source.hasPermissionLevel(2)).executes(context -> {
            ScoreboardRoleSelectorComponent scoreboardRoleSelectorComponent = ScoreboardRoleSelectorComponent.KEY.get(context.getSource().getServer().getScoreboard());
            scoreboardRoleSelectorComponent.reset();
            return 1;
        }));
    }
}
