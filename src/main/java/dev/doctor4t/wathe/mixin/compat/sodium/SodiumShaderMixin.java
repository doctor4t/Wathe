package dev.doctor4t.wathe.mixin.compat.sodium;

import dev.doctor4t.wathe.compat.SodiumShaderInterface;
import net.caffeinemc.mods.sodium.client.gl.buffer.GlMutableBuffer;
import net.caffeinemc.mods.sodium.client.gl.shader.uniform.GlUniformBlock;
import net.caffeinemc.mods.sodium.client.render.chunk.shader.ShaderBindingContext;
import net.irisshaders.iris.gl.blending.BlendModeOverride;
import net.irisshaders.iris.pipeline.IrisRenderingPipeline;
import net.irisshaders.iris.pipeline.programs.SodiumPrograms;
import net.irisshaders.iris.pipeline.programs.SodiumShader;
import net.irisshaders.iris.uniforms.custom.CustomUniforms;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Supplier;

@Mixin(SodiumShader.class)
public class SodiumShaderMixin implements SodiumShaderInterface {
    @Unique
    private GlUniformBlock uniformOffsets;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void wathe$addUniform(IrisRenderingPipeline pipeline, SodiumPrograms.Pass pass, ShaderBindingContext context, int handle, BlendModeOverride blendModeOverride, List bufferBlendOverrides, CustomUniforms customUniforms, Supplier flipState, float alphaTest, boolean containsTessellation, CallbackInfo ci) {
        uniformOffsets = context.bindUniformBlock("ubo_SectionOffsets", 1);
    }

    @Override
    public void wathe$set(GlMutableBuffer buffer) {
        if (uniformOffsets == null) {
            return;
        }

        uniformOffsets.bindBuffer(buffer);
    }
}
