package dev.doctor4t.wathe.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import static net.fabricmc.fabric.api.event.EventFactory.createArrayBacked;

public interface ShouldDropOnDeath {

    /**
     * Callback for determining whether an {@link ItemStack} should drop when player died
     */
    Event<ShouldDropOnDeath> EVENT = createArrayBacked(ShouldDropOnDeath.class, listeners -> (stack, victim) -> {
        for (ShouldDropOnDeath listener : listeners) {
            if (listener.shouldDrop(stack, victim)) {
                return true;
            }
        }
        return false;
    });

    boolean shouldDrop(ItemStack stack, PlayerEntity victim);
}
