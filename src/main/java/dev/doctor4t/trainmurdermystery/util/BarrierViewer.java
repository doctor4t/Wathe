package dev.doctor4t.trainmurdermystery.util;

import java.util.function.Supplier;

public class BarrierViewer {
    private static Class<?> clazz = null;
    public static Supplier<Boolean> isVisible = null;

    // This should probably be checked with FabricLoader#isModLoaded - SkyNotTheLimit
    public static boolean isBarrierVisible() {
        if (isVisible == null) {
            try {
                if (clazz == null) {
                    clazz = Class.forName("xyz.amymialee.visiblebarriers.VisibleBarriers");
                }
                isVisible = () -> {
                    try {
                        return (boolean) clazz.getField("toggleVisible").getBoolean(null);
                    } catch (Exception e) {
                        return false;
                    }
                };
            } catch (Exception ignored) {
                clazz = null;
                isVisible = () -> false;
            }
        }
        return isVisible.get();
    }
}
