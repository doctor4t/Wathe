package dev.doctor4t.wathe.block.property;

import net.minecraft.util.StringIdentifiable;

public enum RailingShape implements StringIdentifiable {
    TOP("top"),
    MIDDLE("middle"),
    BOTTOM("bottom");

    private final String name;

    RailingShape(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
