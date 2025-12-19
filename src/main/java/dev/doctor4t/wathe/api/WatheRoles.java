package dev.doctor4t.wathe.api;

import dev.doctor4t.wathe.Wathe;

import java.util.ArrayList;

public class WatheRoles {
    public static final ArrayList<Role> ROLES = new ArrayList<>();

    public static final Role DISCOVERY_CIVILIAN = new Role.Builder()
            .color(0x36E51B)
            .isInnocent(true)
            .canSeeTime(true)
            .buildAndRegister(Wathe.id("discovery_civilian"));
    public static final Role CIVILIAN = new Role.Builder()
            .color(0x36E51B)
            .innocent()
            .buildAndRegister(Wathe.id("civilian"));
    public static final Role VIGILANTE = new Role.Builder()
            .color(0x1B8AE5)
            .innocent()
            .build(Wathe.id("vigilante"));
    public static final Role KILLER = new Role.Builder()
            .color(0xC13838)
            .killer()
            .buildAndRegister(Wathe.id("killer"));
    public static final Role LOOSE_END = new Role.Builder()
            .color(0x9F0000)
            .buildAndRegister(Wathe.id("loose_end"));

    public static Role registerRole(Role role) {
        ROLES.add(role);
        return role;
    }
}
