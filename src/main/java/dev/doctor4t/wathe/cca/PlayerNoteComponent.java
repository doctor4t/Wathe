package dev.doctor4t.wathe.cca;

import dev.doctor4t.wathe.Wathe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class PlayerNoteComponent implements AutoSyncedComponent {
    public static final ComponentKey<PlayerNoteComponent> KEY = ComponentRegistry.getOrCreate(Wathe.id("note"), PlayerNoteComponent.class);
    public final PlayerEntity player;
    public String[] text = new String[]{"", "", "", ""};
    public boolean written = false;

    public PlayerNoteComponent(PlayerEntity player) {
        this.player = player;
    }

    public void sync() {
        KEY.sync(this.player);
    }

    public void reset() {
        this.text = new String[]{"", "", "", ""};
        this.written = false;
        this.sync();
    }

    public void setNote(@NotNull String s, String s1, String s2, String s3) {
        this.text = new String[]{s, s1, s2, s3};
        this.written = !s.isEmpty() || !s1.isEmpty() || !s2.isEmpty() || !s3.isEmpty();
        this.sync();
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.@NotNull WrapperLookup registryLookup) {
        tag.putString("line1", this.text[0]);
        tag.putString("line2", this.text[1]);
        tag.putString("line3", this.text[2]);
        tag.putString("line4", this.text[3]);
        tag.putBoolean("written", this.written);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.text[0] = tag.getString("line1");
        this.text[1] = tag.getString("line2");
        this.text[2] = tag.getString("line3");
        this.text[3] = tag.getString("line4");
        this.written = tag.getBoolean("written");
    }
}