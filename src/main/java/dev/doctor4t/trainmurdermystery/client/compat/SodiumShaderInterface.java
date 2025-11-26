package dev.doctor4t.trainmurdermystery.client.compat;

import net.caffeinemc.mods.sodium.client.gl.buffer.GlMutableBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface SodiumShaderInterface {
    void tmm$set(GlMutableBuffer buffer);
}
