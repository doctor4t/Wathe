package dev.doctor4t.wathe.block_entity;

import dev.doctor4t.wathe.block.DoorPartBlock;
import dev.doctor4t.wathe.block.SmallDoorBlock;
import dev.doctor4t.wathe.game.GameConstants;
import dev.doctor4t.wathe.index.WatheSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.AnimationState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class DoorBlockEntity extends SyncingBlockEntity {

    public AnimationState state = new AnimationState();
    protected long lastUpdate = 0L;
    protected boolean open;
    protected int age = 0;

    private String keyName = "";

    private int closeCountdown = 0;
    private int jammedTime = 0;
    private boolean blasted = false;

    public DoorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.open = state.get(Properties.OPEN);
        this.state.start(this.age);
        this.state.skip(10, 1);
    }

    public static <T extends DoorBlockEntity> void clientTick(World world, BlockPos pos, BlockState state, T entity) {
        entity.age++;
    }

    public static <T extends DoorBlockEntity> void serverTick(World world, BlockPos pos, BlockState state, T entity) {
        if (state.get(DoorPartBlock.OPEN) && !entity.isBlasted()) {
            entity.setCloseCountdown(entity.getCloseCountdown() - 1);
            if (entity.getCloseCountdown() <= 0) {
                SmallDoorBlock.toggleDoor(state, world, (SmallDoorBlockEntity) entity, pos);
            }
        } else {
            entity.setCloseCountdown(0);
        }

        if (entity.isJammed()) {
            entity.setJammed(entity.getJammedTime() - 1);
        }
    }

    public void toggle(boolean silent) {
        if (this.world == null || this.world.getTime() == this.lastUpdate || this.isBlasted()) {
            return;
        }
        this.toggleOpen();
        if (!silent) {
            this.playToggleSound();
        }
        this.toggleBlocks();
    }

    protected void toggleOpen() {
        if (this.world != null) {
            this.lastUpdate = this.world.getTime();
            this.open = !this.open;
            this.world.addSyncedBlockEvent(this.pos, this.getCachedState().getBlock(), 1, this.open ? 1 : 0);
            this.closeCountdown = this.open ? GameConstants.DOOR_AUTOCLOSE_TIME : 0;
        }
    }

    protected void playToggleSound() {
        if (this.world == null) {
            return;
        }
        this.world.playSound(null, this.pos, WatheSounds.BLOCK_DOOR_TOGGLE, SoundCategory.BLOCKS, 1f, 1f);
    }

    protected abstract void toggleBlocks();

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public float getYaw() {
        return 180 - this.getFacing().asRotation();
    }

    public Direction getFacing() {
        return this.getCachedState().get(Properties.HORIZONTAL_FACING);
    }

    public int getAge() {
        return this.age;
    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        if (this.world != null && type == 1) {
            this.state.start(this.age);
            this.open = data != 0;
            return true;
        }
        return super.onSyncedBlockEvent(type, data);
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        nbt.putBoolean("open", this.isOpen());
        nbt.putBoolean("blasted", this.isBlasted());
        nbt.putInt("closeCountdown", this.getCloseCountdown());
        nbt.putInt("jammedTime", this.getJammedTime());
        nbt.putString("keyName", this.getKeyName());
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        this.setOpen(nbt.getBoolean("open"));
        this.setBlasted(nbt.getBoolean("blasted"));
        this.setCloseCountdown(nbt.getInt("closeCountdown"));
        this.setJammed(nbt.getInt("jammedTime"));
        this.setKeyName(nbt.getString("keyName"));
    }

    public String getKeyName() {
        return this.keyName;
    }

    public void setKeyName(String string) {
        this.keyName = string;
    }

    public int getCloseCountdown() {
        return closeCountdown;
    }

    public void setCloseCountdown(int closeCountdown) {
        this.closeCountdown = closeCountdown;
    }

    public void setJammed(int time) {
        this.jammedTime = time;
    }

    public void jam() {
        this.setJammed(GameConstants.JAMMED_DOOR_TIME);
        if (this.open) {
            this.toggle(false);
        }
    }

    public void blast() {
        if (!this.open) {
            this.toggle(false);
        }
        this.setBlasted(true);
    }

    public boolean isJammed() {
        return this.jammedTime > 0;
    }

    public int getJammedTime() {
        return jammedTime;
    }

    public boolean isBlasted() {
        return blasted;
    }

    public void setBlasted(boolean blasted) {
        this.blasted = blasted;
    }
}
