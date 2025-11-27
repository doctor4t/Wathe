package dev.doctor4t.trainmurdermystery.game;

import dev.doctor4t.trainmurdermystery.api.GameMode;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.cca.GameRoundEndComponent;
import dev.doctor4t.trainmurdermystery.cca.GameTimeComponent;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.TrainWorldComponent;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.util.AnnounceWelcomePayload;
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
        TrainWorldComponent.KEY.get(serverWorld).setTimeOfDay(TrainWorldComponent.TimeOfDay.SUNDOWN);

        for (ServerPlayerEntity player : players) {
            player.getInventory().clear();

            ItemStack derringer = new ItemStack(TMMItems.DERRINGER);
            ItemStack knife = new ItemStack(TMMItems.KNIFE);

            int cooldown = GameConstants.getInTicks(1, 0);
            ItemCooldownManager itemCooldownManager = player.getItemCooldownManager();
            itemCooldownManager.set(TMMItems.DERRINGER, cooldown);
            itemCooldownManager.set(TMMItems.KNIFE, cooldown);

            player.giveItemStack(new ItemStack(TMMItems.CROWBAR));
            player.giveItemStack(derringer);
            player.giveItemStack(knife);

            gameWorldComponent.addRole(player, TMMRoles.LOOSE_END);

            ServerPlayNetworking.send(player, new AnnounceWelcomePayload(RoleAnnouncementTexts.ROLE_ANNOUNCEMENT_TEXTS.indexOf(RoleAnnouncementTexts.LOOSE_END), -1, -1));
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
