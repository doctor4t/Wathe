package dev.doctor4t.trainmurdermystery.event;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import static net.fabricmc.fabric.api.event.EventFactory.createArrayBacked;

public interface AllowPlayerDeath {

    /**
     * Event callback to determine if a player is allowed to die for a specific death type.
     * The game currently has the following death type names defined:
     * 'fell_out_of_train', 'poison', 'grenade', 'bat_hit', 'gun_shot', 'knife_stab'.
     * Any other death type not explicitly defined will default to 'generic'.
     */
    Event<AllowPlayerDeath> EVENT = createArrayBacked(AllowPlayerDeath.class, listeners -> (victim, killer, identifier) -> {
        for (AllowPlayerDeath listener : listeners) {
            if (!listener.allowDeath(victim, killer, identifier)) {
                return false;
            }
        }
        return true;
    });

    boolean allowDeath(PlayerEntity victim, PlayerEntity killer, Identifier identifier);
}