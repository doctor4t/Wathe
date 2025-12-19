package dev.doctor4t.wathe.client.model.item;

import dev.doctor4t.wathe.index.WatheCosmetics;
import dev.doctor4t.wathe.item.KnifeItem;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public class KnifeModel implements UnbakedModel, FabricBakedModel, BakedModel {

    /**
     * indexed by skin, then variant!
     */
    private final BakedModel[][] bakedModels = new BakedModel[KnifeItem.Skin.values().length][KnifeModelLoadingPlugin.Variant.values().length];
    private final UnbakedModel defaultUnbakedModel;

    public KnifeModel(UnbakedModel defaultUnbakedModel) {
        this.defaultUnbakedModel = defaultUnbakedModel;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return defaultUnbakedModel.getModelDependencies();
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {
        defaultUnbakedModel.setParents(modelLoader);
    }

    @Override
    public @Nullable BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings settings) {
        for (KnifeItem.Skin skin : KnifeItem.Skin.values()) {
            for (KnifeModelLoadingPlugin.Variant variant : KnifeModelLoadingPlugin.Variant.values()) {
                bakedModels[skin.ordinal()][variant.ordinal()] = baker.bake(KnifeModelLoadingPlugin.getModelLocation(skin, variant), settings);
            }
        }

        return this;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    private static final Set<ModelTransformationMode> IN_HAND = EnumSet.of(ModelTransformationMode.THIRD_PERSON_LEFT_HAND, ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, ModelTransformationMode.HEAD, ModelTransformationMode.FIXED);

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
        var mode = context.itemTransformationMode();
        var variant = mode.isFirstPerson() || IN_HAND.contains(mode) ? KnifeModelLoadingPlugin.Variant.IN_HAND : KnifeModelLoadingPlugin.Variant.DEFAULT;
        var skin = KnifeItem.Skin.fromString(WatheCosmetics.getSkin(stack));

        bakedModels[skin.ordinal()][variant.ordinal()].emitItemQuads(stack, randomSupplier, context);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return getDefaultModel().getQuads(state, face, random);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return getDefaultModel().useAmbientOcclusion();
    }

    @Override
    public boolean hasDepth() {
        return getDefaultModel().hasDepth();
    }

    @Override
    public boolean isSideLit() {
        return getDefaultModel().isSideLit();
    }

    @Override
    public boolean isBuiltin() {
        return getDefaultModel().isBuiltin();
    }

    @Override
    public Sprite getParticleSprite() {
        return getDefaultModel().getParticleSprite();
    }

    @Override
    public ModelTransformation getTransformation() {
        return getDefaultModel().getTransformation();
    }

    @Override
    public ModelOverrideList getOverrides() {
        return getDefaultModel().getOverrides();
    }

    private BakedModel getDefaultModel() {
        return bakedModels[KnifeItem.Skin.DEFAULT.ordinal()][KnifeModelLoadingPlugin.Variant.DEFAULT.ordinal()];
    }
}
