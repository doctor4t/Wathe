package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class SetShootInnocentPunishmentCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tmm:setShootInnocentPunishment")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("default")
                        .executes(context -> execute(context.getSource(), GameWorldComponent.ShootInnocentPunishment.DEFAULT)))
                .then(CommandManager.literal("preventAllGunPickup")
                        .executes(context -> execute(context.getSource(), GameWorldComponent.ShootInnocentPunishment.PREVENT_ALL_GUN_PICKUP)))
                .then(CommandManager.literal("killShooter")
                        .executes(context -> execute(context.getSource(), GameWorldComponent.ShootInnocentPunishment.KILL_SHOOTER)))
        );
    }

    private static int execute(ServerCommandSource source, GameWorldComponent.ShootInnocentPunishment shootInnocentPunishment) {
        return TMM.executeSupporterCommand(source,
                () -> {
                    GameWorldComponent gameWorldComponent = GameWorldComponent.KEY.get(source.getWorld());
                    gameWorldComponent.setInnocentShootPunishment(shootInnocentPunishment);
                    if (source.getPlayer() != null) {
                        source.sendMessage(Text.literal("Shoot innocent punishment set to " + shootInnocentPunishment.toString()));
                    }
                });
    }
}
