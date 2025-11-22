package dev.doctor4t.trainmurdermystery.util;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.cca.PlayerMoodComponent;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import dev.doctor4t.trainmurdermystery.index.TMMDataComponentTypes;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import dev.doctor4t.trainmurdermystery.index.tag.TMMItemTags;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.sound.SoundCategory;
import org.jetbrains.annotations.NotNull;

public record GunShootPayload(int target) implements CustomPayload {
    public static final Id<GunShootPayload> ID = new Id<>(TMM.id("gunshoot"));
    public static final PacketCodec<PacketByteBuf, GunShootPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, GunShootPayload::target, GunShootPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<GunShootPayload> {
        @Override
        public void receive(@NotNull GunShootPayload payload, ServerPlayNetworking.@NotNull Context context) {
            var player = context.player();
            ItemStack mainHandStack = player.getMainHandStack();
            if (!mainHandStack.isIn(TMMItemTags.GUNS)) return;

            player.getWorld().playSound(null, player.getX(), player.getEyeY(), player.getZ(), TMMSounds.ITEM_REVOLVER_CLICK, SoundCategory.PLAYERS, 0.5f, 1f + player.getRandom().nextFloat() * .1f - .05f);

            // cancel if derringer has been shot
            Boolean isUsed = mainHandStack.get(TMMDataComponentTypes.USED);
            if (mainHandStack.isOf(TMMItems.DERRINGER)) {
                if(isUsed == null) {
                    isUsed = false;
                }

                if (isUsed) {
                    return;
                }

                if (!player.isCreative()) mainHandStack.set(TMMDataComponentTypes.USED, true);
            }

            if (player.getServerWorld().getEntityById(payload.target()) instanceof PlayerEntity target && target.distanceTo(player) < 65.0) {
                var game = GameWorldComponent.KEY.get(player.getWorld());
                Item revolver = TMMItems.REVOLVER;
                if (game.isInnocent(target) && !player.isCreative() && mainHandStack.isOf(revolver)) {
                    Scheduler.schedule(() -> {
                        if (!context.player().getInventory().contains((s) -> s.isIn(TMMItemTags.GUNS))) return;
                        player.getInventory().remove((s) -> s.isOf(revolver), 1, player.getInventory());
                        var item = player.dropItem(revolver.getDefaultStack(), false, false);
                        if (item != null) {
                            item.setPickupDelay(10);
                            item.setThrower(player);
                        }
                        ServerPlayNetworking.send(player, new GunDropPayload());
                        PlayerMoodComponent.KEY.get(player).setMood(0);
                    }, 4);
                }
                GameFunctions.killPlayer(target, true, player, TMM.id("gun_shot"));
            }

            player.getWorld().playSound(null, player.getX(), player.getEyeY(), player.getZ(), TMMSounds.ITEM_REVOLVER_SHOOT, SoundCategory.PLAYERS, 5f, 1f + player.getRandom().nextFloat() * .1f - .05f);

            for (var tracking : PlayerLookup.tracking(player))
                ServerPlayNetworking.send(tracking, new ShootMuzzleS2CPayload(player.getUuidAsString()));
            ServerPlayNetworking.send(player, new ShootMuzzleS2CPayload(player.getUuidAsString()));
            if (!player.isCreative())
                player.getItemCooldownManager().set(mainHandStack.getItem(), GameConstants.ITEM_COOLDOWNS.getOrDefault(mainHandStack.getItem(), 0));
        }
    }
}