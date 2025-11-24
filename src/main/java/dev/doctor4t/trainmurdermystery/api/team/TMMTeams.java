package dev.doctor4t.trainmurdermystery.api.team;

import dev.doctor4t.trainmurdermystery.api.team.impl.InnocentTeam;
import dev.doctor4t.trainmurdermystery.api.team.impl.KillerTeam;
import dev.doctor4t.trainmurdermystery.api.team.impl.NeutralBenignTeam;
import dev.doctor4t.trainmurdermystery.api.team.impl.NeutralKillerTeam;

import java.util.ArrayList;

public class TMMTeams {

    /*
    The win con system is not currently in use and has some issues anyway. Will fix tomorrow.
     */

    public static final ArrayList<CustomTeam> TEAMS = new ArrayList<>();
    public static final CustomTeam KILLER = registerTeam(new KillerTeam());
    public static final CustomTeam INNOCENT = registerTeam(new InnocentTeam());
    public static final CustomTeam NEUTRAL_KILLER = registerTeam(new NeutralKillerTeam());
    public static final CustomTeam NEUTRAL_BENIGN = registerTeam(new NeutralBenignTeam());



    public static CustomTeam registerTeam(CustomTeam team) {
        TEAMS.add(team);
        return team;
    }
}
