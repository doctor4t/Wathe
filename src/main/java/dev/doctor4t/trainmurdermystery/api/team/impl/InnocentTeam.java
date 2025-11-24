package dev.doctor4t.trainmurdermystery.api.team.impl;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.role.CustomRole;
import dev.doctor4t.trainmurdermystery.api.team.CustomTeam;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class InnocentTeam implements CustomTeam {

    @Override
    public Identifier id() {
        return TMM.id("innocents");
    }

    @Override
    public boolean isKiller() {
        return false;
    }

    @Override
    public boolean killsTeammates() {
        return false;
    }

    @Override
    public boolean showsOnEndScreen() {
        return true;
    }

    @Override
    public int endScreenColor(){
        return 0x36E51B;
    }

    @Override
    public boolean didWin(GameWorldComponent game, CustomRole role){
        for (UUID player : game.getAllKillerTeamPlayers()) {
            if (!GameFunctions.isPlayerEliminated(game.getWorld().getPlayerByUuid(player))) {
                return false;
            }
        }
        return true;
    }
}
