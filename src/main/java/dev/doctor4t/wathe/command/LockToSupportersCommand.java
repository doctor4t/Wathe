package dev.doctor4t.wathe.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.UUID;

public class LockToSupportersCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("wathe:lockToSupporters")
                .requires(source -> source.getPlayer() != null && source.getPlayer().getUuid().equals(UUID.fromString("1b44461a-f605-4b29-a7a9-04e649d1981c")))
                .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                        .executes(context -> execute(context.getSource(), BoolArgumentType.getBool(context, "enabled"))))
        );
    }

    private static int execute(ServerCommandSource source, boolean value) {
        GameWorldComponent.KEY.get(source.getWorld()).setLockedToSupporters(value);
        return 1;
    }

}
