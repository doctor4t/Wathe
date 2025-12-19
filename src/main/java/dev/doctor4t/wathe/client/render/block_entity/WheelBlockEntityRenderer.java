package dev.doctor4t.wathe.client.render.block_entity;

import dev.doctor4t.wathe.block_entity.WheelBlockEntity;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.client.model.WatheModelLayers;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class WheelBlockEntityRenderer extends AnimatableBlockEntityRenderer<WheelBlockEntity> {

    private final Identifier texture;
    private final ModelPart part;

    public WheelBlockEntityRenderer(Identifier texture, BlockEntityRendererFactory.Context ctx) {
        this.texture = texture;
        this.part = ctx.getLayerModelPart(WatheModelLayers.WHEEL);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData all = modelPartData.addChild("all", ModelPartBuilder.create().uv(0, 0).cuboid(-0.6F, -16.0F, -16.0F, 1.0F, 32.0F, 32.0F, new Dilation(-0.3F))
                .uv(0, 0).mirrored().cuboid(-0.4F, -16.0F, -16.0F, 1.0F, 32.0F, 32.0F, new Dilation(-0.3F)).mirrored(false), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        ModelPartData bone = all.addChild("bone", ModelPartBuilder.create(), ModelTransform.pivot(-0.5F, 0.0F, 0.0F));

        ModelPartData cube_r1 = bone.addChild("cube_r1", ModelPartBuilder.create().uv(89, 56).cuboid(-2.0F, -4.0F, -4.0F, 4.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData normalsides = all.addChild("normalsides", ModelPartBuilder.create().uv(32, 64).mirrored().cuboid(-2.0F, -4.0F, -6.0F, 4.0F, 4.0F, 12.0F, new Dilation(-0.02F)).mirrored(false)
                .uv(0, 64).mirrored().cuboid(-2.0F, -32.0F, -6.0F, 4.0F, 4.0F, 12.0F, new Dilation(-0.02F)).mirrored(false)
                .uv(0, 80).mirrored().cuboid(-2.0F, -22.0F, -16.0F, 4.0F, 12.0F, 4.0F, new Dilation(-0.02F)).mirrored(false)
                .uv(16, 80).cuboid(-2.0F, -22.0F, 12.0F, 4.0F, 12.0F, 4.0F, new Dilation(-0.02F)), ModelTransform.pivot(0.0F, 16.0F, 0.0F));

        ModelPartData angledsides = normalsides.addChild("angledsides", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData cube_r2 = angledsides.addChild("cube_r2", ModelPartBuilder.create().uv(66, 18).mirrored().cuboid(-2.0F, 0.0F, 0.0F, 4.0F, 14.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, -10.0F, -16.0F, 0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r3 = angledsides.addChild("cube_r3", ModelPartBuilder.create().uv(66, 36).cuboid(-2.0F, -14.0F, -4.0F, 4.0F, 14.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -22.0F, 16.0F, 0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r4 = angledsides.addChild("cube_r4", ModelPartBuilder.create().uv(66, 36).cuboid(-2.0F, 0.0F, -4.0F, 4.0F, 14.0F, 4.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -10.0F, 16.0F, -0.7854F, 0.0F, 0.0F));

        ModelPartData cube_r5 = angledsides.addChild("cube_r5", ModelPartBuilder.create().uv(66, 18).mirrored().cuboid(-2.0F, -14.0F, 0.0F, 4.0F, 14.0F, 4.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.of(0.0F, -22.0F, -16.0F, -0.7854F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void render(WheelBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.translate(0, 0.3f, .5f);
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((WatheClient.trainComponent.getTime() + tickDelta) * (WatheClient.getTrainSpeed() * .9f)));
        super.render(entity, tickDelta, matrices, vertexConsumers, light, overlay);
    }

    @Override
    public void setAngles(WheelBlockEntity entity, float animationProgress) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.part.setAngles(0, entity.getYaw() * MathHelper.RADIANS_PER_DEGREE, 0);
    }

    @Override
    public Identifier getTexture(WheelBlockEntity entity, float tickDelta) {
        return this.texture;
    }

    @Override
    public ModelPart getPart() {
        return this.part;
    }
}
