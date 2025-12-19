package dev.doctor4t.wathe.mixin.client.ui;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.doctor4t.wathe.client.WatheClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TooltipBackgroundRenderer.class)
public abstract class TooltipBackgroundRendererMixin {
    @Shadow
    private static void renderHorizontalLine(DrawContext context, int x, int y, int width, int z, int color) {
    }

    @Shadow
    private static void renderRectangle(DrawContext context, int x, int y, int width, int height, int z, int color) {
    }

    @Shadow
    private static void renderVerticalLine(DrawContext context, int x, int y, int height, int z, int color) {
    }

    @Shadow
    private static void renderVerticalLine(DrawContext context, int x, int y, int height, int z, int startColor, int endColor) {
    }

    @Unique
    private static final int BACKGROUND_COLOR = 0xFF160902;
    @Unique
    private static final int START_Y_BORDER_COLOR = 0xFFC5A244;
    @Unique
    private static final int END_Y_BORDER_COLOR = 0xFF815A15;

    @WrapMethod(method = "render")
    private static void render(DrawContext context, int x, int y, int width, int height, int z, Operation<Void> original) {
        if (WatheClient.isPlayerAliveAndInSurvival()) {
            int i = x - 3;
            int j = y - 3;
            int k = width + 3 + 3;
            int l = height + 3 + 3;
            renderHorizontalLine(context, i, j - 1, k, z, BACKGROUND_COLOR);
            renderHorizontalLine(context, i, j + l, k, z, BACKGROUND_COLOR);
            renderRectangle(context, i, j, k, l, z, BACKGROUND_COLOR);
            renderVerticalLine(context, i - 1, j, l, z, BACKGROUND_COLOR);
            renderVerticalLine(context, i + k, j, l, z, BACKGROUND_COLOR);
            renderBorder(context, i, j + 1, k, l, z);
        } else {
            original.call(context, x, y, width, height, z);
        }
    }

    @Unique
    private static void renderBorder(DrawContext context, int x, int y, int width, int height, int z) {
        renderVerticalLine(context, x, y, height - 2, z, TooltipBackgroundRendererMixin.START_Y_BORDER_COLOR, TooltipBackgroundRendererMixin.END_Y_BORDER_COLOR);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, TooltipBackgroundRendererMixin.START_Y_BORDER_COLOR, TooltipBackgroundRendererMixin.END_Y_BORDER_COLOR);
        renderHorizontalLine(context, x, y - 1, width, z, TooltipBackgroundRendererMixin.START_Y_BORDER_COLOR);
        renderHorizontalLine(context, x, y - 1 + height - 1, width, z, TooltipBackgroundRendererMixin.END_Y_BORDER_COLOR);
    }
}
