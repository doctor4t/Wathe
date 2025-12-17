package dev.doctor4t.trainmurdermystery.mixin.client.self;

import dev.doctor4t.trainmurdermystery.api.event.CanSeePoison;
import dev.doctor4t.trainmurdermystery.block_entity.TrimmedBedBlockEntity;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.index.TMMParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(TrimmedBedBlockEntity.class)
public class TrimmedBedBlockEntityMixin {

    @Inject(method = "clientTick", at = @At("HEAD"))
    private static void tickOnClientSide(World world, BlockPos pos, BlockState state, BlockEntity t, CallbackInfo ci) {
        TrimmedBedBlockEntity entity = (TrimmedBedBlockEntity) t;
        if (!TMMClient.isKiller() && !CanSeePoison.EVENT.invoker().visible(MinecraftClient.getInstance().player)) return;
        if (!entity.hasScorpion()) return;
        if (world.getRandom().nextBetween(0, 20) < 17) return;

        world.addParticle(
                TMMParticles.POISON,
                pos.getX() + 0.5f,
                pos.getY() + 0.5f,
                pos.getZ() + 0.5f,
                0f, 0.05f, 0f
        );
    }
}
