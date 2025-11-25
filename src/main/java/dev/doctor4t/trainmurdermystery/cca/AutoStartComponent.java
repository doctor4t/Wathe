package dev.doctor4t.trainmurdermystery.cca;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.api.GameMode;
import dev.doctor4t.trainmurdermystery.api.TMMGameModes;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class AutoStartComponent implements AutoSyncedComponent, CommonTickingComponent {
    public static final ComponentKey<AutoStartComponent> KEY = ComponentRegistry.getOrCreate(TMM.id("autostart"), AutoStartComponent.class);
    public final World world;
    public int startTime;
    public int time;

    public AutoStartComponent(World world) {
        this.world = world;
    }

    public void sync() {
        KEY.sync(this.world);
    }

    public void reset() {
        this.setTime(this.startTime);
    }

    @Override
    public void tick() {
        GameWorldComponent gameWorldComponent = GameWorldComponent.KEY.get(this.world);
        if (gameWorldComponent.isRunning()) return;

        if (this.startTime <= 0 && this.time <= 0) return;

        if (GameFunctions.getReadyPlayerCount(world) >= gameWorldComponent.getGameMode().minPlayerCount) {
            if (this.time-- <= 0 && this.world instanceof ServerWorld serverWorld) {
                if (gameWorldComponent.getGameStatus() == GameWorldComponent.GameStatus.INACTIVE) {
                    GameMode gameMode = TMMGameModes.MURDER;
                    GameFunctions.startGame(serverWorld, gameMode, GameConstants.getInTicks(gameMode.defaultStartTime, 0));
                    return;
                }
            }

            if (this.getTime() % 20 == 0) {
                this.sync();
            }
        } else {
            if (this.world.getTime() % 20 == 0) {
                this.setTime(this.startTime);
            }
        }
    }

    public boolean isAutoStartActive() {
        return startTime > 0;
    }

    public boolean hasTime() {
        return this.time > 0;
    }

    public int getTime() {
        return this.time;
    }

    public void addTime(int time) {
        this.setTime(this.time + time);
    }

    public void setStartTime(int time) {
        this.startTime = time;
    }

    public void setTime(int time) {
        this.time = time;
        this.sync();
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("startTime", this.startTime);
        tag.putInt("time", this.time);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.startTime = tag.getInt("startTime");
        this.time = tag.getInt("time");
    }
}