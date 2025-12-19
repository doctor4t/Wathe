package dev.doctor4t.wathe.util;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.index.tag.WatheItemTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.NotNull;

public record GunDropPayload() implements CustomPayload {
    public static final Id<GunDropPayload> ID = new Id<>(Wathe.id("gundrop"));
    public static final PacketCodec<PacketByteBuf, GunDropPayload> CODEC = PacketCodec.unit(new GunDropPayload());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    @Environment(EnvType.CLIENT)
    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<GunDropPayload> {
        @Override
        public void receive(@NotNull GunDropPayload payload, ClientPlayNetworking.@NotNull Context context) {
            context.player().getInventory().remove((s) -> s.isIn(WatheItemTags.GUNS), 1, context.player().getInventory());
        }
    }
}