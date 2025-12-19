package dev.doctor4t.wathe.api;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.game.GameConstants;

import java.util.ArrayList;

public class WatheRoles {
    public static final ArrayList<Role> ROLES = new ArrayList<>();

    public static final Role DISCOVERY_CIVILIAN = registerRole(new Role(Wathe.id("discovery_civilian"), 0x36E51B, true, false, Role.MoodType.NONE, -1, true));
    public static final Role CIVILIAN = registerRole(new Role(Wathe.id("civilian"), 0x36E51B, true, false, Role.MoodType.REAL, GameConstants.getInTicks(0, 10), false));
    public static final Role VIGILANTE = registerRole(new Role(Wathe.id("vigilante"), 0x1B8AE5, true, false, Role.MoodType.REAL, GameConstants.getInTicks(0, 10), false));
    public static final Role KILLER = registerRole(new Role(Wathe.id("killer"), 0xC13838, false, true, Role.MoodType.FAKE, -1, true));
    public static final Role LOOSE_END = registerRole(new Role(Wathe.id("loose_end"), 0x9F0000, false, false, Role.MoodType.NONE, -1, false));

    public static Role registerRole(Role role) {
        ROLES.add(role);
        return role;
    }
}
