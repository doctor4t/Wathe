package dev.doctor4t.wathe.networking;

import dev.doctor4t.wathe.Wathe;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public final class AnnounceEndingS2CPayload implements CustomPayload {
    public static final AnnounceEndingS2CPayload INSTANCE = new AnnounceEndingS2CPayload();

    public static final Id<AnnounceEndingS2CPayload> ID = new Id<>(Wathe.id("announceending"));
    public static final PacketCodec<PacketByteBuf, AnnounceEndingS2CPayload> CODEC = PacketCodec.unit(AnnounceEndingS2CPayload.INSTANCE);

    private AnnounceEndingS2CPayload() {
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}