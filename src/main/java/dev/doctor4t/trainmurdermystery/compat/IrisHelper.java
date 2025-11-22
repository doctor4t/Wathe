package dev.doctor4t.trainmurdermystery.compat;

import net.fabricmc.loader.api.FabricLoader;
import net.irisshaders.iris.api.v0.IrisApi;

public class IrisHelper {

    public static boolean isIrisShaderPackInUse() {
        if (!FabricLoader.getInstance().isModLoaded("iris"))
            return false;

        return IrisApi.getInstance().isShaderPackInUse();
    }
}
