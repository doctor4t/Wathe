package dev.doctor4t.trainmurdermystery.api.team.impl;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.role.CustomRole;
import dev.doctor4t.trainmurdermystery.api.team.CustomTeam;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class KillerTeam implements CustomTeam {

    @Override
    public Identifier id() {
        return TMM.id("killers");
    }

    @Override
    public boolean isKiller() {
        return true;
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
        return 0xC13838;
    }

    @Override
    public boolean didWin(GameWorldComponent game, CustomRole role){
        List<CustomRole> roles = new ArrayList<>();
        for (var player : game.getWorld().getPlayers())
            if (GameFunctions.isPlayerAliveAndSurvival(player))
                roles.add(game.getRole(player));
        return roles.stream().allMatch(v -> v.id() == role.id());
    }
}
