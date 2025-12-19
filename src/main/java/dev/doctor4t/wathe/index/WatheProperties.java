package dev.doctor4t.wathe.index;

import dev.doctor4t.wathe.block.property.CouchArms;
import dev.doctor4t.wathe.block.property.OrnamentShape;
import dev.doctor4t.wathe.block.property.RailingShape;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;

public interface WatheProperties {
    BooleanProperty ACTIVE = BooleanProperty.of("active"); // whether a block is receiving power from a breaker
    BooleanProperty INTERACTION_COOLDOWN = BooleanProperty.of("interaction_cooldown");
    BooleanProperty LEFT = BooleanProperty.of("left");
    BooleanProperty OPAQUE = BooleanProperty.of("opaque");
    BooleanProperty RIGHT = BooleanProperty.of("right");
    BooleanProperty SUPPORT = BooleanProperty.of("support");
    BooleanProperty TOP = BooleanProperty.of("top");

    EnumProperty<CouchArms> COUCH_ARMS = EnumProperty.of("arms", CouchArms.class);
    EnumProperty<OrnamentShape> ORNAMENT_SHAPE = EnumProperty.of("shape", OrnamentShape.class);
    EnumProperty<RailingShape> RAILING_SHAPE = EnumProperty.of("shape", RailingShape.class);
}
