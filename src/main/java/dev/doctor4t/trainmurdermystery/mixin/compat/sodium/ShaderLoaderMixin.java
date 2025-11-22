package dev.doctor4t.trainmurdermystery.mixin.compat.sodium;

import dev.doctor4t.trainmurdermystery.compat.IrisHelper;
import dev.doctor4t.trainmurdermystery.util.ShaderEditor;
import net.caffeinemc.mods.sodium.client.gl.shader.ShaderLoader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShaderLoader.class)
public class ShaderLoaderMixin {
    @Inject(method = "getShaderSource", at = @At("RETURN"), cancellable = true)
    private static void tmm$addVertexOffset(Identifier name, CallbackInfoReturnable<String> cir) {
        if (IrisHelper.isIrisShaderPackInUse()) {
            return;
        }

        if (name.getPath().contains("block_layer_opaque.vsh")) {
            String modifiedShader = new ShaderEditor(cir.getReturnValue())
                    .addUniform("struct Offset { vec4 pos; };")
                    .addUniform("layout(std140) uniform ubo_SectionOffsets { Offset Offsets[256]; };")
                    .addBefore("_vert_position",
                            "    _vert_position += Offsets[_draw_id].pos.xyz;")
                    .build();

//            System.out.println(modifiedShader);
            cir.setReturnValue(modifiedShader);
        }
    }
}
