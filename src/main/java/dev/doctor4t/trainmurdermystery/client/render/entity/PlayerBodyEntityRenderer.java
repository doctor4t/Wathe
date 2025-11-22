package dev.doctor4t.trainmurdermystery.client.render.entity;

import dev.doctor4t.ratatouille.client.lib.render.helpers.Easing;
import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.client.model.TMMModelLayers;
import dev.doctor4t.trainmurdermystery.client.model.entity.PlayerSkeletonEntityModel;
import dev.doctor4t.trainmurdermystery.entity.PlayerBodyEntity;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.awt.*;

public class PlayerBodyEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> extends LivingEntityRenderer<PlayerBodyEntity, PlayerEntityModel<PlayerBodyEntity>> {
    public static final Identifier DEFAULT_TEXTURE = TMM.id("textures/entity/player_body_default.png");
    private static final Identifier SKELETON_TEXTURE = TMM.id("textures/entity/player_skeleton.png");

    protected PlayerSkeletonEntityModel<PlayerBodyEntity> skeletonModel;

    public PlayerBodyEntityRenderer(EntityRendererFactory.Context ctx, boolean slim) {
        super(ctx, new PlayerEntityModel<>(ctx.getPart(slim ? TMMModelLayers.PLAYER_BODY_SLIM : TMMModelLayers.PLAYER_BODY), slim), 0F);
        skeletonModel = new PlayerSkeletonEntityModel<>(ctx.getPart(TMMModelLayers.PLAYER_SKELETON));
    }

    public void render(PlayerBodyEntity playerBodyEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        this.setModelPose();

        matrixStack.push();
        float clamp = MathHelper.clamp((float) (playerBodyEntity.age - GameConstants.TIME_TO_DECOMPOSITION) / GameConstants.DECOMPOSING_TIME, 0, GameConstants.TIME_TO_DECOMPOSITION + GameConstants.DECOMPOSING_TIME);
        float ease = Easing.CUBIC_IN.ease(clamp, 0, -1, 1);
        if (ease > -1) {
            matrixStack.translate(0, ease, 0);
            float alpha = TMMClient.moodComponent.isLowerThanDepressed() ? MathHelper.lerp(MathHelper.clamp(Easing.SINE_IN.ease(Math.min(1f, (float) playerBodyEntity.age / 100f), 0, 1, 1), 0, 1), 1f, 0f) : 1f;
            this.renderBody(playerBodyEntity, f, g, matrixStack, vertexConsumerProvider, light, alpha);
        }
        matrixStack.pop();

        renderSkeleton(playerBodyEntity, f, g, matrixStack, vertexConsumerProvider, light, TMMClient.moodComponent.isLowerThanDepressed() ? 0f : 1f);
    }

    public void renderBody(PlayerBodyEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, float alpha) {
        boolean bl = this.isVisible(livingEntity);
        MinecraftClient client = MinecraftClient.getInstance();
        boolean bl2 = !bl && !livingEntity.isInvisibleTo(client.player);
        boolean bl3 = client.hasOutline(livingEntity);
        RenderLayer bodyRenderLayer = this.getRenderLayer(livingEntity, bl, bl2, bl3);

        render(livingEntity, f, g, matrixStack, vertexConsumerProvider, light, this.model, bodyRenderLayer, 1f, alpha);
    }

    public void renderSkeleton(PlayerBodyEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, float alpha) {
        render(livingEntity, f, g, matrixStack, vertexConsumerProvider, light, this.skeletonModel, this.getSkeletonRenderLayer(), .95f, alpha);
    }

    public void render(PlayerBodyEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, BipedEntityModel<PlayerBodyEntity> model, RenderLayer renderLayer, float scale, float alpha) {
        if (alpha > 0) {
            matrixStack.push();
            this.model.handSwingProgress = this.getHandSwingProgress(livingEntity, g);
            this.model.riding = livingEntity.hasVehicle();
            this.model.child = livingEntity.isBaby();

            float h = MathHelper.lerpAngleDegrees(g, livingEntity.prevBodyYaw, livingEntity.bodyYaw);
            float j = MathHelper.lerpAngleDegrees(g, livingEntity.prevHeadYaw, livingEntity.headYaw);
            float k = j - h;

            float m = MathHelper.lerp(g, livingEntity.prevPitch, livingEntity.getPitch());
            if (shouldFlipUpsideDown(livingEntity)) {
                m *= -1.0F;
                k *= -1.0F;
            }

            float lx = livingEntity.getScale();
            matrixStack.scale(lx, lx, lx);
            float n = this.getAnimationProgress(livingEntity, g);
            this.setupTransforms(livingEntity, matrixStack, n, h, g, lx);
            matrixStack.scale(-1.0F, -1.0F, 1.0F);
            this.scale(livingEntity, matrixStack, g);
            matrixStack.translate(0.0F, -1.501F, 0.0F);
            float o = 0.0F;
            float p = 0.0F;
            if (!livingEntity.hasVehicle() && livingEntity.isAlive()) {
                o = livingEntity.limbAnimator.getSpeed(g);
                p = livingEntity.limbAnimator.getPos(g);
                if (livingEntity.isBaby()) {
                    p *= 3.0F;
                }

                if (o > 1.0F) {
                    o = 1.0F;
                }
            }

            model.animateModel(livingEntity, p, o, g);
            model.setAngles(livingEntity, p, o, n, k, m);
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            boolean bl = this.isVisible(livingEntity);
            boolean bl2 = !bl && !livingEntity.isInvisibleTo(minecraftClient.player);
            if (renderLayer != null) {
                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
                int q = getOverlay(livingEntity, this.getAnimationCounter(livingEntity, g));
                matrixStack.push();
                matrixStack.scale(scale, scale, scale);

                Color color = new Color(1f, 1f, 1f, alpha);
                model.render(matrixStack, vertexConsumer, light, q, bl2 ? 654311423 : color.getRGB());
                matrixStack.pop();
            }

            matrixStack.pop();
        }
    }

    private RenderLayer getSkeletonRenderLayer() {
        return this.model.getLayer(SKELETON_TEXTURE);
    }

    private void setModelPose() {
        PlayerEntityModel<PlayerBodyEntity> playerEntityModel = this.getModel();
        playerEntityModel.setVisible(false);
        playerEntityModel.head.visible = true;
        playerEntityModel.hat.visible = true;
        playerEntityModel.setVisible(true);
        playerEntityModel.hat.visible = true;
        playerEntityModel.jacket.visible = true;
        playerEntityModel.leftPants.visible = true;
        playerEntityModel.rightPants.visible = true;
        playerEntityModel.leftSleeve.visible = true;
        playerEntityModel.rightSleeve.visible = true;
        skeletonModel.setVisible(true);
        skeletonModel.child = false;
    }

    @Override
    public Identifier getTexture(PlayerBodyEntity playerBodyEntity) {
        PlayerListEntry playerListEntry = TMMClient.PLAYER_ENTRIES_CACHE.get(playerBodyEntity.getPlayerUuid());
        if (playerListEntry != null) {
            return playerListEntry.getSkinTextures().texture();
        } else {
            return DEFAULT_TEXTURE;
        }
    }


    @Override
    protected void renderLabelIfPresent(PlayerBodyEntity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta) {

    }

    @Override
    protected void setupTransforms(PlayerBodyEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, float scale) {
        int animTickEnd = 20;
        float t = Math.min(entity.age + tickDelta, animTickEnd) / animTickEnd;
        float animProgress = Easing.BOUNCE_OUT.ease(t, 0, 1, 1);

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90 - bodyYaw));
        matrices.translate(1F, 0f, 0f);
        matrices.translate(0F, animProgress * 0.15f, 0F);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(animProgress * this.getLyingAngle(entity)));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
    }

    @Override
    protected void scale(PlayerBodyEntity entity, MatrixStack matrices, float amount) {
        float g = 0.9375F;
        matrices.scale(g, g, g);
    }

    @Override
    protected float getHandSwingProgress(PlayerBodyEntity entity, float tickDelta) {
        return 0f;
    }

    @Override
    protected float getAnimationProgress(PlayerBodyEntity entity, float tickDelta) {
        return 0.1f;
    }
}
