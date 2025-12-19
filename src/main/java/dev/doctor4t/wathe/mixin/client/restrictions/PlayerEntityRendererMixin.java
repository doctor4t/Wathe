package dev.doctor4t.wathe.mixin.client.restrictions;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.cca.PlayerPsychoComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    @WrapMethod(method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V")
    protected void wathe$disableNameTags(AbstractClientPlayerEntity abstractClientPlayerEntity, Text text, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, float f, Operation<Void> original) {
    }

    @Inject(method = "getTexture(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
    private void wathe$psychoSkinTexture(
            AbstractClientPlayerEntity abstractClientPlayerEntity, CallbackInfoReturnable<Identifier> cir) {
        if (PlayerPsychoComponent.KEY.get(abstractClientPlayerEntity).getPsychoTicks() > 0) {
            SkinTextures.Model model = abstractClientPlayerEntity.getSkinTextures().model();
            String suffix = (model == SkinTextures.Model.SLIM) ? "_thin" : "";
            Identifier texture = Wathe.id("textures/entity/psycho" + suffix + ".png");

            cir.setReturnValue(texture);
        }
    }

    @ModifyVariable(method = "renderArm", at = @At("STORE"), ordinal = 0)
    private Identifier wathe$psychoArmTexture(Identifier skinTexture) {
        if (PlayerPsychoComponent.KEY.get(MinecraftClient.getInstance().player).getPsychoTicks() > 0) {
            SkinTextures.Model model = MinecraftClient.getInstance().player.getSkinTextures().model();
            String suffix = model == SkinTextures.Model.SLIM ? "_thin" : "";
            return Wathe.id("textures/entity/psycho" + suffix + ".png");
        }
        return skinTexture;
    }
}
