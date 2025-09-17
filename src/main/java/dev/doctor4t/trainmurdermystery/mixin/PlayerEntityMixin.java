package dev.doctor4t.trainmurdermystery.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.doctor4t.trainmurdermystery.TrainMurderMystery;
import dev.doctor4t.trainmurdermystery.index.TrainMurderMysteryItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Unique
    private float sprintingTicks;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    public abstract boolean isSpectator();

    @ModifyReturnValue(method = "getMovementSpeed", at = @At("RETURN"))
    public float tmm$overrideMovementSpeed(float original) {
        if (TrainMurderMystery.isPlayerAliveAndSurvival((PlayerEntity) (Object) this)) {
            return this.isSprinting() ? 0.1f : 0.07f;
        } else {
            return original;
        }
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void tmm$limitSprint(CallbackInfo ci) {
        if (TrainMurderMystery.isPlayerAliveAndSurvival((PlayerEntity) (Object) this)) {
            if (this.isSprinting()) {
                sprintingTicks = Math.max(sprintingTicks - 1, 0);
            } else {
                // 5s
                float MAX_SPRINTING_TICKS = 100;
                sprintingTicks = Math.min(sprintingTicks + 0.25f, MAX_SPRINTING_TICKS);
            }

            if (sprintingTicks <= 0) {
                this.setSprinting(false);
            }
        }
    }

    @WrapMethod(method = "attack")
    public void attack(Entity target, Operation<Void> original) {
        if (!TrainMurderMystery.isPlayerAliveAndSurvival((PlayerEntity) (Object)this) || this.getMainHandStack().isOf(TrainMurderMysteryItems.KNIFE)) {
            original.call(target);
        }
    }
}
