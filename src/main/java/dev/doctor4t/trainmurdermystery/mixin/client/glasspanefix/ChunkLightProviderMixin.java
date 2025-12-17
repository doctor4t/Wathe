package dev.doctor4t.trainmurdermystery.mixin.client.glasspanefix;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.trainmurdermystery.block.GlassPanelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.chunk.light.ChunkLightProvider;
import net.minecraft.world.chunk.light.ChunkSkyLightProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChunkLightProvider.class)
public class ChunkLightProviderMixin {

    @Definition(id = "getOpacity", method = "Lnet/minecraft/block/BlockState;getOpacity(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)I")
    @Definition(id = "newState", local = @Local(type = BlockState.class, ordinal = 1, argsOnly = true))
    @Definition(id = "oldState", local = @Local(type = BlockState.class, ordinal = 0, argsOnly = true))
    @Expression("newState.getOpacity(?, ?) != oldState.getOpacity(?, ?)")
    @ModifyExpressionValue(method = "needsLightUpdate", at = @At("MIXINEXTRAS:EXPRESSION"), remap = false)
    private static boolean tmm$fixGlassPanelLight(boolean original, BlockView blockView, BlockPos pos, BlockState oldState, BlockState newState) {
        if (oldState.getBlock() instanceof GlassPanelBlock && !(newState.getBlock() instanceof GlassPanelBlock)) {
            return true;
        }
        return original;
    }

    @ModifyExpressionValue(method = "getOpacity", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getOpacity(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)I"))
    private int tmm$fixGlassPanelLight(int original, BlockState state, BlockPos pos) {
        ChunkLightProvider<?, ?> provider = (ChunkLightProvider<?, ?>) (Object) this;

        if (provider instanceof ChunkSkyLightProvider && state.getBlock() instanceof GlassPanelBlock) {
            return 15;
        }
        return original;
    }
}
