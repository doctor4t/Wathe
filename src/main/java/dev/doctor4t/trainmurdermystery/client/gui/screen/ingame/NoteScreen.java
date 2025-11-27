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
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public class NoteScreen extends Screen {
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
        if (this.client == null) return;
        this.selectionManager = new SelectionManager(
                () -> this.text[this.currentRow],
                this::setCurrentRowMessage,
                SelectionManager.makeClipboardGetter(this.client),
                SelectionManager.makeClipboardSetter(this.client),
                string -> this.client.textRenderer.getWidth(string) <= 90
        );
        if (this.client.player == null) return;
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
        if (this.client == null || this.client.player == null || this.selectionManager == null) return;
        context.getMatrices().translate(0.0F, 0.0F, 4.0F);
        Vector3f vector3f = this.getTextScale();
        context.getMatrices().scale(vector3f.x(), vector3f.y(), vector3f.z());
        context.getMatrices().push();
        float scale = 8f;
        context.getMatrices().scale(scale, scale, scale);
        context.getMatrices().translate(-8, -4, 0);
        context.drawGuiTexture(TMM.id("gui/note"), 0, 0, 16, 16);
        context.getMatrices().pop();
        context.getMatrices().translate(0, 45, 0);
        int i = DyeColor.BLACK.getSignColor();
        boolean bl = this.client != null && this.client.player != null && this.client.player.age / 6 % 2 == 0;
        int j = this.selectionManager.getSelectionStart();
        int k = this.selectionManager.getSelectionEnd();
        int l = 4 * 10 / 2;
        int m = this.currentRow * 10 - l;
        for (int n = 0; n < this.text.length; n++) {
            String string = this.text[n];
            if (string == null) continue;
            if (this.textRenderer.isRightToLeft()) string = this.textRenderer.mirror(string);
            int o = -this.textRenderer.getWidth(string) / 2;
            context.drawText(this.textRenderer, string, o, n * 10 - l, i, false);
            if (n != this.currentRow || j < 0 || !bl) continue;
            int p = this.textRenderer.getWidth(string.substring(0, Math.min(j, string.length())));
            int q = p - this.textRenderer.getWidth(string) / 2;
            if (j >= string.length()) context.drawText(this.textRenderer, "_", q, m, i, false);
        }
        for (int nx = 0; nx < this.text.length; nx++) {
            String string = this.text[nx];
            if (string == null || nx != this.currentRow || j < 0) continue;
            int o = this.textRenderer.getWidth(string.substring(0, Math.min(j, string.length())));
            int p = o - this.textRenderer.getWidth(string) / 2;
            if (bl && j < string.length()) context.fill(p, m - 1, p + 1, m + 10, Colors.BLACK | i);
            if (k == j) continue;
            int q = Math.min(j, k);
            int r = Math.max(j, k);
            int s = this.textRenderer.getWidth(string.substring(0, q)) - this.textRenderer.getWidth(string) / 2;
            int t = this.textRenderer.getWidth(string.substring(0, r)) - this.textRenderer.getWidth(string) / 2;
            int u = Math.min(s, t);
            int v = Math.max(s, t);
            context.fill(RenderLayer.getGuiTextHighlight(), u, m, v, m + 10, Colors.BLUE);
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