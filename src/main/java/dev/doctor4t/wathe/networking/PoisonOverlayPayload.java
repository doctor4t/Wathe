package dev.doctor4t.wathe.networking;

import dev.doctor4t.wathe.Wathe;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record PoisonOverlayPayload(String translationKey) implements CustomPayload {
    public static final Id<PoisonOverlayPayload> ID = new Id<>(Wathe.id("poisoned_text"));

    public static final PacketCodec<RegistryByteBuf, PoisonOverlayPayload> CODEC = PacketCodec.of(PoisonOverlayPayload::write, PoisonOverlayPayload::read);

    private void write(RegistryByteBuf buf) {
        buf.writeString(translationKey);
    }

    private static PoisonOverlayPayload read(RegistryByteBuf buf) {
        return new PoisonOverlayPayload(buf.readString());
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}