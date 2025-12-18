package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.doctor4t.trainmurdermystery.cca.MapVariablesWorldComponent;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.command.argument.RotationArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SetMapVariableCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("tmm:setMapVariable")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("help")
                                .executes(
                                        context -> sendHelp(
                                                context.getSource()
                                        )
                                )
                        )
                        .then(CommandManager.literal("train")
                                .then(CommandManager.argument("boolean", BoolArgumentType.bool())
                                        .executes(
                                                context -> setValue(
                                                        BoolArgumentType.getBool(context, "boolean"),
                                                        bool -> getMapVarsComponent(context).setTrain(bool)
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("spawnPosition")
                                .then(CommandManager.argument("location", Vec3ArgumentType.vec3())
                                        .then(CommandManager.argument("rotation", RotationArgumentType.rotation())
                                                .executes(
                                                        context -> setPosWithOrientation(
                                                                context.getSource(),
                                                                Vec3ArgumentType.getPosArgument(context, "location"),
                                                                RotationArgumentType.getRotation(context, "rotation"),
                                                                posWithOrientation -> getMapVarsComponent(context).setSpawnPos(posWithOrientation)
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("spectatorSpawnPosition")
                                .then(CommandManager.argument("location", Vec3ArgumentType.vec3())
                                        .then(CommandManager.argument("rotation", RotationArgumentType.rotation())
                                                .executes(
                                                        context -> setPosWithOrientation(
                                                                context.getSource(),
                                                                Vec3ArgumentType.getPosArgument(context, "location"),
                                                                RotationArgumentType.getRotation(context, "rotation"),
                                                                posWithOrientation -> getMapVarsComponent(context).setSpectatorSpawnPos(posWithOrientation)
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("readyArea")
                                .then(CommandManager.argument("from", Vec3ArgumentType.vec3())
                                        .then(CommandManager.argument("to", Vec3ArgumentType.vec3())
                                                .executes(
                                                        context -> setBox(
                                                                context.getSource(),
                                                                Vec3ArgumentType.getPosArgument(context, "from"),
                                                                Vec3ArgumentType.getPosArgument(context, "to"),
                                                                box -> getMapVarsComponent(context).setReadyArea(box)
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("playAreaOffset")
                                .then(CommandManager.argument("offset", Vec3ArgumentType.vec3(true))
                                        .executes(
                                                context -> setValue(
                                                        BlockPos.ofFloored(Vec3ArgumentType.getVec3(context, "offset")),
                                                        blockPos -> getMapVarsComponent(context).setPlayAreaOffset(blockPos)
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("playArea")
                                .then(CommandManager.argument("from", Vec3ArgumentType.vec3())
                                        .then(CommandManager.argument("to", Vec3ArgumentType.vec3())
                                                .executes(
                                                        context -> setBox(
                                                                context.getSource(),
                                                                Vec3ArgumentType.getPosArgument(context, "from"),
                                                                Vec3ArgumentType.getPosArgument(context, "to"),
                                                                box -> getMapVarsComponent(context).setPlayArea(box)
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("resetTemplateArea")
                                .then(CommandManager.argument("from", Vec3ArgumentType.vec3())
                                        .then(CommandManager.argument("to", Vec3ArgumentType.vec3())
                                                .executes(
                                                        context -> setBox(
                                                                context.getSource(),
                                                                Vec3ArgumentType.getPosArgument(context, "from"),
                                                                Vec3ArgumentType.getPosArgument(context, "to"),
                                                                box -> getMapVarsComponent(context).setResetTemplateArea(box)
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(CommandManager.literal("resetPasteOffset")
                                .then(CommandManager.argument("offset", Vec3ArgumentType.vec3(true))
                                        .executes(
                                                context -> setValue(
                                                        BlockPos.ofFloored(Vec3ArgumentType.getVec3(context, "offset")),
                                                        blockPos -> getMapVarsComponent(context).setResetPasteOffset(blockPos)
                                                )
                                        )
                                )
                        )
        );
    }

    private static @NotNull MapVariablesWorldComponent getMapVarsComponent(CommandContext<ServerCommandSource> context) {
        return MapVariablesWorldComponent.KEY.get(context.getSource().getWorld());
    }

    private static int sendHelp(ServerCommandSource source) {
        source.sendMessage(Text.translatable("tmm.help.map_variables"));
        return 1;
    }

    private static <T> int setValue(T value, Consumer<T> consumer) {
        consumer.accept(value);
        return 1;
    }

    private static int setPosWithOrientation(ServerCommandSource source, PosArgument location, PosArgument rotation, Consumer<MapVariablesWorldComponent.PosWithOrientation> consumer) {
        Vec3d absolutePos = location.toAbsolutePos(source);
        Vec2f absoluteRotation = rotation.toAbsoluteRotation(source);
        setValue(new MapVariablesWorldComponent.PosWithOrientation(absolutePos, absoluteRotation.y, absoluteRotation.x), consumer);
        return 1;
    }

    private static int setBox(ServerCommandSource source, PosArgument from, PosArgument to, Consumer<Box> consumer) {
        setValue(new Box(from.toAbsolutePos(source), to.toAbsolutePos(source)), consumer);
        return 1;
    }

}
