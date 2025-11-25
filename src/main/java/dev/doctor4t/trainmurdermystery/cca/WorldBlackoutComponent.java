package dev.doctor4t.trainmurdermystery.cca;

import dev.doctor4t.trainmurdermystery.TMM;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.index.TMMProperties;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.ArrayList;
import java.util.List;

public class WorldBlackoutComponent implements AutoSyncedComponent, ServerTickingComponent {
    public static final ComponentKey<WorldBlackoutComponent> KEY = ComponentRegistry.getOrCreate(TMM.id("blackout"), WorldBlackoutComponent.class);
    private final World world;
    private final List<BlackoutDetails> blackouts = new ArrayList<>();
    private int ticks = 0;

    public WorldBlackoutComponent(World world) {
        this.world = world;
    }

    public void reset() {
        for (var detail : this.blackouts) detail.end(this.world);
        this.blackouts.clear();
    }

    @Override
    public void serverTick() {
        for (var i = 0; i < this.blackouts.size(); i++) {
            var detail = this.blackouts.get(i);
            detail.tick(this.world);
            if (detail.time <= 0) {
                detail.end(this.world);
                this.blackouts.remove(i);
                i--;
            }
        }
        if (this.ticks > 0) this.ticks--;
    }

    public boolean isBlackoutActive() {
        return this.ticks > 0;
    }

    public boolean triggerBlackout() {
        AreasWorldComponent areas = AreasWorldComponent.KEY.get(world);

        var area = areas.playArea;
        if (this.ticks > 0) return false;
        for (var x = (int) area.minX; x <= (int) area.maxX; x++) {
            for (var y = (int) area.minY; y <= (int) area.maxY; y++) {
                for (var z = (int) area.minZ; z <= (int) area.maxZ; z++) {
                    var pos = new BlockPos(x, y, z);
                    var state = this.world.getBlockState(pos);
                    if (!state.contains(Properties.LIT) || !state.contains(TMMProperties.ACTIVE)) continue;
                    var duration = GameConstants.BLACKOUT_MIN_DURATION + this.world.random.nextInt(GameConstants.BLACKOUT_MAX_DURATION - GameConstants.BLACKOUT_MIN_DURATION);
                    if (duration > this.ticks) this.ticks = duration;
                    var detail = new BlackoutDetails(pos, duration, state.get(Properties.LIT));
                    detail.init(this.world);
                    this.blackouts.add(detail);
                }
            }
        }
        if (this.world instanceof ServerWorld serverWorld) for (var player : serverWorld.getPlayers()) {
            player.networkHandler.sendPacket(new PlaySoundS2CPacket(Registries.SOUND_EVENT.getEntry(TMMSounds.AMBIENT_BLACKOUT), SoundCategory.PLAYERS, player.getX(), player.getY(), player.getZ(), 100f, 1f, player.getRandom().nextLong()));
        }
        return true;
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        var list = new NbtList();
        for (var detail : this.blackouts) list.add(detail.writeToNbt());
        tag.put("blackouts", list);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.blackouts.clear();
        for (var element : tag.getList("blackouts", 10)) {
            var detail = new BlackoutDetails((NbtCompound) element);
            detail.init(this.world);
            this.blackouts.add(detail);
        }
    }

    public static class BlackoutDetails {
        private final BlockPos pos;
        private final boolean original;
        private int time;

        public BlackoutDetails(BlockPos pos, int time, boolean original) {
            this.pos = pos;
            this.time = time;
            this.original = original;
        }

        public BlackoutDetails(@NotNull NbtCompound tag) {
            this.pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
            this.time = tag.getInt("time");
            this.original = tag.getBoolean("original");
        }

        public void init(@NotNull World world) {
            var state = world.getBlockState(this.pos);
            if (!state.contains(Properties.LIT) || !state.contains(TMMProperties.ACTIVE)) return;
            world.setBlockState(this.pos, state.with(Properties.LIT, false).with(TMMProperties.ACTIVE, false));
            world.playSound(null, this.pos, TMMSounds.BLOCK_LIGHT_TOGGLE, SoundCategory.BLOCKS, 0.5f, 1f);
        }

        public void end(@NotNull World world) {
            var state = world.getBlockState(this.pos);
            if (!state.contains(Properties.LIT) || !state.contains(TMMProperties.ACTIVE)) return;
            world.setBlockState(this.pos, state.with(Properties.LIT, this.original).with(TMMProperties.ACTIVE, true));
            world.playSound(null, this.pos, TMMSounds.BLOCK_LIGHT_TOGGLE, SoundCategory.BLOCKS, 0.5f, 0.5f);
        }

        public void tick(World world) {
            if (this.time > 0) this.time--;
            if (this.time > 4) return;
            var state = world.getBlockState(this.pos);
            if (!state.contains(Properties.LIT) || !state.contains(TMMProperties.ACTIVE)) return;
            switch (this.time) {
                case 0 -> this.end(world);
                case 1, 3 -> {
                    world.setBlockState(this.pos, state.with(Properties.LIT, false));
                    world.playSound(null, this.pos, TMMSounds.BLOCK_BUTTON_TOGGLE_NO_POWER, SoundCategory.BLOCKS, 0.1f, 1f);
                }
                case 2, 5 -> {
                    world.setBlockState(this.pos, state.with(Properties.LIT, true));
                    world.playSound(null, this.pos, TMMSounds.BLOCK_BUTTON_TOGGLE_NO_POWER, SoundCategory.BLOCKS, 0.1f, 1f);
                }
            }
        }

        public NbtCompound writeToNbt() {
            var tag = new NbtCompound();
            tag.putInt("x", this.pos.getX());
            tag.putInt("y", this.pos.getY());
            tag.putInt("z", this.pos.getZ());
            tag.putInt("time", this.time);
            tag.putBoolean("original", this.original);
            return tag;
        }
    }
}