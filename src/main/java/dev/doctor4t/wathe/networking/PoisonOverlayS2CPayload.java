package dev.doctor4t.wathe.networking;

import dev.doctor4t.wathe.Wathe;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record PoisonOverlayS2CPayload(String translationKey) implements CustomPayload {
    public static final Id<PoisonOverlayS2CPayload> ID = new Id<>(Wathe.id("poisoned_text"));

    public static final PacketCodec<RegistryByteBuf, PoisonOverlayS2CPayload> CODEC = PacketCodec.of(PoisonOverlayS2CPayload::write, PoisonOverlayS2CPayload::read);

    private void write(RegistryByteBuf buf) {
        buf.writeString(translationKey);
    }

    private static PoisonOverlayS2CPayload read(RegistryByteBuf buf) {
        return new PoisonOverlayS2CPayload(buf.readString());
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}