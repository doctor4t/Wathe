<<<<<<<< HEAD:src/main/java/dev/doctor4t/trainmurdermystery/networking/StoreBuyC2SPayload.java
package dev.doctor4t.trainmurdermystery.networking;
========
package dev.doctor4t.wathe.util;
>>>>>>>> upstream/main:src/main/java/dev/doctor4t/wathe/util/StoreBuyPayload.java

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.cca.PlayerShopComponent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.NotNull;

<<<<<<<< HEAD:src/main/java/dev/doctor4t/trainmurdermystery/networking/StoreBuyC2SPayload.java
public record StoreBuyC2SPayload(int index) implements CustomPayload {
    public static final Id<StoreBuyC2SPayload> ID = new Id<>(TMM.id("storebuy"));
    public static final PacketCodec<PacketByteBuf, StoreBuyC2SPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, StoreBuyC2SPayload::index, StoreBuyC2SPayload::new);
========
public record StoreBuyPayload(int index) implements CustomPayload {
    public static final Id<StoreBuyPayload> ID = new Id<>(Wathe.id("storebuy"));
    public static final PacketCodec<PacketByteBuf, StoreBuyPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, StoreBuyPayload::index, StoreBuyPayload::new);
>>>>>>>> upstream/main:src/main/java/dev/doctor4t/wathe/util/StoreBuyPayload.java

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<StoreBuyC2SPayload> {
        @Override
        public void receive(@NotNull StoreBuyC2SPayload payload, ServerPlayNetworking.@NotNull Context context) {
            PlayerShopComponent.KEY.get(context.player()).tryBuy(payload.index());
        }
    }
}