package dev.doctor4t.trainmurdermystery.client.gui;

import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerShopComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public final class StoreRenderer {
    public static MoneyNumberRenderer view = new MoneyNumberRenderer();
    public static float offsetDelta = 0f;

    private StoreRenderer() {
    }

    public static void renderHud(TextRenderer renderer, @NotNull ClientPlayerEntity player, @NotNull DrawContext context, float delta) {
        if (!GameWorldComponent.KEY.get(player.getWorld()).canUseKillerFeatures(player)) {
            return;
        }

        int balance = PlayerShopComponent.KEY.get(player).balance;
        if (view.getTarget() != balance) {
            offsetDelta = balance > view.getTarget() ? .6f : -.6f;
            view.setTarget(balance);
        }

        float red = offsetDelta > 0 ? 1f - offsetDelta : 1f;
        float green = offsetDelta < 0 ? 1f + offsetDelta : 1f;
        float blue = 1f - Math.abs(offsetDelta);
        int colour = MathHelper.packRgb(red, green, blue) | 0xFF000000;
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(context.getScaledWindowWidth() - 12, 6, 0);
        view.render(renderer, context, 0, 0, colour, delta);
        matrices.pop();
        offsetDelta = MathHelper.lerp(delta / 16, offsetDelta, 0f);
    }

    public static void tick() {
        view.tick();
    }

    public static class MoneyNumberRenderer {
        private final List<ScrollingDigit> digits = new ArrayList<>();
        private float target;

        public void setTarget(float target) {
            this.target = target;
            int length = String.valueOf(target).length();

            while (this.digits.size() < length) {
                this.digits.add(new ScrollingDigit(this.digits.isEmpty()));
            }

            for (int i = 0; i < this.digits.size(); i++) {
                if (i == 0) {
                    this.digits.get(i).setTarget((float) (target / Math.pow(10, i)));
                } else {
                    this.digits.get(i).setTarget((int) (target / Math.pow(10, i)));
                }
            }
        }

        public void tick() {
            this.digits.forEach(ScrollingDigit::tick);
        }

        public void render(TextRenderer renderer, @NotNull DrawContext context, int x, int y, int colour, float delta) {
            MatrixStack matrices = context.getMatrices();

            matrices.push();
            matrices.translate(x, y, 0);
            context.drawTextWithShadow(renderer, "\uE781", 0, 0, colour);
            int offset = -8;
            for (ScrollingDigit digit : this.digits) {
                matrices.push();
                matrices.translate(offset, 0, 0);
                digit.render(renderer, context, colour, delta);
                offset -= 8;
                matrices.pop();
            }
            matrices.pop();
        }

        public float getTarget() {
            return this.target;
        }
    }
}