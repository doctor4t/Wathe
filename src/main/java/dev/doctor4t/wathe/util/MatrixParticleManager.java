package dev.doctor4t.wathe.util;

import dev.doctor4t.wathe.client.WatheClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

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
