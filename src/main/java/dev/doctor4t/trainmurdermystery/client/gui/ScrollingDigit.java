package dev.doctor4t.trainmurdermystery.client.gui;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author AmyMialee
 * @author RAT
 */
@SuppressWarnings("unused")
public class ScrollingDigit {

    private final int power;
    private final boolean cap6;
    private final boolean force;
    private float target;
    private float value;
    private float lastValue;
    private Consumer<ScrollingDigit> tickCallback;

    public ScrollingDigit(boolean force) {
        this(force, 1, false);
    }

    public ScrollingDigit(int power, boolean cap6) {
        this(true, power, cap6);
    }

    public ScrollingDigit(boolean force, int power, boolean cap6) {
        this.force = force;
        this.power = power;
        this.cap6 = cap6;
    }

    public void tick() {
        this.lastValue = this.value;
        this.value = MathHelper.lerp(0.15f, this.value, this.target);
        if (Math.abs(this.value - this.target) < 0.01f) {
            this.value = this.target;
        }
        // now you can play the update sound or whatever - SkyNotTheLimit
        if (this.tickCallback != null) {
            this.tickCallback.accept(this);
        }
    }

    public void render(@NotNull TextRenderer renderer, @NotNull DrawContext context, int colour, float delta) {
        float value = MathHelper.lerp(delta, this.lastValue, this.value);
        int digit = MathHelper.floor(value) % (this.cap6 ? 6 : 10);
        int digitNext = MathHelper.floor(value + 1) % (this.cap6 ? 6 : 10);

        double offset;
        if (this.power != 1) {
            offset = Math.pow(value % 1, this.power);
        } else {
            offset = value % 1;
        }

        colour &= 0xFFFFFF;
        MatrixStack matrices = context.getMatrices();

        matrices.push();
        matrices.translate(0, -offset * (renderer.fontHeight + 2), 0);
        double alpha = (1.0f - Math.abs(offset)) * 255.0f;

        if (value < 1 && !this.force) {
            alpha *= value;
        }

        int baseColour = colour | (int) alpha << 24;
        int nextColour = colour | (int) (Math.abs(offset) * 255.0f) << 24;

        if ((baseColour & -67108864) != 0){
            context.drawTextWithShadow(renderer, String.valueOf(digit), 0, 0, baseColour);
        }
        if ((nextColour & -67108864) != 0) {
            context.drawTextWithShadow(renderer, String.valueOf(digitNext), 0, renderer.fontHeight + 2, nextColour);
        }

        matrices.pop();
    }

    public void setTarget(float target) {
        this.target = target;
    }

    public float getLastValue() {
        return this.lastValue;
    }

    public float getValue() {
        return this.value;
    }

    public float getTarget() {
        return this.target;
    }

    public void setTickCallback(Consumer<ScrollingDigit> callback) {
        this.tickCallback = callback;
    }
}
