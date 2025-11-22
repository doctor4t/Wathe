package dev.doctor4t.trainmurdermystery.client.render.block_entity;

import dev.doctor4t.trainmurdermystery.block_entity.BeveragePlateBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;

public class PlateBlockEntityRenderer implements BlockEntityRenderer<BeveragePlateBlockEntity> {
    private final ItemRenderer itemRenderer;

    public PlateBlockEntityRenderer(BlockEntityRendererFactory.@NotNull Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(@NotNull BeveragePlateBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.isDrink()) {
            this.renderDrinks(entity, matrices, vertexConsumers, light, overlay);
        } else {
            this.renderFood(entity, matrices, vertexConsumers, light, overlay);
        }
    }

    public void renderFood(@NotNull BeveragePlateBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var itemCount = entity.getStoredItems().size();
        if (itemCount == 0) return;

        var radius = 0.25;
        var centerX = 0.5;
        var centerY = 0.0375;
        var centerZ = 0.5;

        for (var i = 0; i < itemCount; i++) {
            var stack = entity.getStoredItems().get(i);
            if (stack == null) continue;

            var angle = (2 * Math.PI / itemCount) * i;

            var x = centerX + radius * Math.cos(angle);
            var z = centerZ + radius * Math.sin(angle);

            matrices.push();

            matrices.translate(x, centerY, z);

            var rotationDegrees = (float) Math.toDegrees(angle) + 90f;

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotationDegrees));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(75f));
            matrices.scale(0.4f, 0.4f, 0.4f);

            this.itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
            matrices.pop();
        }
    }

    public void renderDrinks(@NotNull BeveragePlateBlockEntity entity, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var itemCount = entity.getStoredItems().size();
        if (itemCount == 0) return;

        var radius = 0.25;
        var centerX = 0.5;
        var centerY = 0.225;
        var centerZ = 0.5;

        for (var i = 0; i < itemCount; i++) {
            var stack = entity.getStoredItems().get(i);
            if (stack == null) continue;

            var angle = (2 * Math.PI / itemCount) * i;

            var x = centerX + radius * Math.cos(angle);
            var z = centerZ + radius * Math.sin(angle);

            matrices.push();

            matrices.translate(x, centerY, z);

            var rotationDegrees = (float) Math.toDegrees(angle) + 90f;

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotationDegrees));
            matrices.scale(0.4f, 0.4f, 0.4f);

            this.itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);

            matrices.pop();
        }
    }
}
