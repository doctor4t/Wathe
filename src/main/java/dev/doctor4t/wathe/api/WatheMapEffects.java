package dev.doctor4t.wathe.api;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.game.mapeffect.*;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class WatheMapEffects {
    public static final HashMap<Identifier, MapEffect> MAP_EFFECTS = new HashMap<>();

    public static final Identifier HARPY_EXPRESS_LOBBY_ID = Wathe.id("harpy_express_lobby");
    public static final Identifier HARPY_EXPRESS_NIGHT_ID = Wathe.id("harpy_express_night");
    public static final Identifier HARPY_EXPRESS_DAY_ID = Wathe.id("harpy_express_day");
    public static final Identifier HARPY_EXPRESS_SUNDOWN_ID = Wathe.id("harpy_express_sundown");
    public static final Identifier GENERIC_ID = Wathe.id("generic");

    public static final MapEffect HARPY_EXPRESS_LOBBY = registerMapEffect(HARPY_EXPRESS_LOBBY_ID, new HarpyExpressLobbyMapEffect(HARPY_EXPRESS_LOBBY_ID));
    public static final MapEffect HARPY_EXPRESS_NIGHT = registerMapEffect(HARPY_EXPRESS_NIGHT_ID, new HarpyExpressNightTrainMapEffect(HARPY_EXPRESS_NIGHT_ID));
    public static final MapEffect HARPY_EXPRESS_DAY = registerMapEffect(HARPY_EXPRESS_DAY_ID, new HarpyExpressDayTrainMapEffect(HARPY_EXPRESS_DAY_ID));
    public static final MapEffect HARPY_EXPRESS_SUNDOWN = registerMapEffect(HARPY_EXPRESS_SUNDOWN_ID, new HarpyExpressSundownTrainMapEffect(HARPY_EXPRESS_SUNDOWN_ID));
    public static final MapEffect GENERIC = registerMapEffect(GENERIC_ID, new GenericMapEffect(GENERIC_ID));

    public static MapEffect registerMapEffect(Identifier identifier, MapEffect mapEffect) {
        MAP_EFFECTS.put(identifier, mapEffect);
        return mapEffect;
    }
}
