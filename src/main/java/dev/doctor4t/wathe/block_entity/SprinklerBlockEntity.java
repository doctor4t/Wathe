package dev.doctor4t.wathe.block_entity;

import dev.doctor4t.wathe.block.SprinklerBlock;
import dev.doctor4t.wathe.index.WatheBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class SprinklerBlockEntity extends SyncingBlockEntity {

    private boolean powered;

    public SprinklerBlockEntity(BlockPos pos, BlockState state) {
        super(WatheBlockEntities.SPRINKLER, pos, state);
        this.setPowered(state.get(SprinklerBlock.POWERED));
    }

    public static <T extends BlockEntity> void clientTick(World world, BlockPos pos, BlockState state, T t) {
        SprinklerBlockEntity entity = (SprinklerBlockEntity) t;
        if (!entity.isPowered()) {
            return;
        }
        Direction direction = SprinklerBlock.getDirection(state);
        Random random = world.getRandom();

        float offsetScale = .2f;
        float randomOffsetScale = .2f;
        float velScale = 0.5f;
        double x = pos.getX() + 0.5;
        double y = pos.getY();
        double z = pos.getZ() + 0.5;

        for (int i = 0; i < 5; i++) {
            world.addParticle(direction == Direction.DOWN ? ParticleTypes.FALLING_WATER : ParticleTypes.SPLASH,
                    x - direction.getOffsetX() * offsetScale + ((random.nextFloat() * 2f - 1f) * (direction.getAxis() != Direction.Axis.X ? randomOffsetScale : 0)),
                    (direction == Direction.DOWN ? .5 : .6) + y - direction.getOffsetY() * offsetScale + ((random.nextFloat() * 2f - 1f) * (direction.getAxis() != Direction.Axis.Y ? randomOffsetScale : 0)),
                    z - direction.getOffsetZ() * offsetScale + ((random.nextFloat() * 2f - 1f) * (direction.getAxis() != Direction.Axis.Z ? randomOffsetScale : 0)),
                    direction.getOffsetX() * velScale,
                    direction.getOffsetY() * velScale * (direction == Direction.UP ? 20f : 0),
                    direction.getOffsetZ() * velScale);
        }
    }

    public boolean isPowered() {
        return this.powered;
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putBoolean("powered", this.isPowered());
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        this.setPowered(nbt.getBoolean("powered"));
    }
}
