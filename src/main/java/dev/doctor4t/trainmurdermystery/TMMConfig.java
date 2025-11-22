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
    public static ParticleAmount particleAmount = ParticleAmount.A_LOT;

    public enum ParticleAmount {
        A_LOT(100),
        NOT_A_LOT(50),
        A_TINY_BIT(25),
        NONE(0);

        public final int multiplier;

        ParticleAmount(int multiplier) {
            this.multiplier = multiplier;
        }
    }

    @Override
    public void writeChanges(String modid) {
        super.writeChanges(modid);

        int lockedRenderDistance = TMMClient.getLockedRenderDistance(ultraPerfMode);
        OptionLocker.overrideOption("renderDistance", lockedRenderDistance);

        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && client.options != null) {
            client.options.viewDistance.setValue(lockedRenderDistance);
        }
    }
}
