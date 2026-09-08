package dev.doctor4t.wathe.cca;

import dev.doctor4t.wathe.Wathe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class PlayerVariablesComponent implements AutoSyncedComponent {
    public static final ComponentKey<PlayerVariablesComponent> KEY = ComponentRegistry.getOrCreate(Wathe.id("playervariables"), PlayerVariablesComponent.class);
    private final PlayerEntity player;
    public int innocentKills = 0;

    public PlayerVariablesComponent(PlayerEntity player) {
        this.player = player;
    }

    public void sync() {
        KEY.sync(this.player);
    }

    public void reset() {
        this.setInnocentKills(0);
        this.sync();
    }

    public int getInnocentKills() {
        return innocentKills;
    }

    public void incrementInnocentKills() {
        this.setInnocentKills(this.getInnocentKills() + 1);
    }

    public void setInnocentKills(int innocentKills) {
        this.innocentKills = innocentKills;
        this.sync();
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        tag.putInt("InnocentKills", this.innocentKills);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.innocentKills = tag.contains("InnocentKills") ? tag.getInt("InnocentKills") : 0;
    }
}