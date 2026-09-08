package dev.doctor4t.wathe.game.gamemode;

import dev.doctor4t.wathe.api.GameMode;
import dev.doctor4t.wathe.api.WatheRoles;
import dev.doctor4t.wathe.cca.GameRoundEndComponent;
import dev.doctor4t.wathe.cca.GameTimeComponent;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import dev.doctor4t.wathe.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.doctor4t.wathe.network.AnnounceWelcomePayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SecretMurderGameMode extends GameMode {
    public SecretMurderGameMode(Identifier identifier) {
        super(identifier, 10, 6);
    }

    private static int assignRolesAndGetKillerCount(@NotNull ServerWorld world, @NotNull List<ServerPlayerEntity> players, GameWorldComponent gameComponent) {
        // civilian base role, replaced for selected killers and vigilantes
        for (ServerPlayerEntity player : players) {
            gameComponent.addRole(player, WatheRoles.SECRET_KILLER);
            PlayerShopComponent.KEY.get(player).setBalance(GameConstants.MONEY_START);
        }

        return (int) Math.floor((double) players.size() / gameComponent.getKillerDividend());
    }

    @Override
    public void initializeGame(ServerWorld serverWorld, GameWorldComponent gameWorldComponent, List<ServerPlayerEntity> players) {
        int killerCount = assignRolesAndGetKillerCount(serverWorld, players, gameWorldComponent);

        for (ServerPlayerEntity player : players) {
            ServerPlayNetworking.send(player, new AnnounceWelcomePayload(RoleAnnouncementTexts.ROLE_ANNOUNCEMENT_TEXTS.indexOf(RoleAnnouncementTexts.KILLER), killerCount, players.size() - killerCount));
        }
    }


    @Override
    public void tickServerGameLoop(ServerWorld serverWorld, GameWorldComponent gameWorldComponent) {
        GameFunctions.WinStatus winStatus = GameFunctions.WinStatus.NONE;

        // check if out of time
        if (!GameTimeComponent.KEY.get(serverWorld).hasTime())
            winStatus = GameFunctions.WinStatus.TIME;

        // check if last person standing in loose end
        int playersLeft = 0;
        PlayerEntity lastPlayer = null;
        for (PlayerEntity player : serverWorld.getPlayers()) {
            if (GameFunctions.isPlayerAliveAndSurvival(player)) {
                playersLeft++;
                lastPlayer = player;
            }
        }

        if (playersLeft <= 0) {
            GameFunctions.stopGame(serverWorld);
        }

        if (playersLeft == 1) {
            gameWorldComponent.setLooseEndWinner(lastPlayer.getUuid());
            winStatus = GameFunctions.WinStatus.LOOSE_END;
        }

        // game end on win and display
        if (winStatus != GameFunctions.WinStatus.NONE && gameWorldComponent.getGameStatus() == GameWorldComponent.GameStatus.ACTIVE) {
            GameRoundEndComponent.KEY.get(serverWorld).setRoundEndData(serverWorld.getPlayers(), winStatus);

            GameFunctions.stopGame(serverWorld);
        }
    }
}
