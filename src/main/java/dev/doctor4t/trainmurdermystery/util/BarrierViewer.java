package dev.doctor4t.trainmurdermystery.util;

import java.util.function.Supplier;

public class BarrierViewer {
    public static Supplier<Boolean> isVisible = null;

    public static boolean isBarrierVisible() {
        if (isVisible == null) {
            try {
                Class<?> clazz = Class.forName("xyz.amymialee.visiblebarriers.VisibleBarriers");
                isVisible = () -> {
                    try {
                        return (boolean) clazz.getField("toggleVisible").getBoolean(null);
                    } catch (Exception e) {
                        return false;
                    }
                };
            } catch (Exception ignored) {
                isVisible = () -> false;
            }
        }
        return isVisible.get();
    }
}
