package dev.doctor4t.wathe.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.shadows.ShadowRenderingState;

public class IrisHelper {

    public static boolean isIrisShaderPackInUse() {
        if (!FabricLoader.getInstance().isModLoaded("iris"))
            return false;

        return IrisApi.getInstance().isShaderPackInUse();
    }

    public static boolean isShadowPass() {
        if (!FabricLoader.getInstance().isModLoaded("iris")) return false;

        return isIrisShaderPackInUse() && ShadowRenderingState.areShadowsCurrentlyBeingRendered();
    }
}
