package dev.doctor4t.trainmurdermystery.client.gui;

import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerShopComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StoreRenderer {
    public static MoneyNumberRenderer view = new MoneyNumberRenderer();
    public static float offsetDelta = 0f;

    public static void renderHud(TextRenderer renderer, @NotNull ClientPlayerEntity player, @NotNull DrawContext context, float delta) {
        if (!GameWorldComponent.KEY.get(player.getWorld()).canUseKillerFeatures(player)) return;
        var balance = PlayerShopComponent.KEY.get(player).balance;
        if (view.getTarget() != balance) {
            offsetDelta = balance > view.getTarget() ? .6f : -.6f;
            view.setTarget(balance);
        }
        var r = offsetDelta > 0 ? 1f - offsetDelta : 1f;
        var g = offsetDelta < 0 ? 1f + offsetDelta : 1f;
        var b = 1f - Math.abs(offsetDelta);
        var colour = MathHelper.packRgb(r, g, b) | 0xFF000000;
        context.getMatrices().push();
        context.getMatrices().translate(context.getScaledWindowWidth() - 12, 6, 0);
        view.render(renderer, context, 0, 0, colour, delta);
        context.getMatrices().pop();
        offsetDelta = MathHelper.lerp(delta / 16, offsetDelta, 0f);
    }

    public static void tick() {
        view.update();
    }

    public static class MoneyNumberRenderer {
        private final List<ScrollingDigit> digits = new ArrayList<>();
        private float target;

        public void setTarget(float target) {
            this.target = target;
            var length = String.valueOf(target).length();
            while (this.digits.size() < length) this.digits.add(new ScrollingDigit(this.digits.isEmpty()));
            for (var i = 0; i < this.digits.size(); i++) {
                if (i == 0) {
                    this.digits.get(i).setTarget((float) (target / Math.pow(10, i)));
                } else {
                    this.digits.get(i).setTarget((int) (target / Math.pow(10, i)));
                }
            }
        }

        public void update() {
            for (var digit : this.digits) digit.update();
        }

        public void render(TextRenderer renderer, @NotNull DrawContext context, int x, int y, int colour, float delta) {
            context.getMatrices().push();
            context.getMatrices().translate(x, y, 0);
            context.drawTextWithShadow(renderer, "\uE781", 0, 0, colour);
            var offset = -8;
            for (var digit : this.digits) {
                context.getMatrices().push();
                context.getMatrices().translate(offset, 0, 0);
                digit.render(renderer, context, colour, delta);
                offset -= 8;
                context.getMatrices().pop();
            }
            context.getMatrices().pop();
        }

        public float getTarget() {
            return this.target;
        }
    }

    public static class ScrollingDigit {
        private final boolean force;
        private float target;
        private float value;
        private float lastValue;

        public ScrollingDigit(boolean force) {
            this.force = force;
        }

        public void update() {
            this.lastValue = this.value;
            this.value = MathHelper.lerp(0.15f, this.value, this.target);
            if (Math.abs(this.value - this.target) < 0.01f) this.value = this.target;
        }

        public void render(@NotNull TextRenderer renderer, @NotNull DrawContext context, int colour, float delta) {
            if (MathHelper.floor(this.lastValue) != MathHelper.floor(this.value)) {
                var player = MinecraftClient.getInstance().player;
//                if (player != null)player.getWorld().playSound(player, player.getX(), player.getY(), player.getZ(), TMMSounds.BALANCE_CLICK, SoundCategory.PLAYERS, 0.1f, 1 + this.lastValue - this.value, player.getRandom().nextLong());
            }
            var value = MathHelper.lerp(delta, this.lastValue, this.value);
            var digit = MathHelper.floor(value) % 10;
            var digitNext = MathHelper.floor(value + 1) % 10;
            var offset = value % 1;
            colour &= 0xFFFFFF;
            context.getMatrices().push();
            context.getMatrices().translate(0, -offset * (renderer.fontHeight + 2), 0);
            var alpha = (1.0f - Math.abs(offset)) * 255.0f;
            if (value < 1 && !this.force) alpha *= value;
            var baseColour = colour | (int) alpha << 24;
            var nextColour = colour | (int) (Math.abs(offset) * 255.0f) << 24;
            if ((baseColour & -67108864) != 0)
                context.drawTextWithShadow(renderer, String.valueOf(digit), 0, 0, baseColour);
            if ((nextColour & -67108864) != 0)
                context.drawTextWithShadow(renderer, String.valueOf(digitNext), 0, renderer.fontHeight + 2, nextColour);
            context.getMatrices().pop();
        }

        public void setTarget(float target) {
            this.target = target;
        }
    }
}