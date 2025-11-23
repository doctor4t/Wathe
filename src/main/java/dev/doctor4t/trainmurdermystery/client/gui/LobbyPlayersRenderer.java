package dev.doctor4t.trainmurdermystery.client.gui;

import dev.doctor4t.ratatouille.util.TextUtils;
import dev.doctor4t.trainmurdermystery.cca.AutoStartComponent;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class LobbyPlayersRenderer {
    public static void renderHud(TextRenderer renderer, @NotNull ClientPlayerEntity player, @NotNull DrawContext context) {
        var game = GameWorldComponent.KEY.get(player.getWorld());
        if (!game.isRunning()) {
            context.getMatrices().push();
            context.getMatrices().translate(context.getScaledWindowWidth() / 2f, 6, 0);
            var world = player.getWorld();
            var players = world.getPlayers();
            var count = players.size();
            int readyPlayerCount = GameFunctions.getReadyPlayerCount(world);
            var playerCountText = Text.translatable("lobby.players.count", readyPlayerCount, count);
            context.drawTextWithShadow(renderer, playerCountText, -renderer.getWidth(playerCountText) / 2, 0, 0xFFFFFFFF);

            AutoStartComponent autoStartComponent = AutoStartComponent.KEY.get(world);
            if (autoStartComponent.isAutoStartActive()) {
                MutableText autoStartText;
                int color = 0xFFAAAAAA;
                if (readyPlayerCount >= GameConstants.MIN_PLAYER_COUNT) {
                    int seconds = autoStartComponent.getTime() / 20;
                    autoStartText = Text.translatable(seconds <= 0 ? "lobby.autostart.starting" : "lobby.autostart.time", seconds);
                    color = 0xFF00BC16;
                } else {
                    autoStartText = Text.translatable("lobby.autostart.active");
                }
                context.drawTextWithShadow(renderer, autoStartText, -renderer.getWidth(autoStartText) / 2, 10, color);
            }

            context.getMatrices().pop();

            context.getMatrices().push();
            float scale = 0.75f;
            context.getMatrices().translate(0, context.getScaledWindowHeight(), 0);
            context.getMatrices().scale(scale, scale, 1f);
            int i = 0;
            MutableText thanksText = Text.translatable("credits.trainmurdermystery.thank_you");

            String fallback = "Thank you for playing The Last Voyage of the Harpy Express!\nMe and my team spent a lot of time working\non this mod and we hope you enjoy it.\nIf you do and wish to make a video or stream\nplease make sure to credit my channel,\nvideo and the mod page!\n - RAT / doctor4t";
            if (!thanksText.getString().contains(" - RAT / doctor4t")) {
                thanksText = Text.literal(fallback);
            }

            for (Text text : TextUtils.getWithLineBreaks(thanksText)) {
                i++;
                context.drawTextWithShadow(renderer, text, 10, -90 + 10 * i, 0xFFFFFFFF);
            }
            context.getMatrices().pop();
        }
    }
}