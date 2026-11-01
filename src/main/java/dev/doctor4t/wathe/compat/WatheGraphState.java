package dev.doctor4t.wathe.compat;

import dev.doctor4t.wathe.WatheConfig;
import dev.doctor4t.wathe.client.WatheClient;

// I hate this. it is dirty trickery and it's using a hardcoded 20 seconds but it works so I DONT CARE
public class WatheGraphState {
    public static boolean mainDirty;
    public static int invalidateFrames = 0;
    private static boolean prevShaders = false;
    private static long warmupUntilMs = 0;
    private static int completeFrames = 0;

    public static boolean shouldDisableDirty() {
        if (invalidateFrames > 0) return false;
        if (!WatheConfig.sodiumDisableGraphDirtying || !WatheClient.isTrainMoving()) return false;

        boolean shaders = IrisHelper.isIrisShaderPackInUse();
        if (!shaders) {
            prevShaders = false;
            return true;
        }

        long now = System.currentTimeMillis();
        if (!prevShaders) {
            prevShaders = true;
            warmupUntilMs = now + 20_000;
            completeFrames = 0;
        }
        if (now < warmupUntilMs) return false;

        if (!isBuildComplete()) { completeFrames = 0; return false; }
        return ++completeFrames > 120;
    }

    private static boolean isBuildComplete() {
        var r = net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer.instanceNullable();
        return r != null && r.isTerrainRenderComplete();
    }
}
