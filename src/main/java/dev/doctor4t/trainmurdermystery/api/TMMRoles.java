package dev.doctor4t.trainmurdermystery.api;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.client.gui.RoleAnnouncementTexts;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class TMMRoles {
    public static final ArrayList<CustomRole> ROLES = new ArrayList<>();
    private static final ArrayList<CustomRole> VANILLA_ROLES = new ArrayList<>();

    public static final CustomRole CIVILIAN = registerVanillaRole(buildAnonymousRole(TMM.id("civilian"), 0x36E51B, true, TMMTeams.INNOCENT, RoleAnnouncementTexts.CIVILIAN));
    public static final CustomRole VIGILANTE = registerVanillaRole(buildAnonymousRole(TMM.id("vigilante"), 0x1B8AE5, true, TMMTeams.INNOCENT, RoleAnnouncementTexts.VIGILANTE));
    public static final CustomRole KILLER = registerVanillaRole(buildAnonymousRole(TMM.id("killer"), 0xC13838, false, TMMTeams.KILLER, RoleAnnouncementTexts.KILLER));
    public static final CustomRole LOOSE_END = registerVanillaRole(buildAnonymousRole(TMM.id("loose_end"), 0x9F0000, false, TMMTeams.NEUTRAL_BENIGN, RoleAnnouncementTexts.LOOSE_END));

    private static CustomRole registerVanillaRole(CustomRole role) {
        VANILLA_ROLES.add(role);
        return registerRole(role);
    }

    public static CustomRole registerRole(CustomRole role) {
        ROLES.add(role);
        role.registerEventListeners();
        return role;
    }

    public static CustomRole buildAnonymousRole(Identifier id, int color, boolean isInnocent, CustomTeam team, RoleAnnouncementTexts.RoleAnnouncementText announcementText) {
        return new CustomRole() {
            @Override
            public boolean forcesDroppedGun() {
                return isInnocent;
            }

            @Override
            public boolean canPickupGun() {
                return isInnocent;
            }

            @Override
            public Identifier id() {
                return id;
            }

            @Override
            public int color() {
                return color;
            }

            @Override
            public CustomTeam team() {
                return team;
            }

            @Override
            public RoleAnnouncementTexts.RoleAnnouncementText announcementText() {
                return announcementText;
            }
            @Override
            public boolean hasMood() {
                return isInnocent;
            }
        };
    }

    public static boolean hasAddedRoles() {
        return ROLES.size() != VANILLA_ROLES.size();
    }

    public static CustomRole getFromId(String id){
        return ROLES.stream().filter(role -> role.id().toString().equals(id)).findFirst().orElse(null);
    }
}
