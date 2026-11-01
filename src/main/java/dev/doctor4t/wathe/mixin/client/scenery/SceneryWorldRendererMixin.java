package dev.doctor4t.wathe.mixin.client.scenery;

import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.wathe.cca.TrainWorldComponent;
import dev.doctor4t.wathe.client.WatheClient;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkSectionPos;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class SceneryWorldRendererMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow private Frustum frustum;

    @Inject(method = "renderLayer",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gl/ShaderProgram;bind()V",
                    shift = At.Shift.AFTER),
            cancellable = true)
    private void wathe$renderScenery(RenderLayer renderLayer, double x, double y, double z, Matrix4f matrix4f, Matrix4f positionMatrix, CallbackInfo ci, @Local ObjectListIterator<ChunkBuilder.BuiltChunk> it, @Local ShaderProgram shaderProgram) {
        if (!WatheClient.isTrainMoving()) return;

        GlUniform glUniform = shaderProgram.chunkOffset;
        Frustum realFrustum = this.frustum;

        int chunkSize = 16;
        int tileWidth = 15 * chunkSize;
        int height = 116;
        int tileLength = 32 * chunkSize;
        int tileSize = tileLength * 3;

        float scroll = (WatheClient.trainComponent.getTime() + client.getRenderTickCounter().getTickDelta(true) / 73.8f * WatheClient.getTrainSpeed());
        int camChunkZ = client.cameraEntity.getChunkPos().z;
        float xLimit = WatheClient.trainComponent.getTimeOfDay() == TrainWorldComponent.TimeOfDay.SUNDOWN ? 320 : 160;
        boolean forward = renderLayer != RenderLayer.getTranslucent();

        while (forward ? it.hasNext() : it.hasPrevious()) {
            ChunkBuilder.BuiltChunk chunk = forward ? it.next() : it.previous();
            if (chunk.getData().isEmpty(renderLayer)) continue;

            BlockPos origin = chunk.getOrigin();
            float v1 = (float) (origin.getX() - x);
            float v2 = (float) (origin.getY() - y);
            float v3 = (float) (origin.getZ() - z);
            float fx = v1, fy = v2, fz = v3;

            boolean trainSection = ChunkSectionPos.getSectionCoord(origin.getY()) >= 4;
            int zSection = origin.getZ() / 16 - camChunkZ;

            if (zSection <= -8) {
                fx = (v1 - tileLength + scroll) % tileSize - tileSize / 2f;
                fy = v2 + height;
                fz = v3 + tileWidth;
            } else if (zSection >= 8) {
                fx = (v1 + tileLength + scroll) % tileSize - tileSize / 2f;
                fy = v2 + height;
                fz = v3 - tileWidth;
            } else if (!trainSection) {
                fx = (v1 + scroll) % tileSize - tileSize / 2f;
                fy = v2 + height;
            }

            if (Math.abs(fx) >= xLimit) continue;

            double wx = x + fx, wy = y + fy, wz = z + fz;
            if (!realFrustum.isVisible(new Box(wx, wy, wz, wx + chunkSize, wy + chunkSize, wz + chunkSize))) continue;

            if (glUniform != null) { glUniform.set(fx, fy, fz); glUniform.upload(); }
            chunk.getBuffer(renderLayer).bind();
            chunk.getBuffer(renderLayer).draw();
        }

        if (glUniform != null) glUniform.set(0f, 0f, 0f);
        shaderProgram.unbind();
        VertexBuffer.unbind();
        client.getProfiler().pop();
        renderLayer.endDrawing();
        ci.cancel();
    }

    @Inject(method = "setupTerrain", at = @At("HEAD"))
    private void wathe$disableOcclusion(Camera camera, Frustum frustum, boolean hasForcedFrustum, boolean spectator, CallbackInfo ci) {
        client.chunkCullingEnabled = !WatheClient.isTrainMoving();
    }
}
