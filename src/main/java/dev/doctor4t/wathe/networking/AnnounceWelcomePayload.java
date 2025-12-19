package dev.doctor4t.wathe.networking;

import dev.doctor4t.wathe.Wathe;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record AnnounceWelcomePayload(int role, int killers, int targets) implements CustomPayload {
    public static final Id<AnnounceWelcomePayload> ID = new Id<>(Wathe.id("announcewelcome"));
    public static final PacketCodec<PacketByteBuf, AnnounceWelcomePayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, AnnounceWelcomePayload::role, PacketCodecs.INTEGER, AnnounceWelcomePayload::killers, PacketCodecs.INTEGER, AnnounceWelcomePayload::targets, AnnounceWelcomePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}