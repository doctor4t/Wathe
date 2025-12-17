package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.cca.AreasWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.TrainWorldComponent;
import dev.doctor4t.trainmurdermystery.command.argument.TimeOfDayArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SetConfigCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tmm:setVisual")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("playArea")
                        .executes(context -> print(context.getSource(), AreasWorldComponent.KEY.get(context.getSource().getWorld()).getPlayArea()))
                        .then(CommandManager.argument("bool", BoolArgumentType.bool())
                                .executes(context -> execute(context.getSource(), AreasWorldComponent::setPlayArea, BoolArgumentType.getBool(context, "enabled")))))
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

    private static <T> int executeArea(ServerCommandSource source, BiConsumer<AreasWorldComponent, T> consumer, T value) {
        consumer.accept(AreasWorldComponent.KEY.get(source.getWorld()), value);
        return 1;
    }

    private static <T> int execute(ServerCommandSource source, BiConsumer<GameWorldComponent, T> consumer, T value) {
        consumer.accept(GameWorldComponent.KEY.get(source.getWorld()), value);
        return 1;
    }

    private static <T> int print(ServerCommandSource source, T value) {
        source.sendMessage(Text.literal(value.toString()));
        return 1;
    }
}
