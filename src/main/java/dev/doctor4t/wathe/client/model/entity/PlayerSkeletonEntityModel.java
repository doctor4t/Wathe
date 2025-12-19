package dev.doctor4t.wathe.client.model.entity;

import dev.doctor4t.wathe.entity.PlayerBodyEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;

@Environment(EnvType.CLIENT)
public class PlayerSkeletonEntityModel<T extends PlayerBodyEntity> extends BipedEntityModel<T> {
    public PlayerSkeletonEntityModel(ModelPart modelPart) {
        super(modelPart);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0.0F);
        ModelPartData modelPartData = modelData.getRoot();
        addLimbs(modelPartData);
        return TexturedModelData.of(modelData, 64, 32);
    }

    protected static void addLimbs(ModelPartData data) {
        data.addChild(
                EntityModelPartNames.HEAD,
                ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.1F, -4.0F, 8.0F, 8.0F, 8.0F),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F)
        );
        data.addChild(
                EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, -1.9F, -1.0F, 2.0F, 12.0F, 2.0F), ModelTransform.pivot(-5.0F, 2.0F, 0.0F)
        );
        data.addChild(
                EntityModelPartNames.LEFT_ARM,
                ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(0.0F, -1.9F, -1.0F, 2.0F, 12.0F, 2.0F),
                ModelTransform.pivot(5.0F, 2.0F, 0.0F)
        );
        data.addChild(
                EntityModelPartNames.RIGHT_LEG, ModelPartBuilder.create().uv(0, 16).cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), ModelTransform.pivot(-2.0F, 12.0F, 0.0F)
        );
        data.addChild(
                EntityModelPartNames.LEFT_LEG,
                ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F),
                ModelTransform.pivot(2.0F, 12.0F, 0.0F)
        );
    }

    public void animateModel(T mobEntity, float f, float g, float h) {
        this.rightArmPose = BipedEntityModel.ArmPose.EMPTY;
        this.leftArmPose = BipedEntityModel.ArmPose.EMPTY;
        super.animateModel(mobEntity, f, g, h);
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        float f = arm == Arm.RIGHT ? 1.0F : -1.0F;
        ModelPart modelPart = this.getArm(arm);
        modelPart.pivotX += f;
        modelPart.rotate(matrices);
        modelPart.pivotX -= f;
    }
}
