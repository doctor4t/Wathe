package dev.doctor4t.trainmurdermystery.mixin.client.scenery;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.client.util.AlwaysVisibleFrustum;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    public int ticks;

    @Shadow
    @Nullable
    private ClientWorld world;

    @Inject(method = "method_52816", at = @At(value = "RETURN"), cancellable = true)
    private static void tmm$setFrustumToAlwaysVisible(Frustum frustum, CallbackInfoReturnable<Frustum> cir) {
        cir.setReturnValue(new AlwaysVisibleFrustum(frustum));
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderSky(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V"))
    public void tmm$disableSky(WorldRenderer instance, Matrix4f matrix4f, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback, Operation<Void> original) {
        if (!TMMClient.isTrainMoving()) {
            original.call(instance, matrix4f, projectionMatrix, tickDelta, camera, thickFog, fogCallback);
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BackgroundRenderer;applyFog(Lnet/minecraft/client/render/Camera;Lnet/minecraft/client/render/BackgroundRenderer$FogType;FZF)V"))
    public void tmm$applyBlizzardFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, Operation<Void> original) {
        if (TMMClient.isTrainMoving()) {
            doFog(0, 130);
        } else {
            doFog(30, 200);
        }
    }

    @Unique
    private static void doFog(int fogStart, int fogEnd) {
        BackgroundRenderer.FogData fogData = new BackgroundRenderer.FogData(BackgroundRenderer.FogType.FOG_SKY);

        fogData.fogStart = fogStart;
        fogData.fogEnd = fogEnd;

        fogData.fogShape = FogShape.SPHERE;

        RenderSystem.setShaderFogStart(fogData.fogStart);
        RenderSystem.setShaderFogEnd(fogData.fogEnd);
        RenderSystem.setShaderFogShape(fogData.fogShape);
    }

    @Inject(method = "renderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/ShaderProgram;bind()V", shift = At.Shift.AFTER), cancellable = true)
    private void tmm$renderScenery(RenderLayer renderLayer, double x, double y, double z, Matrix4f matrix4f, Matrix4f positionMatrix, CallbackInfo ci, @Local ObjectListIterator<ChunkBuilder.BuiltChunk> objectListIterator, @Local ShaderProgram shaderProgram) {
        if (TMMClient.isTrainMoving()) {
            GlUniform glUniform = shaderProgram.chunkOffset;

            float trainSpeed = TMMClient.getTrainSpeed(); // in kmh
            int chunkSize = 16;
            int tileWidth = 15 * chunkSize;
            int height = 116;
            int tileLength = 32 * chunkSize;
            int tileSize = tileLength * 3;

            float time = ticks + client.getRenderTickCounter().getTickDelta(true);

            boolean isTranslucent = renderLayer != RenderLayer.getTranslucent();
            while (isTranslucent ? objectListIterator.hasNext() : objectListIterator.hasPrevious()) {
                boolean tooFar = false;

                ChunkPos chunkPos = new ChunkPos(client.cameraEntity.getBlockPos());
                client.chunkCullingEnabled = false;

                ChunkBuilder.BuiltChunk builtChunk2 = isTranslucent ? objectListIterator.next() : objectListIterator.previous();
                if (!builtChunk2.getData().isEmpty(renderLayer)) {
                    VertexBuffer vertexBuffer = builtChunk2.getBuffer(renderLayer);
                    BlockPos blockPos = builtChunk2.getOrigin();

                    if (glUniform != null) {
                        boolean trainSection = ChunkSectionPos.getSectionCoord(blockPos.getY()) >= 4;
                        float v1 = (float) ((double) blockPos.getX() - x);
                        float v2 = (float) ((double) blockPos.getY() - y);
                        float v3 = (float) ((double) blockPos.getZ() - z);

                        int zSection = blockPos.getZ() / chunkSize - chunkPos.z;

                        float finalX = v1;
                        float finalY = v2;
                        float finalZ = v3;

                        if (zSection <= -8) {
                            finalX = ((v1 - tileLength + ((time) / 73.8f * trainSpeed)) % tileSize - tileSize / 2f);
                            finalY = (v2 + height);
                            finalZ = v3 + tileWidth;
                        } else if (zSection >= 8) {
                            finalX = ((v1 + tileLength + ((time) / 73.8f * trainSpeed)) % tileSize - tileSize / 2f);
                            finalY = (v2 + height);
                            finalZ = v3 - tileWidth;
                        } else if (!trainSection) {
                            finalX = ((v1 + ((time) / 73.8f * trainSpeed)) % tileSize - tileSize / 2f);
                            finalY = (v2 + height);
                            finalZ = v3;
                        }

                        if (Math.abs(finalX) < 160) {
                            glUniform.set(
                                    finalX,
                                    finalY,
                                    finalZ
                            );
                            glUniform.upload();
                        } else {
                            tooFar = true;
                        }
                    }

                    if (!tooFar) {
                        vertexBuffer.bind();
                        vertexBuffer.draw();
                    }
                }
            }

            if (glUniform != null) {
                glUniform.set(0.0F, 0.0F, 0.0F);
            }

            shaderProgram.unbind();
            VertexBuffer.unbind();
            this.client.getProfiler().pop();
            renderLayer.endDrawing();

            ci.cancel();
        }
    }
}