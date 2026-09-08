package dev.doctor4t.wathe.network;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.item.AttackUseItem;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

public record AttackUsePayload() implements CustomPayload {
    public static final Id<AttackUsePayload> ID = new Id<>(Wathe.id("attackuse"));
    public static final PacketCodec<PacketByteBuf, AttackUsePayload> CODEC = PacketCodec.unit(new AttackUsePayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<AttackUsePayload> {
        @Override
        public void receive(@NotNull AttackUsePayload payload, ServerPlayNetworking.@NotNull Context context) {
            ServerPlayerEntity player = context.player();
            if (player.getMainHandStack().getItem() instanceof AttackUseItem attackUseItem) {
                attackUseItem.triggerAttackUseServer(player);
            }
        }
    }
}