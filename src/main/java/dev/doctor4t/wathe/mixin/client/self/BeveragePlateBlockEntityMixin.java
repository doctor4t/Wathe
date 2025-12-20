package dev.doctor4t.trainmurdermystery.mixin.client.self;

import dev.doctor4t.trainmurdermystery.block_entity.BeveragePlateBlockEntity;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.event.CanSeePoison;
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

/**
 * @author SkyNotTheLimit
 */
@Environment(EnvType.CLIENT)
@Mixin(BeveragePlateBlockEntity.class)
public class BeveragePlateBlockEntityMixin {

    // haha I love writing extremely cursed mixins
    @Inject(method = "clientTick", at = @At("HEAD"))
    private static void tickWithoutFearOfCrashing(World world, BlockPos pos, BlockState state, BlockEntity blockEntity, CallbackInfo ci) {
        if (!(blockEntity instanceof BeveragePlateBlockEntity tray)) {
            return;
        }
        if (
                (!TMMClient.isKiller() &&
                        !TMMClient.isPlayerSpectatingOrCreative() &&
                        !CanSeePoison.EVENT.invoker().visible(MinecraftClient.getInstance().player)
                ) || tray.getPoisoner() == null) {
            return;
        }
        if (world.getRandom().nextBetween(0, 20) < 17) {
            return;
        }
        world.addParticle(
                TMMParticles.POISON,
                pos.getX() + 0.5f,
                pos.getY(),
                pos.getZ() + 0.5f,
                0f, 0.05f, 0f
        );
    }
}
