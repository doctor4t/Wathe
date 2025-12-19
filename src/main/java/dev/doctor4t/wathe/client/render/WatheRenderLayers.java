package dev.doctor4t.wathe.client.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public interface WatheRenderLayers {
    static RenderLayer additive(Identifier texture) {
        return RenderLayer.of(
                "hand_particle_additive",
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS,
                256,
                true,
                true,
                RenderLayer.MultiPhaseParameters.builder()
                        .program(RenderPhase.ENTITY_TRANSLUCENT_PROGRAM)
                        .texture(new RenderPhase.Texture(texture, false, false))
                        .transparency(RenderPhase.ADDITIVE_TRANSPARENCY)
                        .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
                        .cull(RenderPhase.DISABLE_CULLING)
                        .lightmap(RenderPhase.ENABLE_LIGHTMAP)
                        .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                        .build(true)
        );
    }

    static RenderLayer additiveFullbright(Identifier texture) {
        return RenderLayer.of(
                "hand_particle_additive_fullbright",
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS,
                256,
                true,
                true,
                RenderLayer.MultiPhaseParameters.builder()
                        .program(RenderPhase.ENTITY_TRANSLUCENT_PROGRAM)
                        .texture(new RenderPhase.Texture(texture, false, false))
                        .transparency(RenderPhase.ADDITIVE_TRANSPARENCY)
                        .depthTest(RenderPhase.LEQUAL_DEPTH_TEST)
                        .cull(RenderPhase.DISABLE_CULLING)
                        .lightmap(RenderPhase.DISABLE_LIGHTMAP)
                        .overlay(RenderPhase.ENABLE_OVERLAY_COLOR)
                        .build(true)
        );
    }
}
