package dev.doctor4t.trainmurdermystery.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.player.PlayerEntity;

import static net.fabricmc.fabric.api.event.EventFactory.createArrayBacked;

public interface AllowPlayerPunching {

    /**
     * Callback for determining whether a player is allowed to punch another player,
     * for example when holding a knife.
     */
    Event<AllowPlayerPunching> EVENT = createArrayBacked(AllowPlayerPunching.class, listeners -> player -> {
        for (AllowPlayerPunching listener : listeners) {
            if (listener.allowPunching(player)) {
                return true;
            }
        }
        return false;
    });

    boolean allowPunching(PlayerEntity player);
}
