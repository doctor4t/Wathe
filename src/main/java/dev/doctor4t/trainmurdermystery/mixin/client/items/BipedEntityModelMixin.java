package dev.doctor4t.trainmurdermystery.mixin.client.items;

import dev.doctor4t.trainmurdermystery.cca.PlayerMoodComponent;
import dev.doctor4t.trainmurdermystery.index.tag.TMMItemTags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin<T extends LivingEntity> {
    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart rightArm;

    @Shadow
    @Final
    public ModelPart head;

    @Inject(method = "positionRightArm", at = @At("TAIL"))
    private void holdRevolverRightArm(T entity, CallbackInfo ci) {
        if (tmm$isHoldingGun(entity) && entity.getMainArm() == Arm.RIGHT) {
            tmm$holdGun(this.rightArm, this.leftArm, this.head, true);
        }
    }

    @Inject(method = "positionLeftArm", at = @At("TAIL"))
    private void holdRevolverLeftArm(T entity, CallbackInfo ci) {
        if (tmm$isHoldingGun(entity) && entity.getMainArm() != Arm.RIGHT) {
            tmm$holdGun(this.rightArm, this.leftArm, this.head, false);
        }
    }

    @Unique
    private boolean tmm$isHoldingGun(T entity) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        ItemStack stack = null;
        if (player != null) {
            stack = PlayerMoodComponent.KEY.get(player).getPsychosisItems().get(entity.getUuid());
        }
        if (stack == null) {
            stack = entity.getMainHandStack();
        }
        return stack.isIn(TMMItemTags.GUNS);
    }

    @Unique
    private static void tmm$holdGun(ModelPart holdingArm, ModelPart otherArm, ModelPart head, boolean rightArmed) {
        ModelPart modelPart = rightArmed ? holdingArm : otherArm;
        modelPart.yaw = (rightArmed ? -0.3F : 0.3F) + head.yaw;
        modelPart.pitch = (float) (-Math.PI / 2) + head.pitch + 0.1F;
    }
}
