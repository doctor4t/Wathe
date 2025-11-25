package dev.doctor4t.trainmurdermystery.api;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.game.DiscoveryGameMode;
import dev.doctor4t.trainmurdermystery.game.LooseEndsGameMode;
import dev.doctor4t.trainmurdermystery.game.MurderGameMode;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class TMMGameModes {
    public static final HashMap<Identifier, GameMode> GAME_MODES = new HashMap<>();

    public static final Identifier MURDER_ID = TMM.id("murder");
    public static final Identifier DISCOVERY_ID = TMM.id("discovery");
    public static final Identifier LOOSE_ENDS_ID = TMM.id("loose_ends");

    public static final GameMode MURDER = registerGameMode(MURDER_ID, new MurderGameMode(MURDER_ID));
    public static final GameMode DISCOVERY = registerGameMode(DISCOVERY_ID, new DiscoveryGameMode(DISCOVERY_ID));
    public static final GameMode LOOSE_ENDS = registerGameMode(LOOSE_ENDS_ID, new LooseEndsGameMode(LOOSE_ENDS_ID));

    public static GameMode registerGameMode(Identifier identifier, GameMode gameMode) {
        GAME_MODES.put(identifier, gameMode);
        return gameMode;
    }
}
