package dev.doctor4t.wathe.util;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.NotNull;

public record StoreBuyPayload(int index) implements CustomPayload {
    public static final Id<StoreBuyPayload> ID = new Id<>(Wathe.id("storebuy"));
    public static final PacketCodec<PacketByteBuf, StoreBuyPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, StoreBuyPayload::index, StoreBuyPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<StoreBuyPayload> {
        @Override
        public void receive(@NotNull StoreBuyPayload payload, ServerPlayNetworking.@NotNull Context context) {
            PlayerShopComponent.KEY.get(context.player()).tryBuy(payload.index());
        }
    }
}