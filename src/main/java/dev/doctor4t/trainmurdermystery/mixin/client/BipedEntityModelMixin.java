package dev.doctor4t.trainmurdermystery.mixin.client;

import dev.doctor4t.trainmurdermystery.index.TrainMurderMysteryItems;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin<T extends LivingEntity> {
    @Shadow @Final public ModelPart leftArm;

    @Shadow @Final public ModelPart rightArm;

    @Shadow @Final public ModelPart head;

    @Inject(method = "positionRightArm", at = @At("TAIL"))
    private void tmm$holdRevolverRightArm(T entity, CallbackInfo ci) {
        if (entity.getMainHandStack().isOf(TrainMurderMysteryItems.REVOLVER)) {
            holdRevolver(this.rightArm, this.leftArm, this.head, true);
        }
    }

    @Inject(method = "positionLeftArm", at = @At("TAIL"))
    private void tmm$tmm$holdRevolverLeftArm(T entity, CallbackInfo ci) {
        if (entity.getMainHandStack().isOf(TrainMurderMysteryItems.REVOLVER)) {
//            holdRevolver(this.rightArm, this.leftArm, this.head, false);
        }
    }

    @Unique
    private static void holdRevolver(ModelPart holdingArm, ModelPart otherArm, ModelPart head, boolean rightArmed) {
        ModelPart modelPart = rightArmed ? holdingArm : otherArm;
        modelPart.yaw = (rightArmed ? -0.3F : 0.3F) + head.yaw;
        modelPart.pitch = (float) (-Math.PI / 2) + head.pitch + 0.1F;
    }
}
