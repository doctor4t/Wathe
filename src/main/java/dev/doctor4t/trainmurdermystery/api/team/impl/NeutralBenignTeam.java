package dev.doctor4t.trainmurdermystery.api.team.impl;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.role.CustomRole;
import dev.doctor4t.trainmurdermystery.api.team.CustomTeam;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class NeutralBenignTeam implements CustomTeam {
    @Override
    public Identifier id() {
        return TMM.id("neutral_benigns");
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
        return 0x502878;
    }

    @Override
    public boolean didWin(GameWorldComponent game, CustomRole role){
        return false;
    }
}
