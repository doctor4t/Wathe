package dev.doctor4t.trainmurdermystery.api;

import dev.doctor4t.trainmurdermystery.TMM;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class TMMRoles {
    public static final ArrayList<Role> ROLES = new ArrayList<>();

    public static final Role CIVILIAN = registerRole(new Role(TMM.id("civilian"), 0x36E51B, true, false));
    public static final Role VIGILANTE = registerRole(new Role(TMM.id("vigilante"), 0x1B8AE5, true, false));
    public static final Role KILLER = registerRole(new Role(TMM.id("killer"), 0xC13838, false, true));
    public static final Role LOOSE_END = registerRole(new Role(TMM.id("loose_end"), 0x9F0000, false, false));

    public static Role registerRole(Role role) {
        ROLES.add(role);
        return role;
    }

    /**
     * @param identifier the mod id and name of the role
     * @param color      the role announcement color
     * @param isInnocent whether the gun drops when a person with this role is shot and is considered a civilian to the win conditions
     * @param canUseKiller can see and use the killer features
     */
    public record Role(Identifier identifier, int color, boolean isInnocent, boolean canUseKiller) {
    }
}
