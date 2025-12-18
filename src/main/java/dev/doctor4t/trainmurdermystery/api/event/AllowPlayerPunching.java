package dev.doctor4t.trainmurdermystery.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.player.PlayerEntity;

import static net.fabricmc.fabric.api.event.EventFactory.createArrayBacked;

public interface AllowPlayerPunching {

    /**
     * Callback for determining whether a player is allowed to punch another player.
     */
    Event<AllowPlayerPunching> EVENT = createArrayBacked(AllowPlayerPunching.class, listeners -> (attacker, victim) -> {
        for (AllowPlayerPunching listener : listeners) {
            if (listener.allowPunching(attacker, victim)) {
                return true;
            }
        }
        return false;
    });

    boolean allowPunching(PlayerEntity attacker, PlayerEntity victim);
}
