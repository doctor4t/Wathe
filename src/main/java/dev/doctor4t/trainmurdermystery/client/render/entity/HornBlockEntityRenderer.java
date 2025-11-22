package dev.doctor4t.trainmurdermystery.client.render.entity;

import dev.doctor4t.ratatouille.client.lib.render.helpers.Easing;
import dev.doctor4t.ratatouille.mixin.client.BlockRenderManagerAccessor;
import dev.doctor4t.trainmurdermystery.block.entity.HornBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class HornBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    private final BlockRenderManager renderManager;

    public HornBlockEntityRenderer(BlockEntityRendererFactory.@NotNull Context ctx) {
        this.renderManager = ctx.getRenderManager();
    }

    public void render(@NotNull T entity, float tickDelta, @NotNull MatrixStack matrices, @NotNull VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();
        var pull = Easing.CUBIC_IN.ease((entity instanceof HornBlockEntity plushie ? (float) MathHelper.lerp(tickDelta, plushie.prevPull, plushie.pull) : 0), 0, 1, 1) / 2f;
        matrices.translate(0, -pull, 0);
        var state = entity.getCachedState();
        ((BlockRenderManagerAccessor) this.renderManager).getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state, false)), state, this.renderManager.getModel(state), 0xFF, 0xFF, 0xFF, light, overlay);

        matrices.push();
        BlockState chain = Blocks.CHAIN.getDefaultState();
        matrices.translate(0, 1, 0);
        ((BlockRenderManagerAccessor) this.renderManager).getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(chain, false)), chain, this.renderManager.getModel(chain), 0xFF, 0xFF, 0xFF, light, overlay);
        matrices.translate(0, 1, 0);
        ((BlockRenderManagerAccessor) this.renderManager).getModelRenderer().render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(chain, false)), chain, this.renderManager.getModel(chain), 0xFF, 0xFF, 0xFF, light, overlay);
        matrices.pop();

        matrices.pop();
    }
}