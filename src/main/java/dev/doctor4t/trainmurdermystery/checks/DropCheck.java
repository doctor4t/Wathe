package dev.doctor4t.trainmurdermystery.checks;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class DropCheck {

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(DropCheck::checkDrops);
    }

    private static void checkDrops(MinecraftServer server) {
        Box worldBox = new Box(-1e6, -1e6, -1e6, 1e6, 1e6, 1e6);

        for (World world : server.getWorlds()) {
            for (ItemEntity itemEntity : world.getEntitiesByClass(ItemEntity.class, worldBox, e -> true)) {

                Entity owner = itemEntity.getOwner();
                if (owner instanceof PlayerEntity playerOwner) {
                    if (playerOwner instanceof ServerPlayerEntity serverPlayer &&
                            serverPlayer.interactionManager.isCreative()) {
                        break;
                    }

                    ItemStack stack = itemEntity.getStack();
                    if (!stack.isEmpty()) {
                        boolean inserted = playerOwner.getInventory().insertStack(stack);
                        if (inserted) itemEntity.remove(RemovalReason.DISCARDED);
                    }
                }
            }
        }
    }
}
