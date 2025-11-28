package dev.doctor4t.trainmurdermystery.api;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class TMMRoles {
    public static final ArrayList<Role> ROLES = new ArrayList<>();

    public static final Role DISCOVERY_CIVILIAN = new Role.Builder()
            .color(0x36E51B)
            .isInnocent(true)
            .canSeeTime(true)
            .buildAndRegister(TMM.id("discovery_civilian"));
    public static final Role CIVILIAN = new Role.Builder()
            .color(0x36E51B)
            .innocent()
            .buildAndRegister(TMM.id("civilian"));
    public static final Role VIGILANTE = new Role.Builder()
            .color(0x1B8AE5)
            .innocent()
            .build(TMM.id("vigilante"));
    public static final Role KILLER = new Role.Builder()
            .color(0xC13838)
            .killer()
            .buildAndRegister(TMM.id("killer"));
    public static final Role LOOSE_END = new Role.Builder()
            .color(0x9F0000)
            .buildAndRegister(TMM.id("loose_end"));

    public static Role registerRole(Role role) {
        ROLES.add(role);
        return role;
    }
}
