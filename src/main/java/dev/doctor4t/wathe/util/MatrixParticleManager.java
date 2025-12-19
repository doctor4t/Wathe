<<<<<<<< HEAD:src/main/java/dev/doctor4t/wathe/client/util/MatrixParticleManager.java
package dev.doctor4t.trainmurdermystery.client.util;

import dev.doctor4t.trainmurdermystery.client.TMMClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
========
package dev.doctor4t.wathe.util;

import dev.doctor4t.wathe.client.WatheClient;
>>>>>>>> upstream/main:src/main/java/dev/doctor4t/wathe/util/MatrixParticleManager.java
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public interface MatrixParticleManager {
    static Vec3d getMuzzlePosForPlayer(PlayerEntity playerEntity) {
        Vec3d pos = WatheClient.particleMap.getOrDefault(playerEntity, null);
        WatheClient.particleMap.remove(playerEntity);
        return pos;
    }

    static void setMuzzlePosForPlayer(PlayerEntity playerEntity, Vec3d vec3d) {
        WatheClient.particleMap.put(playerEntity, vec3d);
    }
}
