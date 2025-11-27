package dev.doctor4t.trainmurdermystery.client.gui;

import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerPsychoComponent;
import dev.doctor4t.trainmurdermystery.entity.NoteEntity;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import org.jetbrains.annotations.NotNull;

public class RoleNameRenderer {
    private static TrainRole targetRole = TrainRole.BYSTANDER;
    private static float nametagAlpha = 0f;
    private static float noteAlpha = 0f;
    private static Text nametag = Text.empty();
    private static final Text[] note = new Text[]{Text.empty(), Text.empty(), Text.empty(), Text.empty()};

    public static void renderHud(TextRenderer renderer, @NotNull ClientPlayerEntity player, DrawContext context, RenderTickCounter tickCounter) {
        GameWorldComponent component = GameWorldComponent.KEY.get(player.getWorld());
        if (player.getWorld().getLightLevel(LightType.BLOCK, BlockPos.ofFloored(player.getEyePos())) < 3 && player.getWorld().getLightLevel(LightType.SKY, BlockPos.ofFloored(player.getEyePos())) < 10)
            return;
        float range = GameFunctions.isPlayerSpectatingOrCreative(player) ? 8f : 2f;
        if (ProjectileUtil.getCollision(player, entity -> entity instanceof PlayerEntity, range) instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof PlayerEntity target) {
            nametagAlpha = MathHelper.lerp(tickCounter.getTickDelta(true) / 4, nametagAlpha, 1f);
            nametag = target.getDisplayName();
            if (component.canUseKillerFeatures(target)) {
                targetRole = TrainRole.KILLER;
            } else {
                targetRole = TrainRole.BYSTANDER;
            }
            boolean shouldObfuscate = PlayerPsychoComponent.KEY.get(target).getPsychoTicks() > 0;
            nametag = shouldObfuscate ? Text.literal("urscrewed" + "X".repeat(player.getRandom().nextInt(8))).styled(style -> style.withFormatting(Formatting.OBFUSCATED, Formatting.DARK_RED)) : nametag;
        } else {
            nametagAlpha = MathHelper.lerp(tickCounter.getTickDelta(true) / 4, nametagAlpha, 0f);
        }
        if (nametagAlpha > 0.05f) {
            context.getMatrices().push();
            context.getMatrices().translate(context.getScaledWindowWidth() / 2f, context.getScaledWindowHeight() / 2f + 6, 0);
            context.getMatrices().scale(0.6f, 0.6f, 1f);
            int nameWidth = renderer.getWidth(nametag);
            context.drawTextWithShadow(renderer, nametag, -nameWidth / 2, 16, MathHelper.packRgb(1f, 1f, 1f) | ((int) (nametagAlpha * 255) << 24));
            if (component.isRunning()) {
                TrainRole playerRole = TrainRole.BYSTANDER;
                if (component.canUseKillerFeatures(player)) playerRole = TrainRole.KILLER;
                if (playerRole == TrainRole.KILLER && targetRole == TrainRole.KILLER) {
                    context.getMatrices().translate(0, 20 + renderer.fontHeight, 0);
                    MutableText roleText = Text.translatable("game.tip.cohort");
                    int roleWidth = renderer.getWidth(roleText);
                    context.drawTextWithShadow(renderer, roleText, -roleWidth / 2, 0, MathHelper.packRgb(1f, 0f, 0f) | ((int) (nametagAlpha * 255) << 24));
                }
            }
            context.getMatrices().pop();
        }
        if (ProjectileUtil.getCollision(player, entity -> entity instanceof NoteEntity, range) instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof NoteEntity note) {
            noteAlpha = MathHelper.lerp(tickCounter.getTickDelta(true) / 4, noteAlpha, 1f);
            nametagAlpha = MathHelper.lerp(tickCounter.getTickDelta(true), nametagAlpha, 0f);
            RoleNameRenderer.note[0] = Text.literal(note.getLines()[0]);
            RoleNameRenderer.note[1] = Text.literal(note.getLines()[1]);
            RoleNameRenderer.note[2] = Text.literal(note.getLines()[2]);
            RoleNameRenderer.note[3] = Text.literal(note.getLines()[3]);
        } else {
            noteAlpha = MathHelper.lerp(tickCounter.getTickDelta(true) / 4, noteAlpha, 0f);
        }
        if (noteAlpha > 0.05f) {
            context.getMatrices().push();
            context.getMatrices().translate(context.getScaledWindowWidth() / 2f, context.getScaledWindowHeight() / 2f + 6, 0);
            context.getMatrices().scale(0.6f, 0.6f, 1f);
            for (int i = 0; i < note.length; i++) {
                Text line = note[i];
                int lineWidth = renderer.getWidth(line);
                context.drawTextWithShadow(renderer, line, -lineWidth / 2, 16 + (i * (renderer.fontHeight + 2)), MathHelper.packRgb(1f, 1f, 1f) | ((int) (noteAlpha * 255) << 24));
            }
            context.getMatrices().pop();
        }
    }

    private enum TrainRole {
        KILLER,
        BYSTANDER
    }
}