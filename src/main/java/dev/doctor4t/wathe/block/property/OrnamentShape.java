package dev.doctor4t.wathe.block.property;

import net.minecraft.util.StringIdentifiable;

public enum OrnamentShape implements StringIdentifiable {
    LEFT("left", 0b10000),
    RIGHT("right", 0b01000),
    TOP("top", 0b00100),
    BOTTOM("bottom", 0b00010),
    CENTER("center", 0b00001),
    LEFT_RIGHT("left_right", 0b11000),
    LEFT_RIGHT_CENTER("left_right_center", 0b11001),
    LEFT_TOP("left_top", 0b10100),
    LEFT_BOTTOM("left_bottom", 0b10010),
    RIGHT_TOP("right_top", 0b01100),
    RIGHT_BOTTOM("right_bottom", 0b01010),
    TOP_BOTTOM("top_bottom", 0b00110),
    LEFT_RIGHT_TOP("left_right_top", 0b11100),
    LEFT_RIGHT_BOTTOM("left_right_bottom", 0b11010),
    LEFT_TOP_BOTTOM("left_top_bottom", 0b10110),
    RIGHT_TOP_BOTTOM("right_top_bottom", 0b01110),
    ALL("all", 0b11110);

    private final String name;
    // left right top bottom center
    private final int flags;

    OrnamentShape(String name, int flags) {
        this.name = name;
        this.flags = flags;
    }

    // this is cursed but like idc
    public OrnamentShape with(OrnamentShape shape) {
        int combinedFlags = this.flags | shape.flags;
        for (OrnamentShape ornamentShape : OrnamentShape.values()) {
            if (ornamentShape.flags == combinedFlags) return ornamentShape;
        }
        return this;
    }

    public int getCount() {
        return (this.flags & 1)
                + (this.flags >> 1 & 1)
                + (this.flags >> 2 & 1)
                + (this.flags >> 3 & 1)
                + (this.flags >> 4 & 1);
    }

    @Override
    public String asString() {
        return this.name;
    }
}