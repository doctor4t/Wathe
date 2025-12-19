package dev.doctor4t.wathe;

import dev.doctor4t.ratatouille.client.util.OptionLocker;
import dev.doctor4t.wathe.client.WatheClient;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.MinecraftClient;

public class WatheConfig extends MidnightConfig {
    @Entry
    public static boolean ultraPerfMode = false;
    @Entry
    public static boolean disableScreenShake = false;

    @Override
    public void writeChanges(String modid) {
        super.writeChanges(modid);

        int lockedRenderDistance = WatheClient.getLockedRenderDistance(ultraPerfMode);
        OptionLocker.overrideOption("renderDistance", lockedRenderDistance);

        MinecraftClient.getInstance().options.viewDistance.setValue(lockedRenderDistance);
    }
}
