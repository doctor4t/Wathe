package dev.doctor4t.trainmurdermystery.api.role;

import dev.doctor4t.trainmurdermystery.api.team.CustomTeam;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public interface CustomRole {
    boolean forcesDroppedGun();
    boolean canPickupGun();
    boolean hasMood();
    default boolean didWin(GameWorldComponent game) { return team().didWin(game, this); }
    Identifier id();
    int color();
    CustomTeam team();
    RoleAnnouncementTexts.RoleAnnouncementText announcementText();

    default boolean allowPlayerDeathEvent(PlayerEntity victim, PlayerEntity killer, Identifier id) { return true; }
    default boolean allowPlayerOpenLockedDoorEvent(PlayerEntity player) { return true; }
    default boolean allowPunch(PlayerEntity attacker, PlayerEntity victim) { return true; }
    default boolean canSeePoison(PlayerEntity player) { return true; }
    default boolean shouldDropOnDeath(PlayerEntity victim, ItemStack item) {return true; }
}
