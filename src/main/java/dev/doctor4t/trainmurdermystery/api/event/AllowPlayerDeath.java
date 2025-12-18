package dev.doctor4t.trainmurdermystery.api.event;

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
     * @see dev.doctor4t.trainmurdermystery.game.GameConstants.DeathReasons
     */
    Event<AllowPlayerDeath> EVENT = createArrayBacked(AllowPlayerDeath.class, listeners -> (victim, killer, deathReason) -> {
        for (AllowPlayerDeath listener : listeners) {
            if (!listener.allowDeath(victim, killer, deathReason)) {
                return false;
            }
        }
        return true;
    });

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean allowDeath(PlayerEntity victim, PlayerEntity killer, Identifier deathReason);
}