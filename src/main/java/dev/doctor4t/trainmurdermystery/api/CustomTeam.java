package dev.doctor4t.trainmurdermystery.api;

import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import net.minecraft.util.Identifier;

public interface CustomTeam {
    Identifier id();
    boolean isKiller();
    boolean killsTeammates();
    boolean showsOnEndScreen();
    boolean didWin(GameWorldComponent end, CustomRole role);
    int endScreenColor();
}
