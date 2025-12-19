package dev.doctor4t.wathe.block.property;

import net.minecraft.util.StringIdentifiable;

public enum CouchArms implements StringIdentifiable {
    LEFT("left"),
    RIGHT("right"),
    SINGLE("single"),
    NO_ARMS("no_arms");

    private final String name;

    CouchArms(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
