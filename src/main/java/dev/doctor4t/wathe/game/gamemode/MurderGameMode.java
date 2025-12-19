package dev.doctor4t.wathe.game.gamemode;

<<<<<<< HEAD:src/main/java/dev/doctor4t/trainmurdermystery/game/MurderGameMode.java
import dev.doctor4t.trainmurdermystery.api.GameMode;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.cca.*;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.trainmurdermystery.networking.AnnounceWelcomeS2CPayload;
=======
import dev.doctor4t.wathe.api.GameMode;
import dev.doctor4t.wathe.api.WatheRoles;
import dev.doctor4t.wathe.cca.*;
import dev.doctor4t.wathe.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.doctor4t.wathe.util.AnnounceWelcomePayload;
>>>>>>> upstream/main:src/main/java/dev/doctor4t/wathe/game/gamemode/MurderGameMode.java
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class MurderGameMode extends GameMode {
    public MurderGameMode(Identifier identifier) {
        super(identifier, 10, 6);
    }

    private static int assignRolesAndGetKillerCount(@NotNull ServerWorld world, @NotNull List<ServerPlayerEntity> players, GameWorldComponent gameComponent) {
        // civilian base role, replaced for selected killers and vigilantes
        for (ServerPlayerEntity player : players) {
            gameComponent.addRole(player, WatheRoles.CIVILIAN);
        }

        // select roles
        ScoreboardRoleSelectorComponent roleSelector = ScoreboardRoleSelectorComponent.KEY.get(world.getScoreboard());
        int total = roleSelector.assignKillers(world, gameComponent, players, (int) Math.floor((double) players.size() / gameComponent.getKillerDividend()));
        roleSelector.assignVigilantes(world, gameComponent, players,  (int) Math.floor((double) players.size() / gameComponent.getVigilanteDividend()));
        return total;
    }

    @Override
    public void initializeGame(ServerWorld serverWorld, GameWorldComponent gameWorldComponent, List<ServerPlayerEntity> players) {
        int killerCount = assignRolesAndGetKillerCount(serverWorld, players, gameWorldComponent);

        for (ServerPlayerEntity player : players) {
<<<<<<< HEAD:src/main/java/dev/doctor4t/trainmurdermystery/game/MurderGameMode.java
            ServerPlayNetworking.send(player, new AnnounceWelcomeS2CPayload(RoleAnnouncementTexts.ROLE_ANNOUNCEMENT_TEXTS.indexOf(gameWorldComponent.isRole(player, TMMRoles.KILLER) ? RoleAnnouncementTexts.KILLER : gameWorldComponent.isRole(player, TMMRoles.VIGILANTE) ? RoleAnnouncementTexts.VIGILANTE : RoleAnnouncementTexts.CIVILIAN), killerCount, players.size() - killerCount));
=======
            ServerPlayNetworking.send(player, new AnnounceWelcomePayload(RoleAnnouncementTexts.ROLE_ANNOUNCEMENT_TEXTS.indexOf(gameWorldComponent.isRole(player, WatheRoles.KILLER) ? RoleAnnouncementTexts.KILLER : gameWorldComponent.isRole(player, WatheRoles.VIGILANTE) ? RoleAnnouncementTexts.VIGILANTE : RoleAnnouncementTexts.CIVILIAN), killerCount, players.size() - killerCount));
>>>>>>> upstream/main:src/main/java/dev/doctor4t/wathe/game/gamemode/MurderGameMode.java
        }
    }

    @Override
    public void tickServerGameLoop(ServerWorld serverWorld, GameWorldComponent gameWorldComponent) {
        GameFunctions.WinStatus winStatus = GameFunctions.WinStatus.NONE;

        // check if out of time
        if (!GameTimeComponent.KEY.get(serverWorld).hasTime())
            winStatus = GameFunctions.WinStatus.TIME;

        boolean civilianAlive = false;
        for (ServerPlayerEntity player : serverWorld.getPlayers()) {
            // passive money
            if (gameWorldComponent.canUseKillerFeatures(player)) {
                Integer balanceToAdd = GameConstants.PASSIVE_MONEY_TICKER.apply(serverWorld.getTime());
                if (balanceToAdd > 0) PlayerShopComponent.KEY.get(player).addToBalance(balanceToAdd);
            }

            // check if some civilians are still alive
            if (gameWorldComponent.isInnocent(player) && !GameFunctions.isPlayerEliminated(player)) {
                civilianAlive = true;
            }
        }

        // check killer win condition (killed all civilians)
        if (!civilianAlive) {
            winStatus = GameFunctions.WinStatus.KILLERS;
        }

        // check passenger win condition (all killers are dead)
        if (winStatus == GameFunctions.WinStatus.NONE) {
            winStatus = GameFunctions.WinStatus.PASSENGERS;
            for (UUID player : gameWorldComponent.getAllKillerTeamPlayers()) {
                if (!GameFunctions.isPlayerEliminated(serverWorld.getPlayerByUuid(player))) {
                    winStatus = GameFunctions.WinStatus.NONE;
                }
            }
        }

        // game end on win and display
        if (winStatus != GameFunctions.WinStatus.NONE && gameWorldComponent.getGameStatus() == GameWorldComponent.GameStatus.ACTIVE) {
            GameRoundEndComponent.KEY.get(serverWorld).setRoundEndData(serverWorld.getPlayers(), winStatus);

            GameFunctions.stopGame(serverWorld);
        }
    }
}
