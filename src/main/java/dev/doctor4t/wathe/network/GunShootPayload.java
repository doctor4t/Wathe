package dev.doctor4t.wathe.network;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.cca.GameWorldComponent;
import dev.doctor4t.wathe.cca.PlayerMoodComponent;
import dev.doctor4t.wathe.cca.PlayerVariablesComponent;
import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.game.GameFunctions;
import dev.doctor4t.wathe.index.WatheDataComponentTypes;
import dev.doctor4t.wathe.index.WatheItems;
import dev.doctor4t.wathe.index.WatheSounds;
import dev.doctor4t.wathe.index.tag.WatheItemTags;
import dev.doctor4t.wathe.util.Scheduler;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

public record GunShootPayload(int target) implements CustomPayload {
    public static final Id<GunShootPayload> ID = new Id<>(Wathe.id("gunshoot"));
    public static final PacketCodec<PacketByteBuf, GunShootPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, GunShootPayload::target, GunShootPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<GunShootPayload> {
        @Override
        public void receive(@NotNull GunShootPayload payload, ServerPlayNetworking.@NotNull Context context) {
            ServerPlayerEntity player = context.player();
            ItemStack mainHandStack = player.getMainHandStack();
            if (!mainHandStack.isIn(WatheItemTags.GUNS)) return;
            if (player.getItemCooldownManager().isCoolingDown(mainHandStack.getItem())) return;

            player.getWorld().playSound(null, player.getX(), player.getEyeY(), player.getZ(), WatheSounds.ITEM_REVOLVER_CLICK, SoundCategory.PLAYERS, 0.5f, 1f + player.getRandom().nextFloat() * .1f - .05f);

            // cancel if derringer has been shot
            Boolean isUsed = mainHandStack.get(WatheDataComponentTypes.USED);
            if (mainHandStack.isOf(WatheItems.DERRINGER)) {
                if (isUsed == null) {
                    isUsed = false;
                }

                if (isUsed) {
                    return;
                }

                if (!player.isCreative()) mainHandStack.set(WatheDataComponentTypes.USED, true);
            }

            if (player.getServerWorld().getEntityById(payload.target()) instanceof PlayerEntity target && target.distanceTo(player) < 65.0) {
                GameWorldComponent game = GameWorldComponent.KEY.get(player.getWorld());
                Item revolver = WatheItems.REVOLVER;

                boolean backfire = false;

                if (!player.isCreative() && mainHandStack.isOf(revolver)) {
                    PlayerVariablesComponent playerVariablesComponent = PlayerVariablesComponent.KEY.get(player);

                    if (game.isInnocent(target)) {
                        // drop gun and lower mood
                        Scheduler.schedule(() -> {
                            if (!context.player().getInventory().contains((s) -> s.isIn(WatheItemTags.GUNS))) return;
                            player.getInventory().remove((s) -> s.isOf(revolver), 1, player.getInventory());
                            ItemEntity item = player.dropItem(revolver.getDefaultStack(), false, false);
                            if (item != null) {
                                item.setPickupDelay(10);
                                item.setThrower(player);
                            }
                            ServerPlayNetworking.send(player, new GunDropPayload());
                            PlayerMoodComponent.KEY.get(player).setMood(0);
                        }, 4);

                        // backfire: if you kill an innocent and are not a killer you have a chance of shooting yourself instead
                        if (game.isInnocent(player)) {
                            if (player.getRandom().nextFloat() <= playerVariablesComponent.getInnocentKills() * game.getBackfireChancePerInnocentKill()) {
                                backfire = true;
                                player.sendMessage(Text.translatable("game.backfire").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)), true);
                                GameFunctions.killPlayer(player, true, player, GameConstants.DeathReasons.GUN);
                            }

                            playerVariablesComponent.incrementInnocentKills();
                        }
                    } else {
                        playerVariablesComponent.setInnocentKills(0);
                    }
                }

                if (!backfire) {
                    GameFunctions.killPlayer(target, true, player, GameConstants.DeathReasons.GUN);
                }
            }

            player.getWorld().playSound(null, player.getX(), player.getEyeY(), player.getZ(), WatheSounds.ITEM_REVOLVER_SHOOT, SoundCategory.PLAYERS, 5f, 1f + player.getRandom().nextFloat() * .1f - .05f);

            for (ServerPlayerEntity tracking : PlayerLookup.tracking(player))
                ServerPlayNetworking.send(tracking, new ShootMuzzleS2CPayload(player.getUuidAsString()));
            ServerPlayNetworking.send(player, new ShootMuzzleS2CPayload(player.getUuidAsString()));
            if (!player.isCreative())
                player.getItemCooldownManager().set(mainHandStack.getItem(), GameConstants.ITEM_COOLDOWNS.getOrDefault(mainHandStack.getItem(), 0));
        }
    }
}