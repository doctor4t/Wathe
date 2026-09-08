package dev.doctor4t.wathe.client.model.item;

import dev.doctor4t.wathe.index.WatheCosmetics;
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

public class RevolverModel implements UnbakedModel, FabricBakedModel, BakedModel {

    /**
     * indexed by skin, then variant!
     */
    private final BakedModel[][] bakedModels = new BakedModel[WatheCosmetics.REVOLVER_SKINS_MANAGER.skinList.size()][RevolverModelLoadingPlugin.Variant.values().length];
    private final UnbakedModel defaultUnbakedModel;

    public RevolverModel(UnbakedModel defaultUnbakedModel) {
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
        for (WatheCosmetics.ItemSkinsManager.Skin skin : WatheCosmetics.REVOLVER_SKINS_MANAGER.skinList) {
            for (RevolverModelLoadingPlugin.Variant variant : RevolverModelLoadingPlugin.Variant.values()) {
                bakedModels[WatheCosmetics.REVOLVER_SKINS_MANAGER.skinList.indexOf(skin)][variant.ordinal()] = baker.bake(RevolverModelLoadingPlugin.getModelLocation(skin, variant), settings);
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
        ModelTransformationMode mode = context.itemTransformationMode();
        RevolverModelLoadingPlugin.Variant variant = mode.isFirstPerson() || IN_HAND.contains(mode) ? RevolverModelLoadingPlugin.Variant.IN_HAND : RevolverModelLoadingPlugin.Variant.DEFAULT;
        WatheCosmetics.ItemSkinsManager.Skin skin = WatheCosmetics.REVOLVER_SKINS_MANAGER.fromString(WatheCosmetics.getSkinName(stack));

        bakedModels[WatheCosmetics.REVOLVER_SKINS_MANAGER.skinList.indexOf(skin)][variant.ordinal()].emitItemQuads(stack, randomSupplier, context);
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
        return bakedModels[0][RevolverModelLoadingPlugin.Variant.DEFAULT.ordinal()];
    }
}
