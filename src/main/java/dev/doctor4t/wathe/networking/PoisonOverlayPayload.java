package dev.doctor4t.wathe.networking;

import dev.doctor4t.wathe.Wathe;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

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

    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<PoisonOverlayPayload> {
        @Override
        public void receive(@NotNull PoisonOverlayPayload payload, ClientPlayNetworking.@NotNull Context context) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.execute(() -> client.inGameHud.setOverlayMessage(Text.translatable(payload.translationKey()), false));
        }
    }
}