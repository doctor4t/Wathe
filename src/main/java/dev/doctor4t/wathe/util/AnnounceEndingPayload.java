package dev.doctor4t.wathe.util;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.client.gui.RoundTextRenderer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.NotNull;

public record AnnounceEndingPayload() implements CustomPayload {
    public static final Id<AnnounceEndingPayload> ID = new Id<>(Wathe.id("announceending"));
    public static final PacketCodec<PacketByteBuf, AnnounceEndingPayload> CODEC = PacketCodec.unit(new AnnounceEndingPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<AnnounceEndingPayload> {
        @Override
        public void receive(@NotNull AnnounceEndingPayload payload, ClientPlayNetworking.@NotNull Context context) {
            RoundTextRenderer.startEnd();
        }
    }
}