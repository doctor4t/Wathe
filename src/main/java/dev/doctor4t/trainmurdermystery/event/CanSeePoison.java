package dev.doctor4t.trainmurdermystery.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.Entity;

import static net.fabricmc.fabric.api.event.EventFactory.createArrayBacked;

public interface CanSeePoison {

    /**
     * Callback for determining whether a player can see poison particles on beverage plate.
     */
    Event<CanSeePoison> EVENT = createArrayBacked(CanSeePoison.class, listeners -> player -> {
        for (CanSeePoison listener : listeners) {
            if (listener.visible(player)) {
                return true;
            }
        }
        return false;
    });

    boolean visible(Entity player);
}
