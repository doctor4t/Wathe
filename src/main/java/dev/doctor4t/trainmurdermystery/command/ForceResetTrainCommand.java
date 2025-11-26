package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ForceResetTrainCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tmm:forcereset")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    if (!source.getWorld().equals(source.getServer().getOverworld())) {
                        source.sendError(Text.translatable("commands.trainmurdermystery.forcereset.error.dimension"));
                        return 0;
                    }

                    boolean retryNeeded = GameFunctions.tryResetTrain(source.getWorld());

                    if (retryNeeded) {
                        source.sendFeedback(() -> Text.translatable("commands.trainmurdermystery.forcereset.chunks_loading"), false);
                        return 0;
                    } else {
                        source.sendFeedback(() -> Text.translatable("commands.trainmurdermystery.forcereset.success"), true);
                        return 1;
                    }
                })
        );
    }
}