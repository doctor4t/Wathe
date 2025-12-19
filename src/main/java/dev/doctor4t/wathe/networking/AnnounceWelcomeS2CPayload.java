package dev.doctor4t.wathe.networking;

import dev.doctor4t.wathe.Wathe;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record AnnounceWelcomeS2CPayload(int role, int killers, int targets) implements CustomPayload {
    public static final Id<AnnounceWelcomeS2CPayload> ID = new Id<>(Wathe.id("announcewelcome"));
    public static final PacketCodec<PacketByteBuf, AnnounceWelcomeS2CPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, AnnounceWelcomeS2CPayload::role, PacketCodecs.INTEGER, AnnounceWelcomeS2CPayload::killers, PacketCodecs.INTEGER, AnnounceWelcomeS2CPayload::targets, AnnounceWelcomeS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}