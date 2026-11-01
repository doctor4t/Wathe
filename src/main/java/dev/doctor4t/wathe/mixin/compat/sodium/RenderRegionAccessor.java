package dev.doctor4t.wathe.mixin.compat.sodium;

import net.caffeinemc.mods.sodium.client.gl.device.MultiDrawBatch;
import net.caffeinemc.mods.sodium.client.render.chunk.region.RenderRegion;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RenderRegion.class)
public interface RenderRegionAccessor {
    @Accessor(remap = false, value = "cachedBatches")
    Map<TerrainRenderPass, MultiDrawBatch> getCachedBatches();
}
