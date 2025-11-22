package dev.doctor4t.trainmurdermystery.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.Entity;

import static net.fabricmc.fabric.api.event.EventFactory.createArrayBacked;

public interface IsPlayerPunchable {

    /**
     * Callback for determining whether a player can be punched.
     */
    Event<IsPlayerPunchable> EVENT = createArrayBacked(IsPlayerPunchable.class, listeners -> player -> {
        for (IsPlayerPunchable listener : listeners) {
            if (listener.gotPunchable(player)) {
                return true;
            }
        }
        return false;
    });

    boolean gotPunchable(Entity player);
}
