package dev.doctor4t.wathe.networking;

import dev.doctor4t.wathe.Wathe;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public final class GunDropPayload implements CustomPayload {
    public static final GunDropPayload INSTANCE = new GunDropPayload();

    public static final Id<GunDropPayload> ID = new Id<>(Wathe.id("gundrop"));
    public static final PacketCodec<PacketByteBuf, GunDropPayload> CODEC = PacketCodec.unit(INSTANCE);

    private GunDropPayload() {
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}