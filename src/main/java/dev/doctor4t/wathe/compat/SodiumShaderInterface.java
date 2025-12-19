<<<<<<<< HEAD:src/main/java/dev/doctor4t/trainmurdermystery/client/compat/SodiumShaderInterface.java
package dev.doctor4t.trainmurdermystery.client.compat;
========
package dev.doctor4t.wathe.compat;
>>>>>>>> upstream/main:src/main/java/dev/doctor4t/wathe/compat/SodiumShaderInterface.java

import net.caffeinemc.mods.sodium.client.gl.buffer.GlMutableBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface SodiumShaderInterface {
    void wathe$set(GlMutableBuffer buffer);
}
