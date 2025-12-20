package dev.doctor4t.wathe.block_entity;

import dev.doctor4t.wathe.block.SprinklerBlock;
import dev.doctor4t.trainmurdermystery.cca.PlayerWetComponent;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.wathe.index.WatheBlockEntities;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class SprinklerBlockEntity extends SyncingBlockEntity {

    private int poweredTime;

    public SprinklerBlockEntity(BlockPos pos, BlockState state) {
        super(WatheBlockEntities.SPRINKLER, pos, state);
        this.setPoweredTime(state.get(SprinklerBlock.POWERED) ? 20 : 0);
    }

    public static <T extends BlockEntity> void serverTick(World world, BlockPos pos, BlockState state, T t) {
        SprinklerBlockEntity entity = (SprinklerBlockEntity) t;
        if (!entity.isPowered()) return;
        if (entity.poweredTime == 1) world.scheduleBlockTick(pos, state.getBlock(), 0);
        entity.poweredTime--;
        Direction direction = SprinklerBlock.getDirection(state);
        if (direction == Direction.DOWN) {
            Box box = new Box(pos.add(0, direction.getOffsetY() * 2, 0)).expand(-.2, .8, -.2);
            for (PlayerEntity player : world.getPlayers()) {
                if (player.getBoundingBox().intersects(box)) {
                    PlayerWetComponent.get(player).makeWet();
                }
            }
        }
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

    public void power() {
        this.poweredTime = GameConstants.SPRINKLER_POWERED_TIMER;
        world.playSound(null, this.pos.getX() + .5, this.pos.getY() + .5, this.pos.getZ() + .5, TMMSounds.BLOCK_SPRINKLER_RUN, SoundCategory.BLOCKS, 1f, 1f);
    }

    public boolean isPowered() {
        return this.poweredTime > 0;
    }

    public int getPoweredTime() {
        return this.poweredTime;
    }

    public void setPoweredTime(int poweredTime) {
        this.poweredTime = poweredTime;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        nbt.putInt("poweredTime", this.getPoweredTime());
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        this.setPoweredTime(nbt.getInt("poweredTime"));
    }
}
