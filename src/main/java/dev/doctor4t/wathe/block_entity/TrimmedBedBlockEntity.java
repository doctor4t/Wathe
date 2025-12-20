package dev.doctor4t.wathe.block_entity;

import dev.doctor4t.wathe.client.WatheClient;
import dev.doctor4t.wathe.index.WatheBlockEntities;
import dev.doctor4t.wathe.index.WatheParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
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
        return new TrimmedBedBlockEntity(WatheBlockEntities.TRIMMED_BED, pos, state);
    }

    private void sync() {
        if (world != null && !world.isClient) {
            markDirty();
            world.updateListeners(pos, getCachedState(), getCachedState(), 3);
        }
    }

    @SuppressWarnings("unused")
    public static <T extends BlockEntity> void clientTick(World world, BlockPos pos, BlockState state, T t) {
        TrimmedBedBlockEntity entity = (TrimmedBedBlockEntity) t;
        if (!WatheClient.isKiller() && !WatheClient.isPlayerSpectatingOrCreative()) return;
        if (!entity.hasScorpion()) return;
        if (world.getRandom().nextBetween(0, 20) < 17) return;

        world.addParticle(
                WatheParticles.POISON,
                pos.getX() + 0.5f,
                pos.getY() + 0.5f,
                pos.getZ() + 0.5f,
                0f, 0.05f, 0f
        );
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
