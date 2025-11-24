package dev.doctor4t.trainmurdermystery.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.player.PlayerEntity;

import static net.fabricmc.fabric.api.event.EventFactory.createArrayBacked;

public interface AllowPunch {

    /**
     * Callback for determining whether a player is allowed to punch another player,
     * for example when holding a knife.
     */
    Event<AllowPunch> EVENT = createArrayBacked(AllowPunch.class, listeners -> (player, target) -> {
        for (AllowPunch listener : listeners) {
            if (listener.allowPunching(player, target)) {
                return true;
            }
        }
        return false;
    });

    boolean allowPunching(PlayerEntity player, PlayerEntity target);
}
