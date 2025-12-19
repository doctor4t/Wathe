package dev.doctor4t.wathe.block.entity;

import dev.doctor4t.wathe.block.MountableBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class SeatEntity extends Entity {
    @Nullable
    BlockPos seatPos;

    public SeatEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {

    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        NbtHelper.toBlockPos(nbt, "seatPos").ifPresent(this::setSeatPos);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.getSeatPos() != null) nbt.put("seatPos", NbtHelper.fromBlockPos(this.getSeatPos()));
    }

    @Override
    public void tick() {
        if (!this.getWorld().isClient) {
            if (this.getSeatPos() == null || !this.hasPassengers() || !(this.getWorld().getBlockState(this.getSeatPos()).getBlock() instanceof MountableBlock)) {
                this.removeAllPassengers();
                this.discard();
            }
        }

        super.tick();
    }


    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return false;
    }

    @Nullable
    public BlockPos getSeatPos() {
        return seatPos;
    }

    public void setSeatPos(@Nullable BlockPos seatPos) {
        this.seatPos = seatPos;
    }
}
