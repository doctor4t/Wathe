package dev.doctor4t.trainmurdermystery.mixin.client.restrictions;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.doctor4t.trainmurdermystery.client.TrainMurderMysteryClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SleepingChatScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SleepingChatScreen.class)
public class SleepingChatScreenMixin {
    @WrapMethod(method = "render")
    public void tmm$disableSleepChat(DrawContext context, int mouseX, int mouseY, float delta, Operation<Void> original) {
        if (!TrainMurderMysteryClient.isPlayerAliveAndInSurvival()) {
            original.call(context, mouseX, mouseY, delta);
        }
    }
}
