package dev.doctor4t.wathe.client.render.block_entity;

import dev.doctor4t.wathe.block_entity.SmallDoorBlockEntity;
import dev.doctor4t.wathe.client.animation.SmallDoorAnimations;
import dev.doctor4t.wathe.client.model.WatheModelLayers;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SmallDoorBlockEntityRenderer extends AnimatableBlockEntityRenderer<SmallDoorBlockEntity> {

    private final Identifier texture;
    private final ModelPart part;

    public SmallDoorBlockEntityRenderer(Identifier texture, BlockEntityRendererFactory.Context ctx) {
        this.texture = texture;
        this.part = ctx.getLayerModelPart(WatheModelLayers.SMALL_DOOR);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("Door", ModelPartBuilder.create().uv(28, 56).cuboid(-8.0F, -2.0F, -1.0F, 16.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(28, 60).cuboid(-8.0F, -32.0F, -1.0F, 16.0F, 2.0F, 2.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-6.0F, -30.0F, 0.0F, 12.0F, 28.0F, 0.0F, new Dilation(0.01F))
                .uv(0, 34).cuboid(-8.0F, -30.0F, -1.0F, 2.0F, 28.0F, 2.0F, new Dilation(0.0F))
                .uv(8, 34).cuboid(6.0F, -30.0F, -1.0F, 2.0F, 28.0F, 2.0F, new Dilation(0.0F))
                .uv(28, 0).cuboid(-8.0F, -32.0F, -1.0F, 16.0F, 32.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void render(SmallDoorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.translate(0.5f, 1.5f, 0.5f);
        matrices.scale(1, -1, 1);
        super.render(entity, tickDelta, matrices, vertexConsumers, light, overlay);
    }

    @Override
    public void setAngles(SmallDoorBlockEntity entity, float animationProgress) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.part.setAngles(0, entity.getYaw() * MathHelper.RADIANS_PER_DEGREE, 0);
        this.updateAnimation(entity.state, entity.isOpen() ? SmallDoorAnimations.OPEN : SmallDoorAnimations.CLOSE, animationProgress);
    }

    @Override
    public Identifier getTexture(SmallDoorBlockEntity entity, float tickDelta) {
        return this.texture;
    }

    @Override
    public int getAge(SmallDoorBlockEntity entity) {
        return entity.getAge();
    }

    @Override
    public ModelPart getPart() {
        return this.part;
    }
}
