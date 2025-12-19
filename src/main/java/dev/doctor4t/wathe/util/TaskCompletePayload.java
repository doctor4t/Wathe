package dev.doctor4t.wathe.util;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.client.gui.MoodRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.NotNull;

public record TaskCompletePayload() implements CustomPayload {
    public static final Id<TaskCompletePayload> ID = new Id<>(Wathe.id("taskcomplete"));
    public static final PacketCodec<PacketByteBuf, TaskCompletePayload> CODEC = PacketCodec.unit(new TaskCompletePayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    @Environment(EnvType.CLIENT)
    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<TaskCompletePayload> {
        @Override
        public void receive(@NotNull TaskCompletePayload payload, ClientPlayNetworking.@NotNull Context context) {
            MoodRenderer.arrowProgress = 1f;
        }
    }
}