package dev.doctor4t.trainmurdermystery.mixin.client.restrictions;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.SleepingChatScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SleepingChatScreen.class)
public abstract class SleepingChatScreenMixin extends ChatScreen {
    @Shadow
    private ButtonWidget stopSleepingButton;

    @Shadow
    protected abstract void stopSleeping();

    public SleepingChatScreenMixin(String originalChatText) {
        super(originalChatText);
    }

    @WrapMethod(method = "render")
    public void disableSleepChat(DrawContext context, int mouseX, int mouseY, float delta, Operation<Void> original) {
        if (TMMClient.isPlayerAliveAndInSurvival()) {
            return;
        }
        original.call(context, mouseX, mouseY, delta);
    }

    @WrapMethod(method = "render")
    public void onlyRenderStopSleepingButton(DrawContext context, int mouseX, int mouseY, float delta, Operation<Void> original) {
        this.stopSleepingButton.render(context, mouseX, mouseY, delta);
    }

    @WrapMethod(method = "charTyped")
    public boolean disableCharTyping(char chr, int modifiers, Operation<Boolean> original) {
        return false;
    }

    @WrapMethod(method = "keyPressed")
    public boolean disableKeyPressed(int keyCode, int scanCode, int modifiers, Operation<Boolean> original) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.stopSleeping();
        }

        return false;
    }

    @WrapMethod(method = "closeChatIfEmpty")
    public void alwaysCloseChatOnLeavingBed(Operation<Void> original) {
        this.client.setScreen(null);
    }
}
