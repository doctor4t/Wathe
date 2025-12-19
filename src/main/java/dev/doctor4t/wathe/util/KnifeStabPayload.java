<<<<<<<< HEAD:src/main/java/dev/doctor4t/trainmurdermystery/networking/KnifeStabC2SPayload.java
package dev.doctor4t.trainmurdermystery.networking;
========
package dev.doctor4t.wathe.util;
>>>>>>>> upstream/main:src/main/java/dev/doctor4t/wathe/util/KnifeStabPayload.java

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.api.WatheGameModes;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.doctor4t.wathe.index.WatheItems;
import dev.doctor4t.wathe.index.WatheSounds;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.NotNull;

<<<<<<<< HEAD:src/main/java/dev/doctor4t/trainmurdermystery/networking/KnifeStabC2SPayload.java
public record KnifeStabC2SPayload(int target) implements CustomPayload {
    public static final Id<KnifeStabC2SPayload> ID = new Id<>(TMM.id("knifestab"));
    public static final PacketCodec<PacketByteBuf, KnifeStabC2SPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, KnifeStabC2SPayload::target, KnifeStabC2SPayload::new);
========
public record KnifeStabPayload(int target) implements CustomPayload {
    public static final Id<KnifeStabPayload> ID = new Id<>(Wathe.id("knifestab"));
    public static final PacketCodec<PacketByteBuf, KnifeStabPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, KnifeStabPayload::target, KnifeStabPayload::new);
>>>>>>>> upstream/main:src/main/java/dev/doctor4t/wathe/util/KnifeStabPayload.java

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<KnifeStabC2SPayload> {
        @Override
        public void receive(@NotNull KnifeStabC2SPayload payload, ServerPlayNetworking.@NotNull Context context) {
            ServerPlayerEntity player = context.player();
            if (!(player.getServerWorld().getEntityById(payload.target()) instanceof PlayerEntity target)) return;
            if (target.distanceTo(player) > 3.0) return;
            GameFunctions.killPlayer(target, true, player, GameConstants.DeathReasons.KNIFE);
            target.playSound(WatheSounds.ITEM_KNIFE_STAB, 1.0f, 1.0f);
            player.swingHand(Hand.MAIN_HAND);
            if (!player.isCreative() && GameWorldComponent.KEY.get(context.player().getWorld()).getGameMode() != WatheGameModes.LOOSE_ENDS) {
                player.getItemCooldownManager().set(WatheItems.KNIFE, GameConstants.ITEM_COOLDOWNS.get(WatheItems.KNIFE));
            }
        }
    }
}