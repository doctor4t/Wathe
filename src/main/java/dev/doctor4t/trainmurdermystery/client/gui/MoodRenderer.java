package dev.doctor4t.trainmurdermystery.client.gui;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.Role;
import dev.doctor4t.trainmurdermystery.api.TMMGameModes;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerMoodComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerPsychoComponent;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MoodRenderer {
    public static final Identifier ARROW_UP = TMM.id("hud/arrow_up");
    public static final Identifier ARROW_DOWN = TMM.id("hud/arrow_down");
    public static final Identifier MOOD_HAPPY = TMM.id("hud/mood_happy");
    public static final Identifier MOOD_MID = TMM.id("hud/mood_mid");
    public static final Identifier MOOD_DEPRESSIVE = TMM.id("hud/mood_depressive");
    public static final Identifier MOOD_KILLER = TMM.id("hud/mood_killer");
    public static final Identifier MOOD_PSYCHO = TMM.id("hud/mood_psycho");
    public static final Identifier MOOD_PSYCHO_HIT = TMM.id("hud/mood_psycho_hit");
    public static final Identifier MOOD_PSYCHO_EYES = TMM.id("hud/mood_psycho_eyes");
    private static final Map<PlayerMoodComponent.Task, TaskRenderer> renderers = new HashMap<>();
    public static Random random = new Random();
    public static float arrowProgress = 1f;
    public static float moodRender = 0f;
    public static float moodOffset = 0f;
    public static float moodTextWidth = 0f;
    public static float moodAlpha = 0f;

    @Environment(EnvType.CLIENT)
    public static void renderHud(@NotNull PlayerEntity player, TextRenderer textRenderer, DrawContext context, RenderTickCounter tickCounter) {
        GameWorldComponent gameWorldComponent = GameWorldComponent.KEY.get(player.getWorld());
        if (!gameWorldComponent.isRunning() || !TMMClient.isPlayerAliveAndInSurvival() || gameWorldComponent.getGameMode() != TMMGameModes.MURDER)
            return;
        var component = PlayerMoodComponent.KEY.get(player);
        var oldMood = moodRender;
        moodRender = MathHelper.lerp(tickCounter.getTickDelta(true) / 8, moodRender, component.getMood());
        moodAlpha = MathHelper.lerp(tickCounter.getTickDelta(true) / 16, moodAlpha, renderers.isEmpty() ? 0f : 1f);
        var psycho = PlayerPsychoComponent.KEY.get(player);
        if (psycho.getPsychoTicks() > 0) {
            renderPsycho(player, textRenderer, context, psycho, tickCounter);
            return;
        }
        for (var task : component.tasks.keySet()) {
            if (!renderers.containsKey(task)) {
                for (var renderer : renderers.values()) renderer.index++;
                renderers.put(task, new TaskRenderer());
            }
        }
        var toRemove = new ArrayList<PlayerMoodComponent.Task>();
        for (var taskType : PlayerMoodComponent.Task.values()) {
            var task = renderers.get(taskType);
            if (task != null) {
                task.present = false;
                if (task.tick(component.tasks.get(taskType), tickCounter.getTickDelta(true))) toRemove.add(taskType);
            }
        }
        for (var task : toRemove) renderers.remove(task);
        if (!toRemove.isEmpty()) {
            var renderersList = new ArrayList<>(renderers.values());
            renderersList.sort((a, b) -> Float.compare(a.offset, b.offset));
            for (var i = 0; i < renderersList.size(); i++) renderersList.get(i).index = i;
        }
        TaskRenderer maxRenderer = null;
        for (var entry : renderers.entrySet()) {
            var renderer = entry.getValue();
            context.getMatrices().push();
            context.getMatrices().translate(0, 10 * renderer.offset, 0);
            context.drawTextWithShadow(textRenderer, renderer.text, 22, 6, MathHelper.packRgb(1f, 1f, 1f) | ((int) (renderer.alpha * 255) << 24));
            context.getMatrices().pop();
            if (maxRenderer == null || renderer.offset > maxRenderer.offset) maxRenderer = renderer;
        }
        if (maxRenderer != null) {
            moodOffset = MathHelper.lerp(tickCounter.getTickDelta(true) / 8, moodOffset, maxRenderer.offset);
            moodTextWidth = MathHelper.lerp(tickCounter.getTickDelta(true) / 32, moodTextWidth, textRenderer.getWidth(maxRenderer.text));
        }
        Role role = gameWorldComponent.getRole(player);
        if (role != null) {
            if (role.getMoodType() == Role.MoodType.FAKE) {
                renderKiller(textRenderer, context);
            } else if (role.getMoodType() == Role.MoodType.REAL) {
                renderCivilian(textRenderer, context, oldMood);
            }
        }
        arrowProgress = MathHelper.lerp(tickCounter.getTickDelta(true) / 24, arrowProgress, 0f);
    }

    private static void renderCivilian(@NotNull TextRenderer textRenderer, @NotNull DrawContext context, float prevMood) {
        context.getMatrices().push();
        context.getMatrices().translate(0, 3 * moodOffset, 0);
        var mood = MOOD_HAPPY;
        if (moodRender < GameConstants.DEPRESSIVE_MOOD_THRESHOLD) {
            mood = MOOD_DEPRESSIVE;
        } else if (moodRender < GameConstants.MID_MOOD_THRESHOLD) {
            mood = MOOD_MID;
        }
        if (arrowProgress < 0.1f) {
            if (prevMood >= GameConstants.DEPRESSIVE_MOOD_THRESHOLD && moodRender < GameConstants.DEPRESSIVE_MOOD_THRESHOLD) {
                arrowProgress = -1f;
            } else if (prevMood >= GameConstants.MID_MOOD_THRESHOLD && moodRender < GameConstants.MID_MOOD_THRESHOLD) {
                arrowProgress = -1f;
            }
        }
        context.drawGuiTexture(mood, 5, 6, 14, 17);
        if (Math.abs(arrowProgress) > 0.01f) {
            var up = arrowProgress > 0;
            var arrow = up ? ARROW_UP : ARROW_DOWN;
            context.getMatrices().push();
            if (!up) context.getMatrices().translate(0, 4, 0);
            context.getMatrices().translate(0, arrowProgress * 4, 0);
            context.drawSprite(7, 6, 0, 10, 13, context.guiAtlasManager.getSprite(arrow), 1f, 1f, 1f, (float) Math.sin(Math.abs(arrowProgress) * Math.PI));
            context.getMatrices().pop();
        }
        context.getMatrices().pop();
        context.getMatrices().push();
        context.getMatrices().translate(0, 10 * moodOffset, 0);
        context.getMatrices().translate(26, 8 + textRenderer.fontHeight, 0);
        context.getMatrices().scale((moodTextWidth - 8) * moodRender, 1, 1);
        context.fill(0, 0, 1, 1, MathHelper.hsvToRgb(moodRender / 3.0F, 1.0F, 1.0F) | ((int) (moodAlpha * 255) << 24));
        context.getMatrices().pop();
    }

    private static void renderKiller(@NotNull TextRenderer textRenderer, @NotNull DrawContext context) {
        context.getMatrices().push();
        context.getMatrices().translate(0, 3 * moodOffset, 0);
        context.drawGuiTexture(MOOD_KILLER, 5, 6, 14, 17);
        context.getMatrices().pop();
        context.getMatrices().push();
        context.getMatrices().translate(0, 10 * moodOffset, 0);
        context.getMatrices().translate(26, 8 + textRenderer.fontHeight, 0);
        context.getMatrices().scale((moodTextWidth - 8) * moodRender, 1, 1);
        context.fill(0, 0, 1, 1, MathHelper.hsvToRgb(0F, 1.0F, 0.6F) | ((int) (moodAlpha * 255) << 24));
        context.getMatrices().pop();
    }

    private static void renderPsycho(@NotNull PlayerEntity player, @NotNull TextRenderer renderer, @NotNull DrawContext context, PlayerPsychoComponent component, @NotNull RenderTickCounter tickCounter) {
        var colour = MathHelper.hsvToRgb(0F, 1.0F, 0.5F);
        var text = Text.translatable("game.psycho_mode.text").withColor(colour);
        var width = renderer.getWidth(text);
        random.setSeed(System.currentTimeMillis());

        context.getMatrices().push();
        context.getMatrices().translate(random.nextGaussian() / 3, random.nextGaussian() / 3, 0);
        context.enableScissor(22, 6, 180, 23);
        for (var i = -1; i <= 3; i++) {
            var value = 1 - ((player.age + tickCounter.getTickDelta(true)) / 64) % 1;
            context.getMatrices().push();
            context.getMatrices().translate(value * (width + 4), 6, 0);
            context.drawTextWithShadow(renderer, text, i * (width + 4), 0, colour | 255 << 24);
            context.getMatrices().pop();
        }
        context.disableScissor();
        context.getMatrices().pop();

        context.getMatrices().push();
        context.getMatrices().translate(random.nextGaussian() / 3, random.nextGaussian() / 3, 0);
        context.getMatrices().push();
        context.getMatrices().translate(26, 8 + renderer.fontHeight, 0);
        var duration = Math.max(1f, component.getPsychoTicks() - tickCounter.getTickDelta(true)) / GameConstants.PSYCHO_TIMER;
        context.getMatrices().scale(150 * duration, 1, 1);
        context.fill(0, 0, 1, 1, colour | ((int) (0.9f * 255) << 24));
        context.getMatrices().pop();
        context.getMatrices().pop();

        context.getMatrices().push();
        context.getMatrices().translate(random.nextGaussian() / 3, random.nextGaussian() / 3, 0);
        for (var i = 1; i <= 12; i++) {
            var tick = (player.age - i) * 40;
            if ((player.age - i) % 2 != 0) continue;
            random.setSeed(tick);
            var alpha = (12 - i) / 12f;
            context.getMatrices().push();
            var moodScale = 0.2f + (GameConstants.PSYCHO_MODE_ARMOUR - component.armour) * 0.8f;
            var eyeScale = 0.8f;
            context.getMatrices().translate(
                    (random.nextFloat() - random.nextFloat()) * moodScale * i,
                    (random.nextFloat() - random.nextFloat()) * moodScale * i, -i * 3);
            context.drawSprite(5, 6, 0, 14, 17, context.guiAtlasManager.getSprite(component.armour == GameConstants.PSYCHO_MODE_ARMOUR ? MOOD_PSYCHO : MOOD_PSYCHO_HIT), 1f, 1f, 1f, alpha);
            context.getMatrices().translate(
                    (random.nextFloat() - random.nextFloat()) * eyeScale * i,
                    (random.nextFloat() - random.nextFloat()) * eyeScale * i, 1);
            context.drawSprite(5, 6, 0, 14, 17, context.guiAtlasManager.getSprite(MOOD_PSYCHO_EYES), 1f, 1f, 1f, alpha);
            context.getMatrices().pop();
        }
        context.getMatrices().pop();
    }

    private static class TaskRenderer {
        public int index = 0;
        public float offset = -1f;
        public float alpha = 0.075f;
        public boolean present = false;
        public Text text = Text.empty();

        public boolean tick(PlayerMoodComponent.TrainTask present, float delta) {
            if (present != null)
                this.text = Text.translatable("task." + (TMMClient.isKiller() ? "fake" : "feel")).append(Text.translatable("task." + present.getName()));
            this.present = present != null;
            this.alpha = MathHelper.lerp(delta / 16, this.alpha, present != null ? 1f : 0f);
            this.offset = MathHelper.lerp(delta / 32, this.offset, this.index);
            return this.alpha < 0.075f || (((int) (this.alpha * 255.0f) << 24) & -67108864) == 0;
        }
    }
}