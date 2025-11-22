package dev.doctor4t.trainmurdermystery.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.Entity;

import static net.fabricmc.fabric.api.event.EventFactory.createArrayBacked;

public interface AllowPlayerOpenLockedDoor {

    /**
     * Callback for determining whether a player can open locked door.
     */
    Event<AllowPlayerOpenLockedDoor> EVENT = createArrayBacked(AllowPlayerOpenLockedDoor.class, listeners -> player -> {
        for (AllowPlayerOpenLockedDoor listener : listeners) {
            if (listener.allowOpen(player)) {
                return true;
            }
        }
        return false;
    });

    boolean allowOpen(Entity player);
}
