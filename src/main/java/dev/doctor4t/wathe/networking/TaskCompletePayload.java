package dev.doctor4t.wathe.networking;

import dev.doctor4t.wathe.Wathe;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public final class TaskCompletePayload implements CustomPayload {
    public static final TaskCompletePayload INSTANCE = new TaskCompletePayload();

    public static final Id<TaskCompletePayload> ID = new Id<>(Wathe.id("taskcomplete"));
    public static final PacketCodec<PacketByteBuf, TaskCompletePayload> CODEC = PacketCodec.unit(INSTANCE);

    private TaskCompletePayload() {
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}