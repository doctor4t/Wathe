package dev.doctor4t.wathe.api;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.game.gamemode.DiscoveryGameMode;
import dev.doctor4t.wathe.game.gamemode.LooseEndsGameMode;
import dev.doctor4t.wathe.game.gamemode.MurderGameMode;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class WatheGameModes {
    public static final HashMap<Identifier, GameMode> GAME_MODES = new HashMap<>();

    public static final Identifier MURDER_ID = Wathe.id("murder");
    public static final Identifier DISCOVERY_ID = Wathe.id("discovery");
    public static final Identifier LOOSE_ENDS_ID = Wathe.id("loose_ends");

    public static final GameMode MURDER = registerGameMode(MURDER_ID, new MurderGameMode(MURDER_ID));
    public static final GameMode DISCOVERY = registerGameMode(DISCOVERY_ID, new DiscoveryGameMode(DISCOVERY_ID));
    public static final GameMode LOOSE_ENDS = registerGameMode(LOOSE_ENDS_ID, new LooseEndsGameMode(LOOSE_ENDS_ID));

    public static GameMode registerGameMode(Identifier identifier, GameMode gameMode) {
        GAME_MODES.put(identifier, gameMode);
        return gameMode;
    }
}
