package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.cca.TrainWorldComponent;
import dev.doctor4t.trainmurdermystery.command.argument.TimeOfDayArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.function.BiConsumer;

public class SetVisualCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tmm:setVisual")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("snow")
                        .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                                .executes(context -> execute(context.getSource(), TrainWorldComponent::setSnow, BoolArgumentType.getBool(context, "enabled")))))
                .then(CommandManager.literal("fog")
                        .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                                .executes(context -> execute(context.getSource(), TrainWorldComponent::setFog, BoolArgumentType.getBool(context, "enabled")))))
                .then(CommandManager.literal("hud")
                        .then(CommandManager.argument("enabled", BoolArgumentType.bool())
                                .executes(context -> execute(context.getSource(), TrainWorldComponent::setHud, BoolArgumentType.getBool(context, "enabled")))))
                .then(CommandManager.literal("trainSpeed")
                        .then(CommandManager.argument("speed", IntegerArgumentType.integer(0))
                                .executes(context -> execute(context.getSource(), TrainWorldComponent::setSpeed, IntegerArgumentType.getInteger(context, "speed")))))
                .then(CommandManager.literal("time")
                        .then(CommandManager.argument("timeOfDay", TimeOfDayArgumentType.timeofday())
                                .executes(context -> execute(context.getSource(), TrainWorldComponent::setTimeOfDay, TimeOfDayArgumentType.getTimeofday(context, "timeOfDay")))))
                .then(CommandManager.literal("reset")
                        .executes(context -> reset(context.getSource())))
        );
    }

    private static int reset(ServerCommandSource source) {
        TrainWorldComponent trainWorldComponent = TrainWorldComponent.KEY.get(source.getWorld());
        trainWorldComponent.reset();
        return 1;
    }

    private static <T> int execute(ServerCommandSource source, BiConsumer<TrainWorldComponent, T> consumer, T value) {
        return TMM.executeSupporterCommand(source,
                () -> consumer.accept(TrainWorldComponent.KEY.get(source.getWorld()), value)
        );
    }

}
