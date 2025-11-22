package dev.doctor4t.trainmurdermystery.client.gui.screen.ingame;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.cca.PlayerNoteComponent;
import dev.doctor4t.trainmurdermystery.util.NoteEditPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class NoteScreen extends Screen {
    public static final Identifier NOTE_GUI = TMM.id("gui/note");
    private final String[] text = new String[]{"", "", "", ""};
    private int currentRow;

    private @Nullable SelectionManager selectionManager;

    public NoteScreen() {
        super(Text.literal("Edit Note"));
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("tmm.gui.reset"), button -> this.resetEditing()).dimensions(this.width / 2 - 100, this.height / 4 + 144, 98, 20).build());
        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> this.finishEditing()).dimensions(this.width / 2 + 2, this.height / 4 + 144, 98, 20).build());

        if (this.client == null) {
            return;
        }

        this.selectionManager = new SelectionManager(
                () -> this.text[this.currentRow],
                this::setCurrentRowMessage,
                SelectionManager.makeClipboardGetter(this.client),
                SelectionManager.makeClipboardSetter(this.client),
                string -> this.client.textRenderer.getWidth(string) <= 90
        );

        if (this.client.player == null) {
            return;
        }

        PlayerNoteComponent component = PlayerNoteComponent.KEY.get(this.client.player);
        System.arraycopy(component.text, 0, this.text, 0, Math.min(component.text.length, this.text.length));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.selectionManager == null) return false;
        if (keyCode == GLFW.GLFW_KEY_UP) {
            this.currentRow = this.currentRow - 1 & 3;
            this.selectionManager.putCursorAtEnd();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_DOWN || keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            this.currentRow = this.currentRow + 1 & 3;
            this.selectionManager.putCursorAtEnd();
            return true;
        }
        return this.selectionManager.handleSpecialKey(keyCode) || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (this.selectionManager != null) this.selectionManager.insert(chr);
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        DiffuseLighting.disableGuiDepthLighting();
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 40, 16777215);
        this.renderSign(context);
        DiffuseLighting.enableGuiDepthLighting();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderInGameBackground(context);
    }

    @Override
    public void close() {
        this.finishEditing();
    }

    @Override
    public void removed() {
        ClientPlayNetworking.send(new NoteEditPayload(this.text[0], this.text[1], this.text[2], this.text[3]));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    protected Vector3f getTextScale() {
        return new Vector3f(0.9765628F, 0.9765628F, 0.9765628F);
    }

    private void renderSign(@NotNull DrawContext context) {
        context.getMatrices().push();
        context.getMatrices().translate((float) this.width / 2.0F, 90.0F, 50.0F);
        this.renderSignText(context);
        context.getMatrices().pop();
    }

    private void renderSignText(@NotNull DrawContext context) {
        if (this.client == null || this.client.player == null || this.selectionManager == null) {
            return;
        }

        MatrixStack matrices = context.getMatrices();

        matrices.translate(0.0F, 0.0F, 4.0F);
        Vector3f textScale = this.getTextScale();
        matrices.scale(textScale.x(), textScale.y(), textScale.z());

        matrices.push();

        final float scale = 8f;
        matrices.scale(scale, scale, scale);

        matrices.translate(-8, -4, 0);
        context.drawGuiTexture(NOTE_GUI, 0, 0, 16, 16);

        matrices.pop();

        matrices.translate(0, 45, 0);

        // NAME YOUR VARIABLES BETTER PLEASE
        final int black = DyeColor.BLACK.getSignColor();
        boolean bl = this.client != null && this.client.player != null && this.client.player.age / 6 % 2 == 0;
        int start = this.selectionManager.getSelectionStart();
        int end = this.selectionManager.getSelectionEnd();
        int l = 4 * 10 / 2;
        int m = this.currentRow * 10 - l;

        for (int n = 0; n < this.text.length; n++) {
            String line = this.text[n];

            if (line == null) {
                continue;
            }

            if (this.textRenderer.isRightToLeft()) {
                line = this.textRenderer.mirror(line);
            }

            int o = -this.textRenderer.getWidth(line) / 2;
            context.drawText(this.textRenderer, line, o, n * 10 - l, black, false);

            if (n != this.currentRow || start < 0 || !bl) {
                continue;
            }

            int p = this.textRenderer.getWidth(line.substring(0, Math.min(start, line.length())));
            int q = p - this.textRenderer.getWidth(line) / 2;

            if (start >= line.length()) {
                context.drawText(this.textRenderer, "_", q, m, black, false);
            }
        }

        RenderLayer guiTextHighlight = RenderLayer.getGuiTextHighlight();

        for (int nx = 0; nx < this.text.length; nx++) {
            String line = this.text[nx];

            if (line == null || nx != this.currentRow || start < 0) {
                continue;
            }

            int o = this.textRenderer.getWidth(line.substring(0, Math.min(start, line.length())));
            int p = o - this.textRenderer.getWidth(line) / 2;

            if (bl && start < line.length()) {
                context.fill(p, m - 1, p + 1, m + 10, Colors.BLACK | black);
            }

            if (end == start) {
                continue;
            }

            int q = Math.min(start, end);
            int r = Math.max(start, end);
            int s = this.textRenderer.getWidth(line.substring(0, q)) - this.textRenderer.getWidth(line) / 2;
            int t = this.textRenderer.getWidth(line.substring(0, r)) - this.textRenderer.getWidth(line) / 2;
            int u = Math.min(s, t);
            int v = Math.max(s, t);
            context.fill(guiTextHighlight, u, m, v, m + 10, Colors.BLUE);
        }
    }

    private void setCurrentRowMessage(String message) {
        this.text[this.currentRow] = message;
        if (this.client == null || this.client.player == null) return;
        PlayerNoteComponent.KEY.get(this.client.player).setNote(this.text[0], this.text[1], this.text[2], this.text[3]);
    }

    private void resetEditing() {
        if (this.client == null || this.client.player == null) return;
        Arrays.fill(this.text, "");
        PlayerNoteComponent.KEY.get(this.client.player).setNote(this.text[0], this.text[1], this.text[2], this.text[3]);
    }

    private void finishEditing() {
        if (this.client != null) this.client.setScreen(null);
    }
}