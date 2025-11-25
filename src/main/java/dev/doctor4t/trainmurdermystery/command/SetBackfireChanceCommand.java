package dev.doctor4t.trainmurdermystery.command;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerShopComponent;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;

public class SetBackfireChanceCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("tmm:setBackfireChance")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(
                                CommandManager.argument("chance", FloatArgumentType.floatArg(0f, 1f))
                                        .executes(context -> execute(context.getSource(), FloatArgumentType.getFloat(context, "chance")))
                        )
        );
    }

    private static int execute(ServerCommandSource source, float chance) {
        return TMM.executeSupporterCommand(source,
                () -> GameWorldComponent.KEY.get(source.getWorld()).setBackfireChance(chance)
        );
    }

}
