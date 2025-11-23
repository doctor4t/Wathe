package dev.doctor4t.trainmurdermystery.api;

import dev.doctor4t.trainmurdermystery.TMM;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class TMMRoles {
    public static final List<Role> ROLES = new ArrayList<>();

    public static final Role CIVILIAN = registerRole(new Role(TMM.id("civilian"), 0x36E51B, true, false));
    public static final Role VIGILANTE = registerRole(new Role(TMM.id("vigilante"), 0x1B8AE5, true, false));
    public static final Role KILLER = registerRole(new Role(TMM.id("killer"), 0xC13838, false, true));
    public static final Role LOOSE_END = registerRole(new Role(TMM.id("loose_end"), 0x9F0000, false, false));

    private TMMRoles() {
    }

    public static Role registerRole(Role role) {
        ROLES.add(role);
        return role;
    }

    @SuppressWarnings("ClassCanBeRecord")
    public static class Role {
        protected final Identifier identifier;
        protected final int color;
        protected final boolean innocent;
        protected final boolean canUseKiller;

        /**
         * @param identifier   the mod id and name of the role
         * @param color        the role announcement color
         * @param innocent     whether the gun drops when a person with this role is shot
         * @param canUseKiller can see and use the killer features
         */
        public Role(Identifier identifier, int color, boolean innocent, boolean canUseKiller) {
            this.identifier = identifier;
            this.color = color;
            this.innocent = innocent;
            this.canUseKiller = canUseKiller;
        }

        public Identifier getId() {
            return this.identifier;
        }

        public int getColor() {
            return this.color;
        }

        public boolean isInnocent() {
            return this.innocent;
        }

        public boolean canUseKiller() {
            return this.canUseKiller;
        }
    }
}
