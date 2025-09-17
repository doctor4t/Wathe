package dev.doctor4t.trainmurdermystery.mixin.client.ui;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.doctor4t.trainmurdermystery.TrainMurderMystery;
import dev.doctor4t.trainmurdermystery.client.TrainMurderMysteryClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow @Final private MinecraftClient client;
    private static final Identifier TMM_HOTBAR_TEXTURE = TrainMurderMystery.id("hud/hotbar");
    private static final Identifier TMM_HOTBAR_SELECTION_TEXTURE = TrainMurderMystery.id("hud/hotbar_selection");

    @WrapMethod(method = "renderStatusBars")
    private void tmm$removeStatusBars(DrawContext context, Operation<Void> original) {
        if (!TrainMurderMysteryClient.isPlayerAliveAndInSurvival()) {
            original.call(context);
        }
    }

    @WrapMethod(method = "renderExperienceBar")
    private void tmm$removeExperienceBar(DrawContext context, int x, Operation<Void> original) {
        if (!TrainMurderMysteryClient.isPlayerAliveAndInSurvival()) {
            original.call(context, x);
        }
    }

    @WrapMethod(method = "renderExperienceLevel")
    private void tmm$removeExperienceLevel(DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
        if (!TrainMurderMysteryClient.isPlayerAliveAndInSurvival()) {
            original.call(context, tickCounter);
        }
    }

    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 0))
    private void tmm$overrideHotbarTexture(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        original.call(instance, TrainMurderMysteryClient.isPlayerAliveAndInSurvival() ? TMM_HOTBAR_TEXTURE : texture, x, y, width, height);
    }

    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 1))
    private void tmm$overrideHotbarSelectionTexture(DrawContext instance, Identifier texture, int x, int y, int width, int height, Operation<Void> original) {
        original.call(instance, TrainMurderMysteryClient.isPlayerAliveAndInSurvival() ? TMM_HOTBAR_SELECTION_TEXTURE : texture, x, y, width, height);
    }

    @WrapMethod(method = "renderSleepOverlay")
    private void tmm$overrideSleepOverlay(DrawContext context, RenderTickCounter tickCounter, Operation<Void> original) {
        if (this.client.player.getSleepTimer() > 0) {
            this.client.getProfiler().push("sleep");
            float f = (float) this.client.player.getSleepTimer();
            float g = f / 100.0F;
            if (g > 1.0F) {
                g = 1.0F - (f - 100.0F);
            }

            int i = (int) (255.0F * g) << 24 | 1052704;
            context.fill(RenderLayer.getGuiOverlay(), 0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), i);
            this.client.getProfiler().pop();
        }
    }
}
