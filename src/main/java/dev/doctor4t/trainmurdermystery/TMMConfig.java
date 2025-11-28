package dev.doctor4t.trainmurdermystery;

import dev.doctor4t.ratatouille.client.util.OptionLocker;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.MinecraftClient;

public class TMMConfig extends MidnightConfig {
    @Entry
    public static boolean ultraPerfMode = false;
    @Entry
    public static boolean disableScreenShake = false;
    @Entry
    public static SnowModeConfig snowOptLevel = SnowModeConfig.NO_OPTIMIZATION;

    @Override
    public void writeChanges(String modid) {
        super.writeChanges(modid);

        int lockedRenderDistance = TMMClient.getLockedRenderDistance(ultraPerfMode);
        OptionLocker.overrideOption("renderDistance", lockedRenderDistance);

        MinecraftClient.getInstance().options.viewDistance.setValue(lockedRenderDistance);
    }

    public enum SnowModeConfig {
        NO_OPTIMIZATION,  // Standard behavior: checking if the particle hit the block.
        STRONG_OPTIMIZATION, // replaces the calculation against the terrain to the calculation against a box that approximates the train
        TURN_OFF, // Client side '/tmm:setVisual snow false'
    }
}
