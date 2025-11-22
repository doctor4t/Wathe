package dev.doctor4t.trainmurdermystery.cca;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.TMMRoles;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameWorldComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {
    public static final ComponentKey<GameWorldComponent> KEY = ComponentRegistry.getOrCreate(TMM.id("game"), GameWorldComponent.class);
    private final World world;

    private boolean lockedToSupporters = false;
    private boolean enableWeights = false;

    public void setWeightsEnabled(boolean enabled) {
        this.enableWeights = enabled;
    }

    public boolean areWeightsEnabled() {
        return enableWeights;
    }

    public enum GameStatus {
        INACTIVE, STARTING, ACTIVE, STOPPING
    }
    private GameMode gameMode = GameMode.MURDER;

    public enum GameMode implements StringIdentifiable {
        MURDER(10),
        DISCOVERY(10),
        LOOSE_ENDS(60);

        public final int startTime; // in minutes

        GameMode(int startTime) {
            this.startTime = startTime;
        }

        @Override
        public String asString() {
            return name();
        }
    }

    private boolean bound = true;

    private GameStatus gameStatus = GameStatus.INACTIVE;
    private int fade = 0;

    private final HashMap<UUID, TMMRoles.Role> roles = new HashMap<>();

    private int ticksUntilNextResetAttempt = -1;

    private int psychosActive = 0;

    private UUID looseEndWinner;

    public GameWorldComponent(World world) {
        this.world = world;
    }

    public void sync() {
        GameWorldComponent.KEY.sync(this.world);
    }

    public boolean isBound() {
        return bound;
    }

    public void setBound(boolean bound) {
        this.bound = bound;
        this.sync();
    }

    public int getFade() {
        return fade;
    }

    public void setFade(int fade) {
        this.fade = MathHelper.clamp(fade, 0, GameConstants.FADE_TIME + GameConstants.FADE_PAUSE);
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        this.sync();
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public boolean isRunning() {
        return this.gameStatus == GameStatus.ACTIVE || this.gameStatus == GameStatus.STOPPING;
    }

    public void addRole(PlayerEntity player, TMMRoles.Role role) {
        this.addRole(player.getUuid(), role);
    }

    public void addRole(UUID player, TMMRoles.Role role) {
        this.roles.put(player, role);
    }

    public void resetRole(TMMRoles.Role role) {
        roles.entrySet().removeIf(entry -> entry.getValue() == role);
    }

    public void setRoles(List<UUID> players, TMMRoles.Role role) {
        resetRole(role);

        for (UUID player : players) {
            addRole(player, role);
        }
    }

    public HashMap<UUID, TMMRoles.Role> getRoles() {
        return roles;
    }

    public TMMRoles.Role getRole(PlayerEntity player) {
        return getRole(player.getUuid());
    }

    public @Nullable TMMRoles.Role getRole(UUID uuid) {
        return roles.get(uuid);
    }

    public List<UUID> getAllWithRole(TMMRoles.Role role) {
        List<UUID> ret = new ArrayList<>();
        roles.forEach((uuid, playerRole) -> {
            if (playerRole == role) {
                ret.add(uuid);
            }
        });

        return ret;
    }

    public boolean isRole(@NotNull PlayerEntity player, TMMRoles.Role role) {
        return isRole(player.getUuid(), role);
    }

    public boolean isRole(@NotNull UUID uuid, TMMRoles.Role role) {
        return this.roles.get(uuid) == role;
    }

    public boolean isInnocent(@NotNull PlayerEntity player) {
        return getRole(player) == null || getRole(player).isInnocent();
    }

    public void clearRoleMap() {
        this.roles.clear();
        setPsychosActive(0);
    }

    public void queueTrainReset() {
        ticksUntilNextResetAttempt = 20;
    }

    public int getPsychosActive() {
        return psychosActive;
    }

    public boolean isPsychoActive() {
        return psychosActive > 0;
    }

    public void setPsychosActive(int psychosActive) {
        this.psychosActive = Math.max(0, psychosActive);
        this.sync();
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode discoveryMode) {
        this.gameMode = discoveryMode;
        this.sync();
    }

    public UUID getLooseEndWinner() {
        return this.looseEndWinner;
    }

    public void setLooseEndWinner(UUID looseEndWinner) {
        this.looseEndWinner = looseEndWinner;
        this.sync();
    }

    public boolean isLockedToSupporters() {
        return lockedToSupporters;
    }

    public void setLockedToSupporters(boolean lockedToSupporters) {
        this.lockedToSupporters = lockedToSupporters;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        this.lockedToSupporters = nbtCompound.getBoolean("LockedToSupporters");
        this.enableWeights = nbtCompound.getBoolean("EnableWeights");

        this.gameMode = GameMode.valueOf(nbtCompound.getString("GameMode"));
        this.gameStatus = GameStatus.valueOf(nbtCompound.getString("GameStatus"));

        this.fade = nbtCompound.getInt("Fade");
        this.psychosActive = nbtCompound.getInt("PsychosActive");

        for (TMMRoles.Role role : TMMRoles.ROLES) {
            this.setRoles(uuidListFromNbt(nbtCompound, role.identifier().toString()), role);
        }

        if (nbtCompound.contains("LooseEndWinner")) {
            this.looseEndWinner = nbtCompound.getUuid("LooseEndWinner");
        } else {
            this.looseEndWinner = null;
        }
    }

    private ArrayList<UUID> uuidListFromNbt(NbtCompound nbtCompound, String listName) {
        ArrayList<UUID> ret = new ArrayList<>();
        for (NbtElement e : nbtCompound.getList(listName, NbtElement.INT_ARRAY_TYPE)) {
            ret.add(NbtHelper.toUuid(e));
        }
        return ret;
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbtCompound.putBoolean("LockedToSupporters", lockedToSupporters);
        nbtCompound.putBoolean("EnableWeights", enableWeights);

        nbtCompound.putString("GameMode", gameMode.name());
        nbtCompound.putString("GameStatus", this.gameStatus.toString());

        nbtCompound.putInt("Fade", fade);
        nbtCompound.putInt("PsychosActive", psychosActive);

        for (TMMRoles.Role role : TMMRoles.ROLES) {
            nbtCompound.put(role.identifier().toString(), nbtFromUuidList(getAllWithRole(role)));
        }

        if (this.looseEndWinner != null) nbtCompound.putUuid("LooseEndWinner", this.looseEndWinner);
    }

    private NbtList nbtFromUuidList(List<UUID> list) {
        NbtList ret = new NbtList();
        for (UUID player : list) {
            ret.add(NbtHelper.fromUuid(player));
        }
        return ret;
    }

    @Override
    public void clientTick() {
        tickCommon();
    }


    @Override
    public void serverTick() {
        tickCommon();

        if (ticksUntilNextResetAttempt-- == 0) {
            if (GameFunctions.tryResetTrain((ServerWorld) this.world)) {
                ticksUntilNextResetAttempt = 5;
            }
        }

        ServerWorld serverWorld = (ServerWorld) this.world;

        // if not running and spectators or not in lobby reset them
        if (world.getTime() % 20 == 0) {
            for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                if (!isRunning() && (player.isSpectator() && serverWorld.getServer().getPermissionLevel(player.getGameProfile()) < 2 || (GameFunctions.isPlayerAliveAndSurvival(player) && GameConstants.PLAY_AREA.contains(player.getPos())))) {
                    GameFunctions.resetPlayer(player);
                }
            }
        }

        if (serverWorld.getServer().getOverworld().equals(serverWorld)) {
            TrainWorldComponent trainComponent = TrainWorldComponent.KEY.get(serverWorld);

            // spectator limits
            if (trainComponent.getSpeed() > 0) {
                for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                    if (!GameFunctions.isPlayerAliveAndSurvival(player) && isBound()) {
                        GameFunctions.limitPlayerToBox(player, GameConstants.PLAY_AREA);
                    }
                }
            }

            if (this.isRunning()) {
                var civilianAlive = false;
                for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                    // kill players who fell off the train
                    if (GameFunctions.isPlayerAliveAndSurvival(player) && player.getY() < GameConstants.PLAY_AREA.minY) {
                        GameFunctions.killPlayer(player, false, player.getLastAttacker() instanceof PlayerEntity killerPlayer ? killerPlayer : null, TMM.id("fell_out_of_train"));
                    }

                    // passive money
                    Integer balanceToAdd = GameConstants.PASSIVE_MONEY_TICKER.apply(world.getTime());
                    if (balanceToAdd > 0) PlayerShopComponent.KEY.get(player).addToBalance(balanceToAdd);
                    if (this.isInnocent(player) && !GameFunctions.isPlayerEliminated(player)) civilianAlive = true;
                }

                // check killer win condition: kill count reached
                GameFunctions.WinStatus winStatus = GameFunctions.WinStatus.NONE;

                if (gameMode == GameMode.MURDER) {
                    // check killer win condition (kill count reached)
                    if (!civilianAlive) {
                        winStatus = GameFunctions.WinStatus.KILLERS;
                    }

                    // check passenger win condition (all killers are dead)
                    if (winStatus == GameFunctions.WinStatus.NONE) {
                        winStatus = GameFunctions.WinStatus.PASSENGERS;
                        for (UUID player : this.getAllWithRole(TMMRoles.KILLER)) {
                            if (!GameFunctions.isPlayerEliminated(serverWorld.getPlayerByUuid(player))) {
                                winStatus = GameFunctions.WinStatus.NONE;
                            }
                        }
                    }
                }

                // check if last man standing in loose end
                if (gameMode == GameMode.LOOSE_ENDS) {
                    int playersLeft = 0;
                    PlayerEntity lastPlayer = null;
                    for (PlayerEntity player : world.getPlayers()) {
                        if (GameFunctions.isPlayerAliveAndSurvival(player)) {
                            playersLeft++;
                            lastPlayer = player;
                        }
                    }

                    if (playersLeft <= 0) {
                        GameFunctions.stopGame(serverWorld);
                    }

                    if (playersLeft == 1) {
                        this.setLooseEndWinner(lastPlayer.getUuid());
                        winStatus = GameFunctions.WinStatus.LOOSE_END;
                    }
                }

                // check if out of time
                if (winStatus == GameFunctions.WinStatus.NONE && !GameTimeComponent.KEY.get(serverWorld).hasTime()) winStatus = GameFunctions.WinStatus.TIME;

                // stop game if ran out of time on discovery mode
                if (gameMode == GameMode.DISCOVERY && winStatus == GameFunctions.WinStatus.TIME) GameFunctions.stopGame(serverWorld);

                // game end on win and display
                if (winStatus != GameFunctions.WinStatus.NONE && this.gameStatus == GameStatus.ACTIVE) {
                    GameRoundEndComponent.KEY.get(serverWorld).setRoundEndData(serverWorld.getPlayers(), winStatus);
                    for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                        if (winStatus == GameFunctions.WinStatus.TIME && this.isRole(player, TMMRoles.KILLER))
                            GameFunctions.killPlayer(player, true, null);
                    }
                    GameFunctions.stopGame(serverWorld);
                }
            }

            if (world.getTime() % 20 == 0) {
                this.sync();
            }
        }
    }

    private void tickCommon() {
        // fade and start / stop game
        if (this.getGameStatus() == GameStatus.STARTING || this.getGameStatus() == GameStatus.STOPPING) {
            this.setFade(fade + 1);

            if (this.getFade() >= GameConstants.FADE_TIME + GameConstants.FADE_PAUSE) {
                if (world instanceof ServerWorld serverWorld) {
                    if (this.getGameStatus() == GameStatus.STARTING)
                        GameFunctions.initializeGame(serverWorld);
                    if (this.getGameStatus() == GameStatus.STOPPING)
                        GameFunctions.finalizeGame(serverWorld);
                }
            }
        } else if (this.getGameStatus() == GameStatus.ACTIVE || this.getGameStatus() == GameStatus.INACTIVE) {
            this.setFade(fade - 1);
        }
    }

}
