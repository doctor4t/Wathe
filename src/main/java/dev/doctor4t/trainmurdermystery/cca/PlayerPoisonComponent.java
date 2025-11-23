package dev.doctor4t.trainmurdermystery.cca;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.game.GameFunctions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ClientTickingComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.UUID;

public class PlayerPoisonComponent implements AutoSyncedComponent, ServerTickingComponent, ClientTickingComponent {
    public static final ComponentKey<PlayerPoisonComponent> KEY = ComponentRegistry.getOrCreate(TMM.id("poison"), PlayerPoisonComponent.class);
    public static final Pair<Integer, Integer> clampTime = new Pair<>(800, 1400);
    private final PlayerEntity player;
    public int poisonTicks = -1;
    private int initialPoisonTicks = 0;
    private int poisonPulseCooldown = 0;
    public float pulseProgress = 0f;
    public boolean pulsing = false;
    public UUID poisoner;

    public PlayerPoisonComponent(PlayerEntity player) {
        this.player = player;
    }

    public void sync() {
        KEY.sync(this.player);
    }

    public void reset() {
        this.poisonTicks = -1;
        this.poisonPulseCooldown = 0;
        this.initialPoisonTicks = 0;
        this.pulseProgress = 0f;
        this.pulsing = false;
        this.sync();
    }

    @Override
    public void clientTick() {
        if (this.poisonTicks > -1) this.poisonTicks--;
        if (this.poisonTicks > 0) {
            var ticksSinceStart = this.initialPoisonTicks - this.poisonTicks;

            if (ticksSinceStart < 200) return;

            var minCooldown = 10;
            var maxCooldown = 60;
            var dynamicCooldown = minCooldown + (int) ((maxCooldown - minCooldown) * ((float) this.poisonTicks / clampTime.getRight()));

            if (this.poisonPulseCooldown <= 0) {
                this.poisonPulseCooldown = dynamicCooldown;

                this.pulsing = true;

                var minVolume = 0.5f;
                var maxVolume = 1f;
                var volume = minVolume + (maxVolume - minVolume) * (1f - ((float) this.poisonTicks / clampTime.getRight()));

                this.player.playSoundToPlayer(
                        SoundEvents.ENTITY_WARDEN_HEARTBEAT,
                        SoundCategory.PLAYERS,
                        volume,
                        1f
                );
            } else {
                this.poisonPulseCooldown--;
            }
        } else {
            this.poisonPulseCooldown = 0;
        }
    }

    @Override
    public void serverTick() {
        if (this.poisonTicks > -1) {
            this.poisonTicks--;
            if (this.poisonTicks == 0) {
                this.poisonTicks = -1;
                GameFunctions.killPlayer(this.player, true, this.poisoner == null ? null : this.player.getWorld().getPlayerByUuid(this.poisoner), TMM.id("poison"));
                this.poisoner = null;
                this.sync();
            }
        }
    }

    public void setPoisonTicks(int ticks, UUID poisoner) {
        this.poisoner = poisoner;
        this.poisonTicks = ticks;
        if (this.initialPoisonTicks == 0) this.initialPoisonTicks = ticks;
        this.sync();
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (this.poisoner != null) tag.putUuid("poisoner", this.poisoner);
        tag.putInt("poisonTicks", this.poisonTicks);
        tag.putInt("initialPoisonTicks", this.initialPoisonTicks);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.poisoner = tag.contains("poisoner") ? tag.getUuid("poisoner") : null;
        this.poisonTicks = tag.contains("poisonTicks") ? tag.getInt("poisonTicks") : -1;
        this.initialPoisonTicks = tag.contains("initialPoisonTicks") ? tag.getInt("initialPoisonTicks") : 0;
    }
}