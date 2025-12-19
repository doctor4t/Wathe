package dev.doctor4t.wathe.networking;

import dev.doctor4t.wathe.Wathe;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public final class AnnounceEndingPayload implements CustomPayload {
    public static final AnnounceEndingPayload INSTANCE = new AnnounceEndingPayload();

    public static final Id<AnnounceEndingPayload> ID = new Id<>(Wathe.id("announceending"));
    public static final PacketCodec<PacketByteBuf, AnnounceEndingPayload> CODEC = PacketCodec.unit(AnnounceEndingPayload.INSTANCE);

    private AnnounceEndingPayload() {
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}