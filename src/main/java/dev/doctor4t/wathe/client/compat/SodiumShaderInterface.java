package dev.doctor4t.wathe.client.compat;

import net.caffeinemc.mods.sodium.client.gl.buffer.GlMutableBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface SodiumShaderInterface {
    void wathe$set(GlMutableBuffer buffer);
}
