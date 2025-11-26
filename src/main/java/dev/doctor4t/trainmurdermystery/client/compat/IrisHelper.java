package dev.doctor4t.trainmurdermystery.client.compat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.irisshaders.iris.api.v0.IrisApi;

@Environment(EnvType.CLIENT)
public class IrisHelper {

    public static boolean isIrisShaderPackInUse() {
        if (!FabricLoader.getInstance().isModLoaded("iris"))
            return false;

        return IrisApi.getInstance().isShaderPackInUse();
    }
}
