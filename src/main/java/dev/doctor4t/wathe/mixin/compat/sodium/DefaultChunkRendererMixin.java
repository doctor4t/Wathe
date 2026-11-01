package dev.doctor4t.wathe.mixin.compat.sodium;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.doctor4t.wathe.cca.TrainWorldComponent;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.compat.IrisHelper;
import dev.doctor4t.wathe.compat.SodiumShaderInterface;
import net.caffeinemc.mods.sodium.client.gl.buffer.GlBufferUsage;
import net.caffeinemc.mods.sodium.client.gl.buffer.GlMutableBuffer;
import net.caffeinemc.mods.sodium.client.gl.device.CommandList;
import net.caffeinemc.mods.sodium.client.gl.device.MultiDrawBatch;
import net.caffeinemc.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import net.caffeinemc.mods.sodium.client.render.chunk.DefaultChunkRenderer;
import net.caffeinemc.mods.sodium.client.render.chunk.LocalSectionIndex;
import net.caffeinemc.mods.sodium.client.render.chunk.lists.ChunkRenderListIterable;
import net.caffeinemc.mods.sodium.client.render.chunk.region.RenderRegion;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ChunkShaderInterface;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import net.caffeinemc.mods.sodium.client.render.viewport.CameraTransform;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkSectionPos;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;

@Mixin(value = DefaultChunkRenderer.class)
public abstract class DefaultChunkRendererMixin {
    @Unique private static final ByteBuffer WATHE_BUFFER = MemoryUtil.memAlloc(RenderRegion.REGION_SIZE * 16);
    @Unique private static GlMutableBuffer wathe$glBuffer;

    @Unique private static final FrustumIntersection WATHE_FRUSTUM = new FrustumIntersection();
    @Unique private static final Matrix4f WATHE_TMP = new Matrix4f();
    @Unique private static float worldDeltaX, worldDeltaY, worldDeltaZ;
    @Unique private static boolean worldVisible;
    @Unique private static float frameScroll, frameXLimit;
    @Unique private static int frameCamSectionZ;
    @Unique private static int computeEpoch;
    @Unique private static int lastOriginX, lastOriginY, lastOriginZ, lastEpoch = -1;

    @Unique
    private static void putOffset(int sectionIndex, float x, float y, float z) {
        int base = sectionIndex * 16;
        WATHE_BUFFER.putFloat(base, x);
        WATHE_BUFFER.putFloat(base + 4, y);
        WATHE_BUFFER.putFloat(base + 8, z);
    }

    @Inject(method = "render", at = @At("HEAD"), remap = false)
    private void wathe$captureFrustum(ChunkRenderMatrices matrices, CommandList commandList, ChunkRenderListIterable renderLists, TerrainRenderPass renderPass, CameraTransform camera, boolean indexedRenderingEnabled, CallbackInfo ci) {
        WATHE_FRUSTUM.set(WATHE_TMP.set(matrices.projection()).mul(matrices.modelView()));
        computeEpoch++;
        if (WatheClient.isTrainMoving()) {
            // Only capture world component and world data once per frame
            // This looks like horrible garbage but is actually free CPU time
            float time = WatheClient.trainComponent.getTime() + MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true);
            frameScroll = time / 73.8f * WatheClient.getTrainSpeed();
            frameXLimit = WatheClient.trainComponent.getTimeOfDay() == TrainWorldComponent.TimeOfDay.SUNDOWN ? 320 : 160;
            frameCamSectionZ = ChunkSectionPos.getSectionCoord(camera.intZ);
        }
    }

    @Inject(method = "render", remap = false,
            at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/DefaultChunkRenderer;executeDrawBatch(Lnet/caffeinemc/mods/sodium/client/gl/device/CommandList;Lnet/caffeinemc/mods/sodium/client/gl/tessellation/GlTessellation;Lnet/caffeinemc/mods/sodium/client/gl/device/MultiDrawBatch;)V"))
    private void wathe$uploadOffsets(ChunkRenderMatrices matrices, CommandList commandList, ChunkRenderListIterable renderLists, TerrainRenderPass renderPass, CameraTransform camera, boolean indexedRenderingEnabled, CallbackInfo ci, @Local(name = "shader") ChunkShaderInterface shader) {
        wathe$glBuffer = commandList.createMutableBuffer();
        commandList.uploadData(wathe$glBuffer, WATHE_BUFFER, GlBufferUsage.STREAM_DRAW);
        ((SodiumShaderInterface) shader).wathe$set(wathe$glBuffer);
    }

    @Inject(method = "render", remap = false,
            at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/DefaultChunkRenderer;executeDrawBatch(Lnet/caffeinemc/mods/sodium/client/gl/device/CommandList;Lnet/caffeinemc/mods/sodium/client/gl/tessellation/GlTessellation;Lnet/caffeinemc/mods/sodium/client/gl/device/MultiDrawBatch;)V", shift = At.Shift.AFTER))
    private void wathe$freeGlBuffer(ChunkRenderMatrices matrices, CommandList commandList, ChunkRenderListIterable renderLists, TerrainRenderPass renderPass, CameraTransform camera, boolean indexedRenderingEnabled, CallbackInfo ci) {
        if (wathe$glBuffer != null) {
            commandList.deleteBuffer(wathe$glBuffer);
            wathe$glBuffer = null;
        }
    }

    @ModifyExpressionValue(method = "fillCommandBuffer", remap = false,
            at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/data/SectionRenderDataUnsafe;getSliceMask(J)I"))
    private static int wathe$cullAndOffset(int original, @Local(argsOnly = true, name = "renderRegion") RenderRegion region, @Local(argsOnly = true, name = "camera") CameraTransform camera,
                                           @Local(name = "sectionIndex") int sectionIndex) {
        int offsetX = region.getOriginX() + LocalSectionIndex.unpackX(sectionIndex) * 16;
        int offsetY = region.getOriginY() + LocalSectionIndex.unpackY(sectionIndex) * 16;
        int offsetZ = region.getOriginZ() + LocalSectionIndex.unpackZ(sectionIndex) * 16;

        if (!WatheClient.isTrainMoving()) { putOffset(sectionIndex, 0, 0, 0); return original; }

        wathe$compute(offsetX, offsetY, offsetZ, camera);
        putOffset(sectionIndex, worldDeltaX, worldDeltaY, worldDeltaZ);

        return worldVisible ? original : 0;
    }

    @WrapOperation(method = "fillCommandBuffer", remap = false,
            at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/DefaultChunkRenderer;getVisibleFaces(IIIIII)I"))
    private static int wathe$faces(int originX, int originY, int originZ, int chunkX, int chunkY, int chunkZ, Operation<Integer> original, @Local(argsOnly = true, name = "camera") CameraTransform camera) {
        if (!WatheClient.isTrainMoving()) return original.call(originX, originY, originZ, chunkX, chunkY, chunkZ);
        wathe$compute(chunkX << 4, chunkY << 4, chunkZ << 4, camera);
        return original.call(originX - Math.round(worldDeltaX), originY - Math.round(worldDeltaY), originZ - Math.round(worldDeltaZ), chunkX, chunkY, chunkZ);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/region/RenderRegion;getCachedBatch(Lnet/caffeinemc/mods/sodium/client/render/chunk/terrain/TerrainRenderPass;)Lnet/caffeinemc/mods/sodium/client/gl/device/MultiDrawBatch;"))
    private MultiDrawBatch wathe$noCache(RenderRegion instance, TerrainRenderPass pass) {
        if (WatheClient.isTrainMoving()) {
            // This is ... horrible, but I have no better solution for it other than this. There is ONE way I can think to do it,
            // but I *think* it would be basically just as CPU intensive as this. I've not tested it so don't quote me.
            instance.getCachedBatch(pass).delete(); // DELETE! If you don't call .delete() before removing it, you get an INSANELY fast memory leak!
            ((RenderRegionAccessor) instance).getCachedBatches().remove(pass);
        }
        return instance.getCachedBatch(pass);
    }

    @Unique
    private static void wathe$compute(int originX, int originY, int originZ, CameraTransform camera) {
        // Only compute once per frame! We need this horrible "last" memory keeping because
        // sodium is weird and doesn't call all methods every frame. It also might be because of
        // CPU render-ahead, I'm not sure
        if (originX == lastOriginX && originY == lastOriginY && originZ == lastOriginZ && computeEpoch == lastEpoch) return;

        lastOriginX = originX;
        lastOriginY = originY;
        lastOriginZ = originZ;
        lastEpoch = computeEpoch;

        final int tileWidth = 15 * 16, height = 116, tileLength = 32 * 16, tileSize = tileLength * 3;

        float v1 = originX - camera.fracX;
        float v2 = originY - camera.fracY;
        float v3 = originZ - camera.fracZ;
        int zSection = (originZ >> 4) - frameCamSectionZ;
        boolean trainSection = (originY >> 4) >= 4;

        float finalX = v1, finalY = v2, finalZ = v3;
        if (zSection <= -8) {
            finalX = (v1 - tileLength + frameScroll) % tileSize - tileSize / 2f;
            finalY = v2 + height;
            finalZ = v3 + tileWidth;
        } else if (zSection >= 8) {
            finalX = (v1 + tileLength + frameScroll) % tileSize - tileSize / 2f;
            finalY = v2 + height;
            finalZ = v3 - tileWidth;
        } else if (!trainSection) {
            finalX = (v1 + frameScroll) % tileSize - tileSize / 2f;
            finalY = v2 + height;
        }

        worldDeltaX = finalX - v1;
        worldDeltaY = finalY - v2;
        worldDeltaZ = finalZ - v3;

        float realX = (originX - camera.intX) - camera.fracX + worldDeltaX;
        float realY = (originY - camera.intY) - camera.fracY + worldDeltaY;
        float realZ = (originZ - camera.intZ) - camera.fracZ + worldDeltaZ;

        boolean shadowPass = IrisHelper.isShadowPass();

        // Don't render chunk if not visible!
        // I'm not sure if this was already done previously, or if sodium does some magic
        // to do this without needing to specify, but it *seems* to improve performance so I'm keeping it
        worldVisible = Math.abs(realX) < frameXLimit && (shadowPass || WATHE_FRUSTUM.testAab(realX, realY, realZ, realX + 16, realY + 16, realZ + 16));
    }
}
