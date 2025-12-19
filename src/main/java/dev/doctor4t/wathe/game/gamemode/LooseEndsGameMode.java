package dev.doctor4t.wathe.game.gamemode;

<<<<<<< HEAD:src/main/java/dev/doctor4t/trainmurdermystery/game/LooseEndsGameMode.java
import dev.doctor4t.trainmurdermystery.api.GameMode;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.cca.GameRoundEndComponent;
import dev.doctor4t.trainmurdermystery.cca.GameTimeComponent;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.TrainWorldComponent;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.networking.AnnounceWelcomeS2CPayload;
=======
import dev.doctor4t.wathe.api.GameMode;
import dev.doctor4t.wathe.api.WatheRoles;
import dev.doctor4t.wathe.cca.GameRoundEndComponent;
import dev.doctor4t.wathe.cca.GameTimeComponent;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.TrainWorldComponent;
import dev.doctor4t.wathe.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.doctor4t.wathe.index.WatheItems;
import dev.doctor4t.wathe.util.AnnounceWelcomePayload;
>>>>>>> upstream/main:src/main/java/dev/doctor4t/wathe/game/gamemode/LooseEndsGameMode.java
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public class LooseEndsGameMode extends GameMode {
    public LooseEndsGameMode(Identifier identifier) {
        super(identifier, 60, 2);
    }

    @Override
    public void initializeGame(ServerWorld serverWorld, GameWorldComponent gameWorldComponent, List<ServerPlayerEntity> players) {
        for (ServerPlayerEntity player : players) {
            player.getInventory().clear();

            ItemStack derringer = new ItemStack(WatheItems.DERRINGER);
            ItemStack knife = new ItemStack(WatheItems.KNIFE);

            int cooldown = GameConstants.getInTicks(1, 0);
            ItemCooldownManager itemCooldownManager = player.getItemCooldownManager();
            itemCooldownManager.set(WatheItems.DERRINGER, cooldown);
            itemCooldownManager.set(WatheItems.KNIFE, cooldown);

            player.giveItemStack(new ItemStack(WatheItems.CROWBAR));
            player.giveItemStack(derringer);
            player.giveItemStack(knife);

            gameWorldComponent.addRole(player, WatheRoles.LOOSE_END);

            ServerPlayNetworking.send(player, new AnnounceWelcomeS2CPayload(RoleAnnouncementTexts.ROLE_ANNOUNCEMENT_TEXTS.indexOf(RoleAnnouncementTexts.LOOSE_END), -1, -1));
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
