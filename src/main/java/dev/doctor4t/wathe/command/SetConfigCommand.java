package dev.doctor4t.wathe.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.RotationArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SetConfigCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
//        dispatcher.register(CommandManager.literal("tmm:setConfig")
//                .requires(source -> source.hasPermissionLevel(2))
//                .then(CommandManager.literal("readyArea")
//                        .executes(context -> printArea(context.getSource(), AreasWorldComponent::getReadyArea))
//                        .then(CommandManager.argument("1", Vec3ArgumentType.vec3())
//                                .then(CommandManager.argument("2", Vec3ArgumentType.vec3())
//                                        .executes(context -> executeArea(context.getSource(), AreasWorldComponent::setReadyArea, Vec3ArgumentType.getVec3(context, "1"), Vec3ArgumentType.getVec3(context, "2"))))))
//                .then(CommandManager.literal("playArea")
//                        .executes(context -> printArea(context.getSource(), AreasWorldComponent::getPlayArea))
//                        .then(CommandManager.argument("1", Vec3ArgumentType.vec3())
//                                .then(CommandManager.argument("2", Vec3ArgumentType.vec3())
//                                        .executes(context -> executeArea(context.getSource(), AreasWorldComponent::setPlayArea, Vec3ArgumentType.getVec3(context, "1"), Vec3ArgumentType.getVec3(context, "2"))))))
//                .then(CommandManager.literal("particleColliderArea")
//                        .executes(context -> printArea(context.getSource(), AreasWorldComponent::getParticleColliderArea))
//                        .then(CommandManager.argument("1", Vec3ArgumentType.vec3())
//                                .then(CommandManager.argument("2", Vec3ArgumentType.vec3())
//                                        .executes(context -> executeArea(context.getSource(), AreasWorldComponent::setParticleColliderArea, Vec3ArgumentType.getVec3(context, "1"), Vec3ArgumentType.getVec3(context, "2"))))))
//                .then(CommandManager.literal("resetTemplateArea")
//                        .executes(context -> printArea(context.getSource(), AreasWorldComponent::getResetTemplateArea))
//                        .then(CommandManager.argument("1", BlockPosArgumentType.blockPos())
//                                .then(CommandManager.argument("2", Vec3ArgumentType.vec3())
//                                        .executes(context -> executeArea(context.getSource(), AreasWorldComponent::setResetTemplateArea, Vec3d.of(BlockPosArgumentType.getBlockPos(context, "1")), Vec3d.of(BlockPosArgumentType.getBlockPos(context, "2")))))))
//                .then(CommandManager.literal("resetPasteArea")
//                        .executes(context -> printArea(context.getSource(), AreasWorldComponent::getResetPasteArea))
//                        .then(CommandManager.argument("1", BlockPosArgumentType.blockPos())
//                                .then(CommandManager.argument("2", BlockPosArgumentType.blockPos())
//                                        .executes(context -> executeArea(context.getSource(), AreasWorldComponent::setResetPasteArea, Vec3d.of(BlockPosArgumentType.getBlockPos(context, "1")), Vec3d.of(BlockPosArgumentType.getBlockPos(context, "2")))))))
//                .then(CommandManager.literal("playAreaOffset")
//                        .executes(context -> printArea(context.getSource(), AreasWorldComponent::getPlayAreaOffset))
//                        .then(CommandManager.argument("1", Vec3ArgumentType.vec3())
//                                .executes(context -> executeArea(context.getSource(), AreasWorldComponent::setPlayAreaOffset, Vec3ArgumentType.getVec3(context, "1")))))
//                .then(CommandManager.literal("lobbySpawnPos")
//                        .executes(context -> printArea(context.getSource(), AreasWorldComponent::getSpawnPos))
//                        .then(CommandManager.argument("pos", Vec3ArgumentType.vec3())
//                                .then(CommandManager.argument("rotation", RotationArgumentType.rotation())
//                                        .executes(context -> {
//                                            Vec2f rotation = RotationArgumentType.getRotation(context, "rotation").toAbsoluteRotation(context.getSource());
//                                            return executeArea(context.getSource(), AreasWorldComponent::setSpawnPos, new AreasWorldComponent.PosWithOrientation(Vec3ArgumentType.getVec3(context, "pos"), rotation.x, rotation.y));
//                                        }))))
//                .then(CommandManager.literal("spectatorSpawnPos")
//                        .executes(context -> printArea(context.getSource(), AreasWorldComponent::getSpawnPos))
//                        .then(CommandManager.argument("pos", Vec3ArgumentType.vec3())
//                                .then(CommandManager.argument("rotation", RotationArgumentType.rotation())
//                                        .executes(context -> {
//                                            Vec2f rotation = RotationArgumentType.getRotation(context, "rotation").toAbsoluteRotation(context.getSource());
//                                            return executeArea(context.getSource(), AreasWorldComponent::setSpectatorSpawnPos, new AreasWorldComponent.PosWithOrientation(Vec3ArgumentType.getVec3(context, "pos"), rotation.x, rotation.y));
//                                        }))))
//                .then(CommandManager.literal("sync"))
//                .executes(ctx -> {
//                    GameWorldComponent.KEY.get(ctx.getSource().getWorld()).sync();
//                    AreasWorldComponent.KEY.get(ctx.getSource().getWorld()).sync();
//                    return 1;
//                })
//                .then(CommandManager.literal("maxRoomKeys")
//                        .executes(ctx -> print(ctx, "There is " + GameWorldComponent.KEY.get(ctx.getSource().getWorld()).getMaxRoomKey() + " unique room keys"))
//                        .then(CommandManager.argument("maxKeys", IntegerArgumentType.integer(1))
//                                .executes(ctx -> execute(ctx.getSource(), GameWorldComponent::setMaxRoomKey, IntegerArgumentType.getInteger(ctx, "maxKeys")))))
//                .then(CommandManager.literal("ambientBrightness")
//                        .executes(ctx -> print(ctx, "Brightness is " + GameWorldComponent.KEY.get(ctx.getSource().getWorld()).getAmbientBrightness()))
//                        .then(CommandManager.argument("bright", FloatArgumentType.floatArg(0))
//                                .executes(ctx -> execute(ctx.getSource(), GameWorldComponent::setAmbientBrightness, FloatArgumentType.getFloat(ctx, "bright")))))
//                .then(CommandManager.literal("tasks")
//                        .executes(ctx -> print(ctx, Arrays.stream(PlayerMoodComponent.Task.values()).map(it -> it.toString() + (GameWorldComponent.KEY.get(ctx.getSource().getWorld()).offTasks.contains(it) ? ":off" : "")).collect(Collectors.joining(", "))))
//                        .then(CommandManager.argument("task", StringArgumentType.string())
//                                .executes(ctx -> {
//                                    try {
//                                        PlayerMoodComponent.Task task = PlayerMoodComponent.Task.valueOf(StringArgumentType.getString(ctx, "task"));
//                                        ArrayList<PlayerMoodComponent.Task> offTasks = GameWorldComponent.KEY.get(ctx.getSource().getWorld()).offTasks;
//                                        if (!offTasks.remove(task))
//                                            offTasks.add(task);
//                                    } catch (Exception e) {
//                                        ctx.getSource().sendError(Text.literal("Enum name incorrect. Nothing changed"));
//                                        return 0;
//                                    }
//                                    return 1;
//                                })))
//        );
    }

//    private static int print(CommandContext<ServerCommandSource> ctx, String string) {
//        ctx.getSource().sendMessage(Text.of(string));
//        return 1;
//    }
//
//    private static int executeArea(ServerCommandSource source, BiConsumer<AreasWorldComponent, Box> consumer, Vec3d first, Vec3d second) {
//        consumer.accept(AreasWorldComponent.KEY.get(source.getWorld()), new Box(first, second));
//        return 1;
//    }
//
//    private static <T> int executeArea(ServerCommandSource source, BiConsumer<AreasWorldComponent, T> consumer, T value) {
//        consumer.accept(AreasWorldComponent.KEY.get(source.getWorld()), value);
//        return 1;
//    }
//
//    private static <T> int execute(ServerCommandSource source, BiConsumer<GameWorldComponent, T> consumer, T value) {
//        consumer.accept(GameWorldComponent.KEY.get(source.getWorld()), value);
//        return 1;
//    }
//
//    private static <T> int printArea(ServerCommandSource source, Function<AreasWorldComponent, T> func) {
//        source.sendMessage(Text.literal(func.apply(AreasWorldComponent.KEY.get(source.getWorld())).toString()));
//        return 1;
//    }
}
