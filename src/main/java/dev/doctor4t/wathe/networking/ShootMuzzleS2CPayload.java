package dev.doctor4t.wathe.networking;

import dev.doctor4t.wathe.Wathe;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record ShootMuzzleS2CPayload(UUID shooterUuid) implements CustomPayload {
    public static final Id<ShootMuzzleS2CPayload> ID = new Id<>(Wathe.id("shoot_muzzle_s2c"));
    public static final PacketCodec<PacketByteBuf, ShootMuzzleS2CPayload> CODEC = PacketCodec.tuple(Uuids.PACKET_CODEC, ShootMuzzleS2CPayload::shooterUuid, ShootMuzzleS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}