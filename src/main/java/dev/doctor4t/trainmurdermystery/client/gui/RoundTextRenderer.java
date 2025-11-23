package dev.doctor4t.trainmurdermystery.client.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.doctor4t.trainmurdermystery.cca.GameRoundEndComponent;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public final class RoundTextRenderer {
    private static final Map<String, GameProfile> failCache = new HashMap<>();
    private static final int WELCOME_DURATION = 200 + GameConstants.FADE_TIME * 2;
    private static final int END_DURATION = 200;
    private static RoleAnnouncementTexts.RoleAnnouncementText role = RoleAnnouncementTexts.CIVILIAN;
    private static int welcomeTime = 0;
    private static int killers = 0;
    private static int targets = 0;
    private static int endTime = 0;

    private RoundTextRenderer() {
    }

    public static void renderHud(TextRenderer renderer, ClientPlayerEntity player, @NotNull DrawContext context) {
        boolean isLooseEnds = GameWorldComponent.KEY.get(player.getWorld()).getGameMode() == GameWorldComponent.GameMode.LOOSE_ENDS;

        // 47 OCCURRENCES and no extraction?? How many hours of sleep were you running on
        MatrixStack matrices = context.getMatrices();

        renderWelcome(renderer, context, matrices, isLooseEnds);
        GameWorldComponent game = GameWorldComponent.KEY.get(player.getWorld());
        renderEnding(renderer, player, context, game, matrices, isLooseEnds);
    }

    private static void renderWelcome(TextRenderer renderer, @NotNull DrawContext context, MatrixStack matrices, boolean isLooseEnds) {
        if (welcomeTime <= 0) {
            return;
        }

        matrices.push();
        matrices.translate(context.getScaledWindowWidth() / 2f, context.getScaledWindowHeight() / 2f + 3.5, 0);

        matrices.push();
        matrices.scale(2.6f, 2.6f, 1f);
        int color = isLooseEnds ? 0x9F0000 : 0xFFFFFF;
        if (welcomeTime <= 180) {
            Text welcomeText = isLooseEnds ? Text.translatable("announcement.loose_ends.welcome") : role.welcomeText;
            context.drawTextWithShadow(renderer, welcomeText, -renderer.getWidth(welcomeText) / 2, -12, color);
        }
        matrices.pop();

        matrices.push();
        matrices.scale(1.2f, 1.2f, 1f);
        if (welcomeTime <= 120) {
            Text premiseText = isLooseEnds ? Text.translatable("announcement.loose_ends.premise") : role.premiseText.apply(killers);
            context.drawTextWithShadow(renderer, premiseText, -renderer.getWidth(premiseText) / 2, 0, color);
        }
        matrices.pop();

        matrices.push();
        matrices.scale(1f, 1f, 1f);
        if (welcomeTime <= 60) {
            Text goalText = isLooseEnds ? Text.translatable("announcement.loose_ends.goal") : role.goalText.apply(targets);
            context.drawTextWithShadow(renderer, goalText, -renderer.getWidth(goalText) / 2, 14, color);
        }
        matrices.pop();

        matrices.pop();
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private static void renderEnding(TextRenderer renderer, ClientPlayerEntity player, @NotNull DrawContext context, GameWorldComponent game, MatrixStack matrices, boolean isLooseEnds) {
        if (endTime <= 0) {
            return;
        }
        if (endTime >= END_DURATION - (GameConstants.FADE_TIME * 2)) {
            return;
        }
        if (game.isRunning()) {
            return;
        }
        if (game.getGameMode() == GameWorldComponent.GameMode.DISCOVERY) {
            return;
        }

        GameRoundEndComponent roundEnd = GameRoundEndComponent.KEY.get(player.getWorld());
        if (roundEnd.getWinStatus() == GameFunctions.WinStatus.NONE) {
            return;
        }

        PlayerEntity winner = player.getWorld().getPlayerByUuid(game.getLooseEndWinner() == null ? UUID.randomUUID() : game.getLooseEndWinner());
        Text endText = role.getEndText(roundEnd.getWinStatus(), winner == null ? Text.empty() : winner.getDisplayName());
        if (endText == null) {
            return;
        }

        matrices.push();
        matrices.translate(context.getScaledWindowWidth() / 2f, context.getScaledWindowHeight() / 2f - 40, 0);

        matrices.push();
        matrices.scale(2.6f, 2.6f, 1f);
        context.drawTextWithShadow(renderer, endText, -renderer.getWidth(endText) / 2, -12, 0xFFFFFF);
        matrices.pop();

        matrices.push();
        matrices.scale(1.2f, 1.2f, 1f);
        MutableText winMessage = Text.translatable("game.win." + roundEnd.getWinStatus().name().toLowerCase().toLowerCase());
        context.drawTextWithShadow(renderer, winMessage, -renderer.getWidth(winMessage) / 2, -4, 0xFFFFFF);
        matrices.pop();

        if (isLooseEnds) {
            renderLooseEndsing(renderer, context, matrices, roundEnd);
            matrices.pop();
            return;
        }

        int vigilanteTotal = 1;
        for (GameRoundEndComponent.RoundEndData entry : roundEnd.getPlayers()) {
            if (entry.role() == RoleAnnouncementTexts.VIGILANTE) {
                vigilanteTotal += 1;
            }
        }

        context.drawTextWithShadow(renderer, RoleAnnouncementTexts.CIVILIAN.titleText, -renderer.getWidth(RoleAnnouncementTexts.CIVILIAN.titleText) / 2 - 60, 14, 0xFFFFFF);
        context.drawTextWithShadow(renderer, RoleAnnouncementTexts.VIGILANTE.titleText, -renderer.getWidth(RoleAnnouncementTexts.VIGILANTE.titleText) / 2 + 50, 14, 0xFFFFFF);
        context.drawTextWithShadow(renderer, RoleAnnouncementTexts.KILLER.titleText, -renderer.getWidth(RoleAnnouncementTexts.KILLER.titleText) / 2 + 50, 14 + 16 + 24 * ((vigilanteTotal) / 2), 0xFFFFFF);

        int civilians = 0;
        int vigilantes = 0;
        int killers = 0;
        // how many times are we going to iterate through the players
        for (GameRoundEndComponent.RoundEndData entry : roundEnd.getPlayers()) {
            matrices.push();
            matrices.scale(2f, 2f, 1f);

            if (entry.role() == RoleAnnouncementTexts.CIVILIAN) {
                matrices.translate(-60 + (civilians % 4) * 12, 14 + (civilians / 4) * 12, 0);
                civilians++;
            } else if (entry.role() == RoleAnnouncementTexts.VIGILANTE) {
                matrices.translate(7 + (vigilantes % 2) * 12, 14 + (vigilantes / 2) * 12, 0);
                vigilantes++;
            } else if (entry.role() == RoleAnnouncementTexts.KILLER) {
                matrices.translate(0, 8 + ((vigilanteTotal) / 2) * 12, 0);
                matrices.translate(7 + (killers % 2) * 12, 14 + (killers / 2) * 12, 0);
                killers++;
            }

            PlayerListEntry playerListEntry = TMMClient.PLAYER_ENTRIES_CACHE.get(entry.player().getId());
            if (playerListEntry == null) {
                matrices.pop();
                continue;
            }

            Identifier texture = playerListEntry.getSkinTextures().texture();
            if (texture != null) {
                RenderSystem.enableBlend();

                matrices.push();
                matrices.translate(8, 0, 0);
                float offColour = entry.wasDead() ? 0.4f : 1f;
                context.drawTexturedQuad(texture, 0, 8, 0, 8, 0, 8 / 64f, 16 / 64f, 8 / 64f, 16 / 64f, 1f, offColour, offColour, 1f);
                matrices.translate(-0.5, -0.5, 0);
                matrices.scale(1.125f, 1.125f, 1f);
                context.drawTexturedQuad(texture, 0, 8, 0, 8, 0, 40 / 64f, 48 / 64f, 8 / 64f, 16 / 64f, 1f, offColour, offColour, 1f);
                matrices.pop();
            }
            if (entry.wasDead()) {
                matrices.translate(13, 0, 0);
                matrices.scale(2f, 1f, 1f);
                context.drawText(renderer, "x", -renderer.getWidth("x") / 2, 0, 0xE10000, false);
                context.drawText(renderer, "x", -renderer.getWidth("x") / 2, 1, 0x550000, false);
            }
            matrices.pop();
        }
        matrices.pop();
    }

    // Haha, get it? Loose ends ending? Never mind - SkyNotTheLimit
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private static void renderLooseEndsing(TextRenderer renderer, @NotNull DrawContext context, MatrixStack matrices, GameRoundEndComponent roundEnd) {
        context.drawTextWithShadow(renderer, RoleAnnouncementTexts.LOOSE_END.titleText, -renderer.getWidth(RoleAnnouncementTexts.LOOSE_END.titleText) / 2, 14, 0xFFFFFF);
        int looseEnds = 0;

        for (GameRoundEndComponent.RoundEndData entry : roundEnd.getPlayers()) {
            matrices.push();
            matrices.scale(2f, 2f, 1f);
            matrices.translate(((looseEnds % 6) - 3.5) * 12, 14 + (looseEnds / 6) * 12, 0);
            looseEnds++;
            PlayerListEntry playerListEntry = TMMClient.PLAYER_ENTRIES_CACHE.get(entry.player().getId());
            if (playerListEntry != null) {
                Identifier texture = playerListEntry.getSkinTextures().texture();

                if (texture != null) {
                    RenderSystem.enableBlend();
                    matrices.push();
                    matrices.translate(8, 0, 0);
                    float offColour = entry.wasDead() ? 0.4f : 1f;
                    context.drawTexturedQuad(texture, 0, 8, 0, 8, 0, 8 / 64f, 16 / 64f, 8 / 64f, 16 / 64f, 1f, offColour, offColour, 1f);
                    matrices.translate(-0.5, -0.5, 0);
                    matrices.scale(1.125f, 1.125f, 1f);
                    context.drawTexturedQuad(texture, 0, 8, 0, 8, 0, 40 / 64f, 48 / 64f, 8 / 64f, 16 / 64f, 1f, offColour, offColour, 1f);
                    matrices.pop();
                }
            }
            if (entry.wasDead()) {
                matrices.translate(13, 0, 0);
                matrices.scale(2f, 1f, 1f);
                context.drawText(renderer, "x", -renderer.getWidth("x") / 2, 0, 0xE10000, false);
                context.drawText(renderer, "x", -renderer.getWidth("x") / 2, 1, 0x550000, false);
            }
            matrices.pop();
        }
    }

    public static void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) {
            return;
        }
        if (GameWorldComponent.KEY.get(client.world).getGameMode() == GameWorldComponent.GameMode.DISCOVERY) {
            return;
        }

        ClientPlayerEntity player = client.player;
        if (welcomeTime > 0) {
            if (player != null) {
                SoundEvent soundEvent = null;
                float pitch = 1;
                switch (welcomeTime) {
                    case 200 -> soundEvent = TMMSounds.UI_RISER;
                    case 180 -> {
                        soundEvent = TMMSounds.UI_PIANO;
                        pitch = 1.25f;
                    }
                    case 120 -> {
                        soundEvent = TMMSounds.UI_PIANO;
                        pitch = 1.5f;
                    }
                    case 60 -> {
                        soundEvent = TMMSounds.UI_PIANO;
                        pitch = 1.75f;
                    }
                    case 1 -> soundEvent = TMMSounds.UI_PIANO_STINGER;
                }
                if (soundEvent != null) {
                    player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), soundEvent, SoundCategory.MASTER, 10f, pitch, player.getRandom().nextLong());
                }
            }

            welcomeTime--;
        }
        if (endTime > 0) {
            if (endTime == END_DURATION - (GameConstants.FADE_TIME * 2)) {
                if (player != null) {
                    player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), GameRoundEndComponent.KEY.get(player.getWorld()).didWin(player.getUuid()) ? TMMSounds.UI_PIANO_WIN : TMMSounds.UI_PIANO_LOSE, SoundCategory.MASTER, 10f, 1f, player.getRandom().nextLong());
                }
            }
            endTime--;
        }
        GameOptions options = client.options;
        if (options != null && options.playerListKey.isPressed()) {
            endTime = Math.max(2, endTime);
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
        Optional<GameProfile> optional = SkullBlockEntity.fetchProfileByName(disguise).getNow(Optional.of(failCache(disguise)));
        return optional.orElse(failCache(disguise));
    }

    @SuppressWarnings("unused")
    public static SkinTextures getSkinTextures(String disguise) {
        return MinecraftClient.getInstance().getSkinProvider().getSkinTextures(getGameProfile(disguise));
    }

    public static GameProfile failCache(String name) {
        return failCache.computeIfAbsent(name, (d) -> new GameProfile(UUID.randomUUID(), name));
    }
}