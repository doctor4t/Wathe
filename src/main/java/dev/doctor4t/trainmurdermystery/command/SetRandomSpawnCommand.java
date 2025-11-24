package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SetRandomSpawnCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tmm:setRandomSpawn")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("disabled")
                        .executes(context -> execute(context.getSource(), GameWorldComponent.SpawnMode.DISABLED)))
                .then(CommandManager.literal("shuffle")
                        .executes(context -> execute(context.getSource(), GameWorldComponent.SpawnMode.SHUFFLE)))
                .then(CommandManager.literal("randomPos")
                        .executes(context -> execute(context.getSource(), GameWorldComponent.SpawnMode.RANDOM_POS)))
                .then(CommandManager.literal("positions")
                        .then(CommandManager.literal("add")
                                .executes(context -> addPosition(context.getSource())))
                        .then(CommandManager.literal("clear")
                                .executes(context -> clearPositions(context.getSource())))
                        .then(CommandManager.literal("list")
                                .executes(context -> listPositions(context.getSource()))))
        );
    }

    private static int execute(ServerCommandSource source, GameWorldComponent.SpawnMode mode) {
        return TMM.executeSupporterCommand(source,
                () -> {
                    GameWorldComponent gameWorldComponent = GameWorldComponent.KEY.get(source.getWorld());
                    gameWorldComponent.setSpawnMode(mode);
                    if (source.getPlayer() != null) {
                        source.getPlayer().sendMessage(Text.literal("Random spawn mode set to " + mode.asString()), false);
                    }
                }
        );
    }

    private static int addPosition(ServerCommandSource source) {
        return TMM.executeSupporterCommand(source,
                () -> {
                    ServerPlayerEntity player = source.getPlayer();
                    GameWorldComponent gameWorldComponent = GameWorldComponent.KEY.get(source.getWorld());
                    GameConstants.SpawnPoint spawnPoint = new GameConstants.SpawnPoint(player.getPos(), player.getYaw(), player.getPitch());
                    gameWorldComponent.addRandomSpawnPosition(spawnPoint);
                    player.sendMessage(Text.literal("Added random spawn position #" + gameWorldComponent.getRandomSpawnPositions().size()), false);
                }
        );
    }

    private static int clearPositions(ServerCommandSource source) {
        return TMM.executeSupporterCommand(source,
                () -> {
                    ServerPlayerEntity player = source.getPlayer();
                    GameWorldComponent gameWorldComponent = GameWorldComponent.KEY.get(source.getWorld());
                    gameWorldComponent.clearRandomSpawnPositions();
                    player.sendMessage(Text.literal("Cleared custom random spawn positions"), false);
                }
        );
    }

    private static int listPositions(ServerCommandSource source) {
        return TMM.executeSupporterCommand(source,
                () -> {
                    ServerPlayerEntity player = source.getPlayer();
                    GameWorldComponent gameWorldComponent = GameWorldComponent.KEY.get(source.getWorld());
                    var positions = gameWorldComponent.getRandomSpawnPositions();
                    player.sendMessage(Text.literal("Random spawn positions (" + positions.size() + "):"), false);
                    for (int i = 0; i < positions.size(); i++) {
                        GameConstants.SpawnPoint spawnPoint = positions.get(i);
                        player.sendMessage(Text.literal("#" + (i + 1) + " " +
                                String.format("(%.1f, %.1f, %.1f) yaw=%.1f pitch=%.1f",
                                        spawnPoint.pos().getX(), spawnPoint.pos().getY(), spawnPoint.pos().getZ(),
                                        spawnPoint.yaw(), spawnPoint.pitch())), false);
                    }
                }
        );
    }
}
