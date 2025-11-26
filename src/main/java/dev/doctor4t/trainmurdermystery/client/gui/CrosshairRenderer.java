package dev.doctor4t.trainmurdermystery.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.item.DerringerItem;
import dev.doctor4t.trainmurdermystery.item.KnifeItem;
import dev.doctor4t.trainmurdermystery.item.RevolverItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class CrosshairRenderer {
    private static final Identifier CROSSHAIR = TMM.id("hud/crosshair");
    private static final Identifier CROSSHAIR_TARGET = TMM.id("hud/crosshair_target");
    private static final Identifier KNIFE_ATTACK = TMM.id("hud/knife_attack");
    private static final Identifier KNIFE_PROGRESS = TMM.id("hud/knife_progress");
    private static final Identifier KNIFE_BACKGROUND = TMM.id("hud/knife_background");
    private static final Identifier BAT_ATTACK = TMM.id("hud/bat_attack");
    private static final Identifier BAT_PROGRESS = TMM.id("hud/bat_progress");
    private static final Identifier BAT_BACKGROUND = TMM.id("hud/bat_background");


    public static void renderCrosshair(@NotNull MinecraftClient client, @NotNull ClientPlayerEntity player, DrawContext context, RenderTickCounter tickCounter) {
        if (!client.options.getPerspective().isFirstPerson()) return;
        boolean target = false;
        context.getMatrices().push();
        context.getMatrices().translate(context.getScaledWindowWidth() / 2f, context.getScaledWindowHeight() / 2f, 0);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        ItemStack mainHandStack = player.getMainHandStack();
        if (mainHandStack.isOf(TMMItems.REVOLVER) && !player.getItemCooldownManager().isCoolingDown(mainHandStack.getItem()) && RevolverItem.getGunTarget(player) instanceof EntityHitResult) {
            target = true;
        } else if (mainHandStack.isOf(TMMItems.DERRINGER) && !player.getItemCooldownManager().isCoolingDown(mainHandStack.getItem()) && DerringerItem.getGunTarget(player) instanceof EntityHitResult) {
            target = true;
        } else if (mainHandStack.isOf(TMMItems.KNIFE)) {
            ItemCooldownManager manager = player.getItemCooldownManager();
            if (!manager.isCoolingDown(TMMItems.KNIFE) && KnifeItem.getKnifeTarget(player) instanceof EntityHitResult) {
                target = true;
                context.drawGuiTexture(KNIFE_ATTACK, -5, 5, 10, 7);
            } else {
                float f = 1 - manager.getCooldownProgress(TMMItems.KNIFE, tickCounter.getTickDelta(true));
                context.drawGuiTexture(KNIFE_BACKGROUND, -5, 5, 10, 7);
                context.drawGuiTexture(KNIFE_PROGRESS, 10, 7, 0, 0, -5, 5, (int) (f * 10.0f), 7);
            }
        } else if (mainHandStack.isOf(TMMItems.BAT)) {
            if (player.getAttackCooldownProgress(tickCounter.getTickDelta(true)) >= 1f && client.crosshairTarget instanceof EntityHitResult result && result.getEntity() instanceof PlayerEntity) {
                target = true;
                context.drawGuiTexture(BAT_ATTACK, -5, 5, 10, 7);
            } else {
                float f = player.getAttackCooldownProgress(tickCounter.getTickDelta(true));
                context.drawGuiTexture(BAT_BACKGROUND, -5, 5, 10, 7);
                context.drawGuiTexture(BAT_PROGRESS, 10, 7, 0, 0, -5, 5, (int) (f * 10.0f), 7);
            }
        }
        context.getMatrices().push();
        context.getMatrices().translate(-1.5f, -1.5f, 0);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR, GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        if (target) {
            context.drawGuiTexture(CROSSHAIR_TARGET, 0, 0, 3, 3);
        } else {
            context.drawGuiTexture(CROSSHAIR, 0, 0, 3, 3);
        }
        context.getMatrices().pop();
        context.getMatrices().pop();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }
}