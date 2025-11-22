package dev.doctor4t.trainmurdermystery.block.entity;

import dev.doctor4t.trainmurdermystery.index.TMMBlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HornBlockEntity extends BlockEntity {
    public double prevPull;
    public double pull;
    public int cooldown;

    public HornBlockEntity(BlockPos pos, BlockState state) {
        super(TMMBlockEntities.HORN, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, @NotNull HornBlockEntity horn) {
        horn.prevPull = horn.pull;
        if (horn.pull > 0) {
            horn.pull -= .05f;
            if (horn.pull < 0.01f) {
                horn.pull = 0;
                if (world != null) world.updateListeners(pos, state, state, Block.NOTIFY_LISTENERS);
            }
        }
        horn.cooldown--;
    }

    public void pull(int pull) {
        if (this.cooldown <= 0) this.cooldown = 600; // 30s
        this.pull = pull;
        if (this.world != null)
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
        this.markDirty();
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        nbt.putDouble("pull", this.pull);
        nbt.putInt("cooldown", this.cooldown);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        this.pull = nbt.getDouble("pull");
        this.cooldown = nbt.getInt("cooldown");
    }

    @Override
    public @Nullable Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registries) {
        return createNbt(registries);
    }
}