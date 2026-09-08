package dev.doctor4t.wathe.item;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

public interface AttackUseItem {
    void triggerAttackUseClient(ClientPlayerEntity player);
    void triggerAttackUseServer(ServerPlayerEntity player);
}
