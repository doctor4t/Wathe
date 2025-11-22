package dev.doctor4t.trainmurdermystery.mixin.compat.sodium;

import io.github.douira.glsl_transformer.ast.node.TranslationUnit;
import io.github.douira.glsl_transformer.ast.node.statement.CompoundStatement;
import io.github.douira.glsl_transformer.ast.node.statement.Statement;
import io.github.douira.glsl_transformer.ast.query.Root;
import io.github.douira.glsl_transformer.ast.transform.ASTInjectionPoint;
import io.github.douira.glsl_transformer.ast.transform.ASTParser;
import net.irisshaders.iris.gl.shader.ShaderType;
import net.irisshaders.iris.pipeline.transform.parameter.SodiumParameters;
import net.irisshaders.iris.pipeline.transform.transformer.SodiumTransformer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SodiumTransformer.class)
public class SodiumTransformerMixin {

    @Inject(method = "transform", at = @At("TAIL"), remap = false)
    private static void tmm$addVertexOffset(ASTParser t, TranslationUnit tree, Root root, SodiumParameters parameters, CallbackInfo ci) {
        if (parameters.type.glShaderType == ShaderType.VERTEX) {
            List<String> declarations = List.of(
                    "struct Offset { vec4 pos; };",
                    "layout(std140) uniform ubo_SectionOffsets { Offset Offsets[256]; };"
            );
            tree.parseAndInjectNodes(t, ASTInjectionPoint.BEFORE_FUNCTIONS, declarations.stream());

            CompoundStatement vertInit = tree.getOneFunctionDefinitionBody("_vert_init");
            if (vertInit != null) {
                vertInit.getStatements().add(
                        t.parseStatement(root, "_vert_position = _vert_position + Offsets[_draw_id].pos.xyz;")
                );
            }
        }
    }
}
