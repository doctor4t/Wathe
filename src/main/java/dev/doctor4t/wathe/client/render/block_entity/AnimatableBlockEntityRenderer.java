package dev.doctor4t.wathe.client.render.block_entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public abstract class AnimatableBlockEntityRenderer<T extends BlockEntity> extends SinglePartEntityModel<Entity> implements BlockEntityRenderer<T> {

    public AnimatableBlockEntityRenderer() {
        super();
    }

    public AnimatableBlockEntityRenderer(Function<Identifier, RenderLayer> layerFactory) {
        super(layerFactory);
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        this.setAngles(entity, this.getAge(entity) + tickDelta);
        this.renderPart(entity, tickDelta, matrices, vertexConsumers, light, overlay);
    }

    public void renderPart(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        this.getPart().render(matrices, vertexConsumers.getBuffer(this.layerFactory.apply(this.getTexture(entity, tickDelta))), light, overlay);
    }

    public int getAge(T entity) {
        return entity.getWorld() == null ? 0 : (int) entity.getWorld().getTime();
    }

    public abstract void setAngles(T entity, float animationProgress);

    public abstract Identifier getTexture(T entity, float tickDelta);

    @Override
    public final void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        throw new AssertionError();
    }

    @Override
    public final void animateModel(Entity entity, float limbAngle, float limbDistance, float tickDelta) {
        throw new AssertionError();
    }
}
