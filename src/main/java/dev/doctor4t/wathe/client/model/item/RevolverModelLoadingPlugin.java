package dev.doctor4t.wathe.client.model.item;

import dev.doctor4t.wathe.index.WatheCosmetics;
import dev.doctor4t.wathe.item.RevolverItem;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

public class RevolverModelLoadingPlugin implements ModelLoadingPlugin {

    public static final ModelIdentifier REVOLVER_MODEL_ID = ModelIdentifier.ofInventoryVariant(RevolverItem.ITEM_ID);

    public static Identifier getModelLocation(WatheCosmetics.ItemSkinsManager.Skin skin, Variant variant) {
        String skinPart = skin.getName().equalsIgnoreCase("default") ? "" : "_%s".formatted(skin.getName());
        String variantPart = variant == Variant.DEFAULT ? "" : "_%s".formatted(variant.asString());

        return REVOLVER_MODEL_ID.id().withPath(path -> "item/%s%s%s".formatted(path, skinPart, variantPart));
    }

    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        // make sure all models get loaded
        for (WatheCosmetics.ItemSkinsManager.Skin skin : WatheCosmetics.REVOLVER_SKINS_MANAGER.skinList) {
            for (Variant variant : Variant.values()) {
                pluginContext.addModels(getModelLocation(skin, variant));
            }
        }

        pluginContext.modifyModelOnLoad().register((unbakedModel, context) -> {
            // replace the original model with our custom one
            if (REVOLVER_MODEL_ID.equals(context.topLevelId())) {
                return new RevolverModel(unbakedModel);
            }

            return unbakedModel;
        });
    }

    public enum Variant implements StringIdentifiable {
        DEFAULT("default"),
        IN_HAND("in_hand");

        private final String name;

        Variant(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return name;
        }
    }
}
