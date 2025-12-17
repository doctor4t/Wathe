package dev.doctor4t.trainmurdermystery.block_entity;

import dev.doctor4t.trainmurdermystery.api.event.CanSeePoison;
import dev.doctor4t.trainmurdermystery.client.TMMClient;
import dev.doctor4t.trainmurdermystery.index.TMMBlockEntities;
import dev.doctor4t.trainmurdermystery.index.TMMParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TrimmedBedBlockEntity extends BlockEntity {
    private boolean hasScorpion = false;
    private UUID poisoner;

    public boolean hasScorpion() {
        return hasScorpion;
    }

    public void setHasScorpion(boolean hasScorpion, @Nullable UUID poisoner) {
        this.hasScorpion = hasScorpion;
        this.poisoner = poisoner;
        sync();
    }

    public UUID getPoisoner() {
        return poisoner;
    }

    public TrimmedBedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static TrimmedBedBlockEntity create(BlockPos pos, BlockState state) {
        return new TrimmedBedBlockEntity(TMMBlockEntities.TRIMMED_BED, pos, state);
    }

    private void sync() {
        if (world != null && !world.isClient) {
            markDirty();
            world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
    }

    @SuppressWarnings("unused")
    public static <T extends BlockEntity> void clientTick(World world, BlockPos pos, BlockState state, T t) {

    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putBoolean("hasScorpion", this.hasScorpion);
        if (this.poisoner != null) nbt.putUuid("poisoner", this.poisoner);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.hasScorpion = nbt.getBoolean("hasScorpion");
        this.poisoner = nbt.containsUuid("poisoner") ? nbt.getUuid("poisoner") : null;
    }
}
