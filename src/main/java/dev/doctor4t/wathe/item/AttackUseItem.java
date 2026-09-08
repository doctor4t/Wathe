package dev.doctor4t.wathe.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public interface AttackUseItem {
    void triggerAttackUseClient(PlayerEntity player);

    void triggerAttackUseServer(ServerPlayerEntity player);
}
