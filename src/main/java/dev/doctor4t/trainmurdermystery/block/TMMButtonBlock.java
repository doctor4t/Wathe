package dev.doctor4t.trainmurdermystery.block;

import dev.doctor4t.trainmurdermystery.block_entity.SmallDoorBlockEntity;
import dev.doctor4t.trainmurdermystery.index.TMMProperties;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ButtonBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public abstract class TMMButtonBlock extends ButtonBlock {
    public static final BooleanProperty ACTIVE = TMMProperties.ACTIVE;

    public TMMButtonBlock(Settings settings) {
        super(BlockSetType.IRON, 20, settings);
        this.setDefaultState(super.getDefaultState().with(ACTIVE, true));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState placementState = super.getPlacementState(ctx);
        if (placementState != null) {
            return placementState.with(ACTIVE, true);
        }
        return null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(ACTIVE);
    }

    @Override
    protected int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWERED) && state.get(ACTIVE) ? 15 : 0;
    }

    @Override
    protected int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWERED) && state.get(ACTIVE) && getDirection(state) == direction ? 15 : 0;
    }

    @Override
    public void powerOn(BlockState state, World world, BlockPos pos, @Nullable PlayerEntity player) {
        if (state.get(ACTIVE)) {
            if (!world.isClient) {
                Iterable<BlockPos> iterable = BlockPos.iterateOutwards(pos, 1, 1, 1);
                for (BlockPos blockPos : iterable) {
                    if (blockPos.equals(pos)) {
                        continue;
                    }
                    if (this.tryOpenDoors(world, blockPos)) {
                        break;
                    }
                }
            }
        } else {
            world.playSound(player, pos, TMMSounds.BLOCK_BUTTON_TOGGLE_NO_POWER, SoundCategory.BLOCKS, 0.1f, 1f);
        }
        super.powerOn(state, world, pos, player);
    }

    private boolean tryOpenDoors(World world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof SmallDoorBlockEntity entity) {
            if (entity.isJammed()) {
                if (!world.isClient)
                    world.playSound(null, entity.getPos().getX() + .5f, entity.getPos().getY() + 1, entity.getPos().getZ() + .5f, TMMSounds.BLOCK_DOOR_LOCKED, SoundCategory.BLOCKS, 1f, 1f);
                return false;
            }

            entity.toggle(false);
            return true;
        }
        return false;
    }

    @Override
    protected void playClickSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos, boolean powered) {
        world.playSound(player, pos, this.getClickSound(powered), SoundCategory.BLOCKS, 0.5f, powered ? 1.0f : 1.5f);
    }

    @Override
    protected SoundEvent getClickSound(boolean powered) {
        return TMMSounds.BLOCK_SPACE_BUTTON_TOGGLE;
    }
}
