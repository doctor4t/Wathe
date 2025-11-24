package dev.doctor4t.trainmurdermystery.api;

import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
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
    default void registerEventListeners() {};
}
