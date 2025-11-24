package dev.doctor4t.trainmurdermystery.api;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.cca.GameWorldComponent;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiFunction;

public class TMMTeams {

    /*
    The win con system is not currently in use and has some issues anyway. Will fix tomorrow.
     */

    public static final ArrayList<CustomTeam> TEAMS = new ArrayList<>();
    public static final CustomTeam KILLER = registerTeam(buildAnonymousTeam(TMM.id("killers"), true, false, true, 0xC13838, (game, role) -> {
        List<CustomRole> roles = new ArrayList<>();
        for (var player : game.getWorld().getPlayers())
            if (GameFunctions.isPlayerAliveAndSurvival(player))
                roles.add(game.getRole(player));
        return roles.stream().allMatch(v -> v.id() == role.id());
    }));
    public static final CustomTeam INNOCENT = registerTeam(buildAnonymousTeam(TMM.id("innocents"), false, false, true, 0x36E51B, (game, role) -> {
        for (UUID player : game.getAllKillerTeamPlayers()) {
            if (!GameFunctions.isPlayerEliminated(game.getWorld().getPlayerByUuid(player))) {
                return false;
            }
        }
        return true;
    }));
    public static final CustomTeam NEUTRAL_KILLER = registerTeam(buildAnonymousTeam(TMM.id("neutral_killers"), true, true, true, 0xB4B5B5, (game, role) -> {
        List<CustomRole> roles = new ArrayList<>();
        for (var player : game.getWorld().getPlayers())
            if (GameFunctions.isPlayerAliveAndSurvival(player))
                roles.add(game.getRole(player));
        return roles.stream().allMatch(v -> v.id() == role.id());
    }));
    public static final CustomTeam NEUTRAL_BENIGN = registerTeam(buildAnonymousTeam(TMM.id("neutral_benigns"), false, false, true, 0x502878, (game, role) -> false));
    // non-killing neutral roles usually have custom win cons like jester



    public static CustomTeam registerTeam(CustomTeam team) {
        TEAMS.add(team);
        return team;
    }

    public static CustomTeam buildAnonymousTeam(Identifier id, boolean isKiller, boolean killsTeammates, boolean showsOnEndScreen, int color, BiFunction<GameWorldComponent, CustomRole, Boolean> didWin){
        return new CustomTeam() {
            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public boolean isKiller() {
                return isKiller;
            }

            @Override
            public boolean killsTeammates() {
                return killsTeammates;
            }

            @Override
            public boolean showsOnEndScreen() {
                return showsOnEndScreen;
            }

            @Override
            public int endScreenColor(){
                return color;
            }

            @Override
            public boolean didWin(GameWorldComponent game, CustomRole role){
                return didWin.apply(game, role);
            }
        };
    }
}
