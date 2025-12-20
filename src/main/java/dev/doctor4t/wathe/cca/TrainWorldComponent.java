package dev.doctor4t.wathe.cca;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.game.mapeffect.HarpyExpressTrainMapEffect;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class TrainWorldComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {
    public static final ComponentKey<TrainWorldComponent> KEY = ComponentRegistry.getOrCreate(Wathe.id("train"), TrainWorldComponent.class);

    private final World world;
    private int speed = 0; // im km/h
    private int time = 0;
    private boolean snow = false;
    private boolean fog = false;
    private boolean hud = false;
    private TimeOfDay timeOfDay = TimeOfDay.DAY;

    public TrainWorldComponent(World world) {
        this.world = world;
    }

    private void sync() {
        TrainWorldComponent.KEY.sync(this.world);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        this.sync();
    }

    public int getSpeed() {
        return speed;
    }

    public float getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
        this.sync();
    }

    public boolean isSnowing() {
        return WatheClient.gameComponent.getMapEffect() instanceof HarpyExpressTrainMapEffect && snow;
    }

    public void setSnow(boolean snow) {
        this.snow = snow;
        this.sync();
    }

    public boolean isFoggy() {
        return fog;
    }

    public void setFog(boolean fog) {
        this.fog = fog;
        this.sync();
    }

    public boolean hasHud() {
        return hud;
    }

    public void setHud(boolean hud) {
        this.hud = hud;
        this.sync();
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(TimeOfDay timeOfDay) {
        this.timeOfDay = timeOfDay;
        this.sync();
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        this.setSpeed(nbtCompound.getInt("Speed"));
        this.setTime(nbtCompound.getInt("Time"));
        this.setSnow(nbtCompound.getBoolean("Snow"));
        this.setFog(nbtCompound.getBoolean("Fog"));
        this.setHud(nbtCompound.getBoolean("Hud"));
        this.setTimeOfDay(TimeOfDay.valueOf(nbtCompound.getString("TimeOfDay")));
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound, RegistryWrapper.WrapperLookup wrapperLookup) {
        nbtCompound.putInt("Speed", speed);
        nbtCompound.putInt("Time", time);
        nbtCompound.putBoolean("Snow", snow);
        nbtCompound.putBoolean("Fog", fog);
        nbtCompound.putBoolean("Hud", hud);
        nbtCompound.putString("TimeOfDay", timeOfDay.name());
    }

    @Override
    public void clientTick() {
        tickTime();
    }

    private void tickTime() {
        if (speed > 0) {
            time++;
        } else {
            time = 0;
        }
    }

    @Override
    public void serverTick() {
        tickTime();

        ServerWorld serverWorld = (ServerWorld) world;
        if (GameWorldComponent.KEY.get(serverWorld).getMapEffect() instanceof HarpyExpressTrainMapEffect) {
            serverWorld.setTimeOfDay(timeOfDay.time);
        }
    }

    public enum TimeOfDay implements StringIdentifiable {
        DAY(6000),
        NIGHT(18000),
        SUNDOWN(12800);

        final int time;

        TimeOfDay(int time) {
            this.time = time;
        }

        @Override
        public String asString() {
            return this.name();
        }
    }

}
