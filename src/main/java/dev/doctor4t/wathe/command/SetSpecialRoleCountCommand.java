package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class SetSpecialRoleCountCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("tmm:setSpecialRoleCount")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(
                                CommandManager.argument("count", DoubleArgumentType.doubleArg(0))
                                        .executes(context -> exec(context.getSource(), DoubleArgumentType.getDouble(context, "count")))
                        )
        );
    }

    private static int exec(ServerCommandSource source, double count) {
        GameWorldComponent.KEY.get(source.getWorld()).setSpecialRoleCount(count);
        source.sendFeedback(() -> Text.literal("Set special role count to " + count), false);
        return 1;
    }
}
