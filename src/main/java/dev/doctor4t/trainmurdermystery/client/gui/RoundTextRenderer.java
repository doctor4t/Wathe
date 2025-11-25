package dev.doctor4t.trainmurdermystery.client.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.doctor4t.trainmurdermystery.api.TMMGameModes;
import dev.doctor4t.trainmurdermystery.cca.GameRoundEndComponent;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class RoundTextRenderer {
    private static final Map<String, Optional<GameProfile>> failCache = new HashMap<>();
    private static final int WELCOME_DURATION = 200 + GameConstants.FADE_TIME * 2;
    private static final int END_DURATION = 200;
    private static RoleAnnouncementTexts.RoleAnnouncementText role = RoleAnnouncementTexts.CIVILIAN;
    private static int welcomeTime = 0;
    private static int killers = 0;
    private static int targets = 0;
    private static int endTime = 0;

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    public static void renderHud(TextRenderer renderer, ClientPlayerEntity player, @NotNull DrawContext context) {
        var isLooseEnds = GameWorldComponent.KEY.get(player.getWorld()).getGameMode() == TMMGameModes.LOOSE_ENDS;

        if (welcomeTime > 0) {
            context.getMatrices().push();
            context.getMatrices().translate(context.getScaledWindowWidth() / 2f, context.getScaledWindowHeight() / 2f + 3.5, 0);
            context.getMatrices().push();
            context.getMatrices().scale(2.6f, 2.6f, 1f);
            var color = isLooseEnds ? 0x9F0000 : 0xFFFFFF;
            if (welcomeTime <= 180) {
                var welcomeText = isLooseEnds ? Text.translatable("announcement.loose_ends.welcome") : role.welcomeText;
                context.drawTextWithShadow(renderer, welcomeText, -renderer.getWidth(welcomeText) / 2, -12, color);
            }
            context.getMatrices().pop();
            context.getMatrices().push();
            context.getMatrices().scale(1.2f, 1.2f, 1f);
            if (welcomeTime <= 120) {
                var premiseText = isLooseEnds ? Text.translatable("announcement.loose_ends.premise") : role.premiseText.apply(killers);
                context.drawTextWithShadow(renderer, premiseText, -renderer.getWidth(premiseText) / 2, 0, color);
            }
            context.getMatrices().pop();
            context.getMatrices().push();
            context.getMatrices().scale(1f, 1f, 1f);
            if (welcomeTime <= 60) {
                var goalText = isLooseEnds ? Text.translatable("announcement.loose_ends.goal") : role.goalText.apply(targets);
                context.drawTextWithShadow(renderer, goalText, -renderer.getWidth(goalText) / 2, 14, color);
            }
            context.getMatrices().pop();
            context.getMatrices().pop();
        }
        var game = GameWorldComponent.KEY.get(player.getWorld());
        if (endTime > 0 && endTime < END_DURATION - (GameConstants.FADE_TIME * 2) && !game.isRunning() && game.getGameMode() != TMMGameModes.DISCOVERY) {
            var roundEnd = GameRoundEndComponent.KEY.get(player.getWorld());
            if (roundEnd.getWinStatus() == GameFunctions.WinStatus.NONE) return;
            var winner = player.getWorld().getPlayerByUuid(game.getLooseEndWinner() == null ? UUID.randomUUID() : game.getLooseEndWinner());
            var endText = role.getEndText(roundEnd.getWinStatus(), winner == null ? Text.empty() : winner.getDisplayName());
            if (endText == null) return;
            context.getMatrices().push();
            context.getMatrices().translate(context.getScaledWindowWidth() / 2f, context.getScaledWindowHeight() / 2f - 40, 0);
            context.getMatrices().push();
            context.getMatrices().scale(2.6f, 2.6f, 1f);
            context.drawTextWithShadow(renderer, endText, -renderer.getWidth(endText) / 2, -12, 0xFFFFFF);
            context.getMatrices().pop();
            context.getMatrices().push();
            context.getMatrices().scale(1.2f, 1.2f, 1f);
            var winMessage = Text.translatable("game.win." + roundEnd.getWinStatus().name().toLowerCase().toLowerCase());
            context.drawTextWithShadow(renderer, winMessage, -renderer.getWidth(winMessage) / 2, -4, 0xFFFFFF);
            context.getMatrices().pop();
            if (isLooseEnds) {
                context.drawTextWithShadow(renderer, RoleAnnouncementTexts.LOOSE_END.titleText, -renderer.getWidth(RoleAnnouncementTexts.LOOSE_END.titleText) / 2, 14, 0xFFFFFF);
                var looseEnds = 0;
                for (var entry : roundEnd.getPlayers()) {
                    context.getMatrices().push();
                    context.getMatrices().scale(2f, 2f, 1f);
                    context.getMatrices().translate(((looseEnds % 6) - 3.5) * 12, 14 + (looseEnds / 6) * 12, 0);
                    looseEnds++;
                    var playerEntry = TMMClient.PLAYER_ENTRIES_CACHE.get(entry.player().getId());
                    if (playerEntry != null && playerEntry.getSkinTextures().texture() != null) {
                        var texture = playerEntry.getSkinTextures().texture();
                        RenderSystem.enableBlend();
                        context.getMatrices().push();
                        context.getMatrices().translate(8, 0, 0);
                        var offColour = entry.wasDead() ? 0.4f : 1f;
                        context.drawTexturedQuad(texture, 0, 8, 0, 8, 0, 8 / 64f, 16 / 64f, 8 / 64f, 16 / 64f, 1f, offColour, offColour, 1f);
                        context.getMatrices().translate(-0.5, -0.5, 0);
                        context.getMatrices().scale(1.125f, 1.125f, 1f);
                        context.drawTexturedQuad(texture, 0, 8, 0, 8, 0, 40 / 64f, 48 / 64f, 8 / 64f, 16 / 64f, 1f, offColour, offColour, 1f);
                        context.getMatrices().pop();
                    }
                    if (entry.wasDead()) {
                        context.getMatrices().translate(13, 0, 0);
                        context.getMatrices().scale(2f, 1f, 1f);
                        context.drawText(renderer, "x", -renderer.getWidth("x") / 2, 0, 0xE10000, false);
                        context.drawText(renderer, "x", -renderer.getWidth("x") / 2, 1, 0x550000, false);
                    }
                    context.getMatrices().pop();
                }
                context.getMatrices().pop();
            } else {
                var vigilanteTotal = 1;
                for (var entry : roundEnd.getPlayers())
                    if (entry.role() == RoleAnnouncementTexts.VIGILANTE) vigilanteTotal += 1;
                context.drawTextWithShadow(renderer, RoleAnnouncementTexts.CIVILIAN.titleText, -renderer.getWidth(RoleAnnouncementTexts.CIVILIAN.titleText) / 2 - 60, 14, 0xFFFFFF);
                context.drawTextWithShadow(renderer, RoleAnnouncementTexts.VIGILANTE.titleText, -renderer.getWidth(RoleAnnouncementTexts.VIGILANTE.titleText) / 2 + 50, 14, 0xFFFFFF);
                context.drawTextWithShadow(renderer, RoleAnnouncementTexts.KILLER.titleText, -renderer.getWidth(RoleAnnouncementTexts.KILLER.titleText) / 2 + 50, 14 + 16 + 24 * ((vigilanteTotal) / 2), 0xFFFFFF);
                var civilians = 0;
                var vigilantes = 0;
                var killers = 0;
                for (var entry : roundEnd.getPlayers()) {
                    context.getMatrices().push();
                    context.getMatrices().scale(2f, 2f, 1f);

                    if (entry.role() == RoleAnnouncementTexts.CIVILIAN) {
                        context.getMatrices().translate(-60 + (civilians % 4) * 12, 14 + (civilians / 4) * 12, 0);
                        civilians++;
                    } else if (entry.role() == RoleAnnouncementTexts.VIGILANTE) {
                        context.getMatrices().translate(7 + (vigilantes % 2) * 12, 14 + (vigilantes / 2) * 12, 0);
                        vigilantes++;
                    } else if (entry.role() == RoleAnnouncementTexts.KILLER) {
                        context.getMatrices().translate(0, 8 + ((vigilanteTotal) / 2) * 12, 0);
                        context.getMatrices().translate(7 + (killers % 2) * 12, 14 + (killers / 2) * 12, 0);
                        killers++;
                    }

                    PlayerListEntry playerListEntry = TMMClient.PLAYER_ENTRIES_CACHE.get(entry.player().getId());
                    if (playerListEntry != null) {
                        var texture = playerListEntry.getSkinTextures().texture();
                        if (texture != null) {
                            RenderSystem.enableBlend();
                            context.getMatrices().push();
                            context.getMatrices().translate(8, 0, 0);
                            var offColour = entry.wasDead() ? 0.4f : 1f;
                            context.drawTexturedQuad(texture, 0, 8, 0, 8, 0, 8 / 64f, 16 / 64f, 8 / 64f, 16 / 64f, 1f, offColour, offColour, 1f);
                            context.getMatrices().translate(-0.5, -0.5, 0);
                            context.getMatrices().scale(1.125f, 1.125f, 1f);
                            context.drawTexturedQuad(texture, 0, 8, 0, 8, 0, 40 / 64f, 48 / 64f, 8 / 64f, 16 / 64f, 1f, offColour, offColour, 1f);
                            context.getMatrices().pop();
                        }
                        if (entry.wasDead()) {
                            context.getMatrices().translate(13, 0, 0);
                            context.getMatrices().scale(2f, 1f, 1f);
                            context.drawText(renderer, "x", -renderer.getWidth("x") / 2, 0, 0xE10000, false);
                            context.drawText(renderer, "x", -renderer.getWidth("x") / 2, 1, 0x550000, false);
                        }
                    }
                    context.getMatrices().pop();
                }
                context.getMatrices().pop();
            }
        }
    }

    public static void tick() {
        if (MinecraftClient.getInstance().world != null && GameWorldComponent.KEY.get(MinecraftClient.getInstance().world).getGameMode() != TMMGameModes.DISCOVERY) {
            var player = MinecraftClient.getInstance().player;
            if (welcomeTime > 0) {
                switch (welcomeTime) {
                    case 200 -> {
                        if (player != null)
                            player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), TMMSounds.UI_RISER, SoundCategory.MASTER, 10f, 1f, player.getRandom().nextLong());
                    }
                    case 180 -> {
                        if (player != null)
                            player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), TMMSounds.UI_PIANO, SoundCategory.MASTER, 10f, 1.25f, player.getRandom().nextLong());
                    }
                    case 120 -> {
                        if (player != null)
                            player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), TMMSounds.UI_PIANO, SoundCategory.MASTER, 10f, 1.5f, player.getRandom().nextLong());
                    }
                    case 60 -> {
                        if (player != null)
                            player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), TMMSounds.UI_PIANO, SoundCategory.MASTER, 10f, 1.75f, player.getRandom().nextLong());
                    }
                    case 1 -> {
                        if (player != null)
                            player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), TMMSounds.UI_PIANO_STINGER, SoundCategory.MASTER, 10f, 1f, player.getRandom().nextLong());
                    }
                }
                welcomeTime--;
            }
            if (endTime > 0) {
                if (endTime == END_DURATION - (GameConstants.FADE_TIME * 2)) {
                    if (player != null)
                        player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), GameRoundEndComponent.KEY.get(player.getWorld()).didWin(player.getUuid()) ? TMMSounds.UI_PIANO_WIN : TMMSounds.UI_PIANO_LOSE, SoundCategory.MASTER, 10f, 1f, player.getRandom().nextLong());
                }
                endTime--;
            }
            var options = MinecraftClient.getInstance().options;
            if (options != null && options.playerListKey.isPressed()) endTime = Math.max(2, endTime);
        }
    }

    public static void startWelcome(RoleAnnouncementTexts.RoleAnnouncementText role, int killers, int targets) {
        RoundTextRenderer.role = role;
        welcomeTime = WELCOME_DURATION;
        RoundTextRenderer.killers = killers;
        RoundTextRenderer.targets = targets;
    }

    public static void startEnd() {
        welcomeTime = 0;
        endTime = END_DURATION;
    }

    public static GameProfile getGameProfile(String disguise) {
        var optional = SkullBlockEntity.fetchProfileByName(disguise).getNow(failCache(disguise));
        return optional.orElse(failCache(disguise).get());
    }

    public static SkinTextures getSkinTextures(String disguise) {
        return MinecraftClient.getInstance().getSkinProvider().getSkinTextures(getGameProfile(disguise));
    }

    public static Optional<GameProfile> failCache(String name) {
        return failCache.computeIfAbsent(name, (d) -> Optional.of(new GameProfile(UUID.randomUUID(), name)));
    }

}