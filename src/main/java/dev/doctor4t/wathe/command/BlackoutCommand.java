package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.doctor4t.trainmurdermystery.cca.WorldBlackoutComponent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.NotNull;

public class BlackoutCommand {
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("tmm:blackout")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.argument("size", IntegerArgumentType.integer(3))
                                .executes(context -> {
                                    int size = IntegerArgumentType.getInteger(context, "size");
                                    WorldBlackoutComponent.KEY.get(context.getSource().getWorld()).triggerBlackout(Box.of(context.getSource().getPosition(), size, size, size));
                                    return 1;
                                }))
        );
    }
}
