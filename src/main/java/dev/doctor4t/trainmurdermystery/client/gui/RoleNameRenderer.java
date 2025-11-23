package dev.doctor4t.trainmurdermystery.client.gui;

import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerPsychoComponent;
import dev.doctor4t.trainmurdermystery.entity.NoteEntity;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public final class RoleNameRenderer {
    private static TrainRole targetRole = TrainRole.BYSTANDER;
    private static float nametagAlpha = 0f;
    private static float noteAlpha = 0f;
    private static Text nametag = Text.empty();
    private static final Text[] note = new Text[]{Text.empty(), Text.empty(), Text.empty(), Text.empty()};

    private RoleNameRenderer() {
    }

    public static void renderHud(TextRenderer renderer, @NotNull ClientPlayerEntity player, DrawContext context, RenderTickCounter tickCounter) {
        GameWorldComponent component = GameWorldComponent.KEY.get(player.getWorld());
        if (player.getWorld().getLightLevel(LightType.BLOCK, BlockPos.ofFloored(player.getEyePos())) < 3 && player.getWorld().getLightLevel(LightType.SKY, BlockPos.ofFloored(player.getEyePos())) < 10) {
            return;
        }

        float range = GameFunctions.isPlayerSpectatingOrCreative(player) ? 8f : 2f;
        findNametag(player, tickCounter, range, component);

        MatrixStack matrices = context.getMatrices();
        renderNametag(renderer, player, context, matrices, component);
        findNote(player, tickCounter, range);
        renderNote(renderer, context, matrices);
    }

    public static void findNametag(@NotNull ClientPlayerEntity player, RenderTickCounter tickCounter, float range, GameWorldComponent component) {
        Supplier<Float> fadeNametagOut = () -> MathHelper.lerp(tickCounter.getTickDelta(true) / 4, nametagAlpha, 0f);

        if (!(ProjectileUtil.getCollision(player, entity -> entity instanceof PlayerEntity, range) instanceof EntityHitResult entityHitResult)) {
            nametagAlpha = fadeNametagOut.get();
            return;
        }
        if (!(entityHitResult.getEntity() instanceof PlayerEntity target)) {
            nametagAlpha = fadeNametagOut.get();
            return;
        }

        nametagAlpha = MathHelper.lerp(tickCounter.getTickDelta(true) / 4, nametagAlpha, 1f);
        nametag = target.getDisplayName();
        if (component.canUseKillerFeatures(target)) {
            targetRole = TrainRole.KILLER;
        } else {
            targetRole = TrainRole.BYSTANDER;
        }

        boolean shouldObfuscate = PlayerPsychoComponent.KEY.get(target).getPsychoTicks() > 0;
        nametag = shouldObfuscate ? Text.literal("urscrewed" + "X".repeat(player.getRandom().nextInt(8))).styled(style -> style.withFormatting(Formatting.OBFUSCATED, Formatting.DARK_RED)) : nametag;
    }

    public static void renderNametag(TextRenderer renderer, @NotNull ClientPlayerEntity player, DrawContext context, MatrixStack matrices, GameWorldComponent component) {
        if (nametagAlpha <= 0.05f) {
            return;
        }

        matrices.push();
        matrices.translate(context.getScaledWindowWidth() / 2f, context.getScaledWindowHeight() / 2f + 6, 0);
        matrices.scale(0.6f, 0.6f, 1f);
        int nameWidth = renderer.getWidth(nametag);
        context.drawTextWithShadow(renderer, nametag, -nameWidth / 2, 16, MathHelper.packRgb(1f, 1f, 1f) | ((int) (nametagAlpha * 255) << 24));
        if (component.isRunning()) {
            TrainRole playerRole = TrainRole.BYSTANDER;
            if (component.canUseKillerFeatures(player)) {
                playerRole = TrainRole.KILLER;
            }
            if (playerRole == TrainRole.KILLER && targetRole == TrainRole.KILLER) {
                matrices.translate(0, 20 + renderer.fontHeight, 0);
                Text roleText = Text.translatable("game.tip.cohort");
                int roleWidth = renderer.getWidth(roleText);
                context.drawTextWithShadow(renderer, roleText, -roleWidth / 2, 0, MathHelper.packRgb(1f, 0f, 0f) | ((int) (nametagAlpha * 255) << 24));
            }
        }
        matrices.pop();
    }

    private static void findNote(@NotNull ClientPlayerEntity player, RenderTickCounter tickCounter, float range) {
        Supplier<Float> fadeNoteOut = () -> MathHelper.lerp(tickCounter.getTickDelta(true) / 4, noteAlpha, 0f);
        if (!(ProjectileUtil.getCollision(player, entity -> entity instanceof NoteEntity, range) instanceof EntityHitResult entityHitResult)) {
            noteAlpha = fadeNoteOut.get();
            return;
        }
        if (!(entityHitResult.getEntity() instanceof NoteEntity noteEntity)) {
            noteAlpha = fadeNoteOut.get();
            return;
        }
        noteAlpha = MathHelper.lerp(tickCounter.getTickDelta(true) / 4, noteAlpha, 1f);
        nametagAlpha = MathHelper.lerp(tickCounter.getTickDelta(true), nametagAlpha, 0f);
        RoleNameRenderer.note[0] = Text.literal(noteEntity.getLines()[0]);
        RoleNameRenderer.note[1] = Text.literal(noteEntity.getLines()[1]);
        RoleNameRenderer.note[2] = Text.literal(noteEntity.getLines()[2]);
        RoleNameRenderer.note[3] = Text.literal(noteEntity.getLines()[3]);
    }

    public static void renderNote(TextRenderer renderer, DrawContext context, MatrixStack matrices) {
        if (noteAlpha <= 0.05f) {
            return;
        }

        matrices.push();
        matrices.translate(context.getScaledWindowWidth() / 2f, context.getScaledWindowHeight() / 2f + 6, 0);
        matrices.scale(0.6f, 0.6f, 1f);
        for (int i = 0; i < note.length; i++) {
            Text line = note[i];
            int lineWidth = renderer.getWidth(line);
            context.drawTextWithShadow(renderer, line, -lineWidth / 2, 16 + (i * (renderer.fontHeight + 2)), MathHelper.packRgb(1f, 1f, 1f) | ((int) (noteAlpha * 255) << 24));
        }
        matrices.pop();
    }

    private enum TrainRole {
        KILLER,
        BYSTANDER
    }
}