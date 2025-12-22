package dev.doctor4t.wathe.networking;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.cca.PlayerNoteComponent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.NotNull;

public record NoteEditC2SPayload(String line1, String line2, String line3, String line4) implements CustomPayload {
    public static final Id<NoteEditC2SPayload> ID = new Id<>(Wathe.id("note"));
    public static final PacketCodec<PacketByteBuf, NoteEditC2SPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, NoteEditC2SPayload::line1, PacketCodecs.STRING, NoteEditC2SPayload::line2, PacketCodecs.STRING, NoteEditC2SPayload::line3, PacketCodecs.STRING, NoteEditC2SPayload::line4, NoteEditC2SPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<NoteEditC2SPayload> {
        @Override
        public void receive(@NotNull NoteEditC2SPayload payload, ServerPlayNetworking.@NotNull Context context) {
            PlayerNoteComponent.KEY.get(context.player()).setNote(payload.line1(), payload.line2(), payload.line3(), payload.line4());
        }
    }
}