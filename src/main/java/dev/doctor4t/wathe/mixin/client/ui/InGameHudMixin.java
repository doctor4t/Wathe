package dev.doctor4t.wathe.mixin.client.ui;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.ratatouille.client.lib.render.helpers.Easing;
import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.client.gui.*;
import dev.doctor4t.wathe.game.GameConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;
    @Unique
    private static final Identifier WATHE_HOTBAR_TEXTURE = Wathe.id("hud/hotbar");
    @Unique
    private static final Identifier WATHE_HOTBAR_SELECTION_TEXTURE = Wathe.id("hud/hotbar_selection");

    @Inject(method = "renderMainHud", at = @At("TAIL"))
    private void wathe$renderHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (WatheClient.trainComponent != null && WatheClient.trainComponent.hasHud()) {
            ClientPlayerEntity player = this.client.player;
            if (player == null) return;
            TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
            MoodRenderer.renderHud(player, renderer, context, tickCounter);
            RoleNameRenderer.renderHud(renderer, player, context, tickCounter);
            RoundTextRenderer.renderHud(renderer, player, context);
            if (MinecraftClient.getInstance().currentScreen == null)
                StoreRenderer.renderHud(renderer, player, context, tickCounter.getTickDelta(true));
            TimeRenderer.renderHud(renderer, player, context, tickCounter.getTickDelta(true));
            LobbyPlayersRenderer.renderHud(renderer, player, context);
        }
    }

    @WrapMethod(method = "renderCrosshair")
    private void wathe$renderHud(DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
        if (!WatheClient.isPlayerAliveAndInSurvival()) {
            original.call(context, tickCounter);
            return;
        }
        ClientPlayerEntity player = this.client.player;
        if (player == null) return;
        CrosshairRenderer.renderCrosshair(this.client, player, context, tickCounter);
    }

    @WrapMethod(method = "renderStatusBars")
    private void wathe$removeStatusBars(DrawContext context, Operation<Void> original) {
        if (!WatheClient.isPlayerAliveAndInSurvival()) {
            original.call(context);
        }
    }

    @WrapMethod(method = "renderExperienceBar")
    private void wathe$removeExperienceBar(DrawContext context, int x, Operation<Void> original) {
        if (!WatheClient.isPlayerAliveAndInSurvival()) {
            original.call(context, x);
        }
    }

    @WrapMethod(method = "renderPlayerList")
    private void wathe$removePlayerList(DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
        if (!WatheClient.isPlayerAliveAndInSurvival()) original.call(context, tickCounter);
    }

    @WrapMethod(method = "renderExperienceLevel")
    private void wathe$removeExperienceLevel(DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
        if (!WatheClient.isPlayerAliveAndInSurvival()) {
            original.call(context, tickCounter);
        }
    }

    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0))
    private void wathe$overrideHotbarTexture(DrawContext instance, Identifier texture, int x, int y, int width, int height, @NotNull Operation<Void> original) {
        original.call(instance, WatheClient.isPlayerAliveAndInSurvival() ? WATHE_HOTBAR_TEXTURE : texture, x, y, width, height);
    }

    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 1))
    private void wathe$overrideHotbarSelectionTexture(DrawContext instance, Identifier texture, int x, int y, int width, int height, @NotNull Operation<Void> original) {
        original.call(instance, WatheClient.isPlayerAliveAndInSurvival() ? WATHE_HOTBAR_SELECTION_TEXTURE : texture, x, y, width, height);
    }

    @WrapMethod(method = "renderMiscOverlays")
    private void wathe$moveSleepOverlayToUnderUI(DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
        // sleep overlay
        if (this.client.player != null && this.client.player.getSleepTimer() > 0) {
            this.client.getProfiler().push("sleep");

            float f = (float) this.client.player.getSleepTimer();

            float g = Math.min(1, f / 30f);

            if (f > 100f) {
                g = 1 - (f - 100f) / 10f;
            }

            float fadeAlpha = MathHelper.lerp(MathHelper.clamp(Easing.SINE_IN.ease(g, 0, 1, 1), 0, 1), 0f, 1f);
            Color color = new Color(0.04f, 0f, 0.08f, fadeAlpha);
            context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), color.getRGB());

            this.client.getProfiler().pop();
        }
    }

    @WrapMethod(method = "renderSleepOverlay")
    private void wathe$removeSleepOverlayAndDoGameFade(DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
        if (WatheClient.gameComponent != null) {
            // game start / stop fade in / out
            float fadeIn = WatheClient.gameComponent.getFade();
            if (fadeIn >= 0) {
                this.client.getProfiler().push("watheFade");
                float fadeAlpha = MathHelper.lerp(Math.min(fadeIn / GameConstants.FADE_TIME, 1), 0f, 1f);
                Color color = new Color(0f, 0f, 0f, fadeAlpha);

                context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), color.getRGB());
                this.client.getProfiler().pop();
            }
        }
    }
}
