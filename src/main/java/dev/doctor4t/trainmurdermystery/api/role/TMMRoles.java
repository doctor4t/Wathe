package dev.doctor4t.trainmurdermystery.api.role;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.role.impl.CivilianRole;
import dev.doctor4t.trainmurdermystery.api.role.impl.KillerRole;
import dev.doctor4t.trainmurdermystery.api.role.impl.LooseEndRole;
import dev.doctor4t.trainmurdermystery.api.role.impl.VigilanteRole;
import dev.doctor4t.trainmurdermystery.api.team.CustomTeam;
import dev.doctor4t.trainmurdermystery.api.team.TMMTeams;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import dev.doctor4t.trainmurdermystery.event.*;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class TMMRoles {
    public static final ArrayList<CustomRole> ROLES = new ArrayList<>();
    private static final ArrayList<CustomRole> DEFAULT_ROLES = new ArrayList<>();

    public static final CustomRole CIVILIAN = registerDefaultRole(new CivilianRole());
    public static final CustomRole VIGILANTE = registerDefaultRole(new VigilanteRole());
    public static final CustomRole KILLER = registerDefaultRole(new KillerRole());
    public static final CustomRole LOOSE_END = registerDefaultRole(new LooseEndRole());

    private static CustomRole registerDefaultRole(CustomRole role) {
        DEFAULT_ROLES.add(role);
        return registerRole(role);
    }

    public static CustomRole registerRole(CustomRole role) {
        ROLES.add(role);
        return role;
    }

    public static boolean hasAddedRoles() {
        return ROLES.size() != DEFAULT_ROLES.size();
    }

    public static CustomRole getFromId(String id){
        return ROLES.stream().filter(role -> role.id().toString().equals(id)).findFirst().orElse(null);
    }

    static {
        ShouldDropOnDeath.EVENT.register((victim, item) ->
                ROLES.stream().allMatch(role -> role.shouldDropOnDeath(victim, item))
        );
        AllowPlayerDeath.EVENT.register((victim, attacker, id) ->
                ROLES.stream().allMatch(role -> role.allowPlayerDeathEvent(victim, attacker, id))
        );
        AllowPlayerOpenLockedDoor.EVENT.register(player ->
                ROLES.stream().allMatch(role -> role.allowPlayerOpenLockedDoorEvent(player))
        );
        AllowPunch.EVENT.register((attacker, victim) ->
                ROLES.stream().allMatch(role -> role.allowPunch(attacker, victim))
        );
        CanSeePoison.EVENT.register(player ->
                ROLES.stream().allMatch(role -> role.canSeePoison(player))
        );
    }
}
