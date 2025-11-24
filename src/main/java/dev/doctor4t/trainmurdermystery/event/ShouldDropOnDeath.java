package dev.doctor4t.trainmurdermystery.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import static net.fabricmc.fabric.api.event.EventFactory.createArrayBacked;

public interface ShouldDropOnDeath {

    /**
     * Callback for determining whether an {@link ItemStack} should drop when player died
     */
    Event<ShouldDropOnDeath> EVENT = createArrayBacked(ShouldDropOnDeath.class, listeners -> (victim, stack) -> {
        for (ShouldDropOnDeath listener : listeners) {
            if (listener.shouldDrop(victim, stack)) {
                return true;
            }
        }
        return false;
    });

    boolean shouldDrop(PlayerEntity victim, ItemStack stack);
}
