package dev.doctor4t.wathe.cca;

import dev.doctor4t.wathe.Wathe;
import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.index.WatheProperties;
import dev.doctor4t.wathe.index.WatheSounds;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.ArrayList;
import java.util.List;

public class WorldBlackoutComponent implements AutoSyncedComponent, ServerTickingComponent {
    public static final ComponentKey<WorldBlackoutComponent> KEY = ComponentRegistry.getOrCreate(Wathe.id("blackout"), WorldBlackoutComponent.class);
    private final World world;
    private final List<BlackoutDetails> blackouts = new ArrayList<>();
    private int ticks = 0;

    public WorldBlackoutComponent(World world) {
        this.world = world;
    }

    public void reset() {
        for (BlackoutDetails detail : this.blackouts) detail.end(this.world);
        this.blackouts.clear();
    }

    @Override
    public void serverTick() {
        for (int i = 0; i < this.blackouts.size(); i++) {
            BlackoutDetails detail = this.blackouts.get(i);
            detail.tick(this.world);
            if (detail.time <= 0) {
                detail.end(this.world);
                this.blackouts.remove(i);
                i--;
            }
        }
        if (this.ticks > 0) {
            this.ticks--;
            sync();
        }
    }

    public boolean isBlackoutActive() {
        return this.ticks > 0;
    }

    public boolean triggerBlackout() {
        MapVariablesWorldComponent areas = MapVariablesWorldComponent.KEY.get(world);

        return triggerBlackout(areas.playArea);
    }

    public boolean triggerBlackout(Box area) {
        if (this.ticks > 0) return false;
        for (int x = (int) area.minX; x <= (int) area.maxX; x++) {
            for (int y = (int) area.minY; y <= (int) area.maxY; y++) {
                for (int z = (int) area.minZ; z <= (int) area.maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = this.world.getBlockState(pos);
                    if (!(state.getBlock() instanceof BlackoutBlock block)) continue;
                    int duration = block.getDuration(world.random);
                    if (duration > this.ticks) this.ticks = duration;
                    BlackoutDetails detail = new BlackoutDetails(pos, duration);
                    detail.init(this.world);
                    this.blackouts.add(detail);
                }
            }
        }
        if (this.world instanceof ServerWorld serverWorld) for (ServerPlayerEntity player : serverWorld.getPlayers()) {
            player.networkHandler.sendPacket(new PlaySoundS2CPacket(Registries.SOUND_EVENT.getEntry(WatheSounds.AMBIENT_BLACKOUT), SoundCategory.PLAYERS, player.getX(), player.getY(), player.getZ(), 100f, 1f, player.getRandom().nextLong()));
        }
        return true;
    }

    private void sync() {
        KEY.sync(this.world);
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        NbtList list = new NbtList();
        for (BlackoutDetails detail : this.blackouts) list.add(detail.writeToNbt());
        tag.put("blackouts", list);
        tag.putInt("ticks", this.ticks);
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.blackouts.clear();
        for (NbtElement element : tag.getList("blackouts", 10)) {
            BlackoutDetails detail = new BlackoutDetails((NbtCompound) element);
            detail.init(this.world);
            this.blackouts.add(detail);
        }
        this.ticks = tag.getInt("ticks");
    }

    @Override
    public void writeSyncPacket(RegistryByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeInt(ticks);
    }

    @Override
    public void applySyncPacket(RegistryByteBuf buf) {
        this.ticks = buf.readInt();
    }

    public static class BlackoutDetails {
        public final BlockPos pos;
        public byte data;
        private int time;

        public int getTime() {
            return time;
        }

        public BlackoutDetails(BlockPos pos, int time) {
            this.pos = pos;
            this.time = time;
            this.data = 0;
        }

        public BlackoutDetails(@NotNull NbtCompound tag) {
            this.pos = new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
            this.time = tag.getInt("time");
            this.data = tag.getByte("data");
        }

        public void init(@NotNull World world) {
            BlockState state = world.getBlockState(this.pos);
            if (state.getBlock() instanceof BlackoutBlock block)
                block.init(world, this);
//            boolean isPrivateBlock = state.getBlock() instanceof PrivacyBlock;
//            if (!isPrivateBlock)
//                if (!state.contains(Properties.LIT) || !state.contains(TMMProperties.ACTIVE)) return;
//            world.setBlockState(this.pos, state.with(isPrivateBlock ? TMMProperties.OPAQUE : Properties.LIT, isPrivateBlock).with(TMMProperties.ACTIVE, false));
//            world.playSound(null, this.pos, isPrivateBlock ? TMMSounds.BLOCK_PRIVACY_PANEL_TOGGLE : TMMSounds.BLOCK_BUTTON_TOGGLE_NO_POWER, SoundCategory.BLOCKS, 0.5f, 1f);
        }

        public void end(@NotNull World world) {
            BlockState state = world.getBlockState(this.pos);
            if (state.getBlock() instanceof BlackoutBlock block)
                block.end(world, this);
//            boolean isPrivateBlock = state.getBlock() instanceof PrivacyBlock;
//            if (!isPrivateBlock)
//                if (!state.contains(Properties.LIT) || !state.contains(TMMProperties.ACTIVE)) return;
//            world.setBlockState(this.pos, state.with(isPrivateBlock ? TMMProperties.OPAQUE : Properties.LIT, this.original).with(TMMProperties.ACTIVE, true));
//            world.playSound(null, this.pos, TMMSounds.BLOCK_BUTTON_TOGGLE_NO_POWER, SoundCategory.BLOCKS, 0.5f, 0.5f);
        }

        public void tick(World world) {
            if (this.time > 0) this.time--;
            BlockState state = world.getBlockState(this.pos);
            if (state.getBlock() instanceof BlackoutBlock block)
                block.tick(world, this);
//            if (this.time > 4) return;
//            BlockState state = world.getBlockState(this.pos);
//            boolean isPrivateBlock = state.getBlock() instanceof PrivacyBlock;
//            if (!isPrivateBlock)
//                if (!state.contains(Properties.LIT) || !state.contains(TMMProperties.ACTIVE)) return;
//            switch (this.time) {
//                case 0 -> this.end(world);
//                case 1, 3, 8, 18 -> {
//                    world.setBlockState(this.pos, state.with(isPrivateBlock ? TMMProperties.OPAQUE : Properties.LIT, !isPrivateBlock));
//                    world.playSound(null, this.pos, isPrivateBlock ? TMMSounds.BLOCK_PRIVACY_PANEL_TOGGLE : TMMSounds.BLOCK_BUTTON_TOGGLE_NO_POWER, SoundCategory.BLOCKS, 0.1f, 1f);
//                }
//                case 2, 5, 13 -> {
//                    world.setBlockState(this.pos, state.with(isPrivateBlock ? TMMProperties.OPAQUE : Properties.LIT, isPrivateBlock));
//                    world.playSound(null, this.pos, isPrivateBlock ? TMMSounds.BLOCK_PRIVACY_PANEL_TOGGLE : TMMSounds.BLOCK_BUTTON_TOGGLE_NO_POWER, SoundCategory.BLOCKS, 0.1f, 1f);
//                }
//            }
        }

        public NbtCompound writeToNbt() {
            NbtCompound tag = new NbtCompound();
            tag.putInt("x", this.pos.getX());
            tag.putInt("y", this.pos.getY());
            tag.putInt("z", this.pos.getZ());
            tag.putInt("time", this.time);
            tag.putByte("data", this.data);
            return tag;
        }
    }
}