package dev.doctor4t.wathe.mixin.client;

import com.mojang.authlib.GameProfile;
import dev.doctor4t.wathe.cca.PlayerPoisonComponent;
import dev.doctor4t.wathe.util.PoisonUtils;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {
    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "getFovMultiplier", at = @At("RETURN"), cancellable = true)
    private void wathe$fovPulse(CallbackInfoReturnable<Float> cir) {
        float original = cir.getReturnValueF();
        cir.setReturnValue(original * PoisonUtils.getFovMultiplier(1f, PlayerPoisonComponent.KEY.get(this)));
    }
}
