package dev.doctor4t.wathe.block;

import dev.doctor4t.wathe.index.WatheProperties;
import dev.doctor4t.wathe.index.WatheSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SpyglassItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Set;

public interface PrivacyBlock {

    BooleanProperty OPAQUE = WatheProperties.OPAQUE;
    BooleanProperty INTERACTION_COOLDOWN = WatheProperties.INTERACTION_COOLDOWN;
    Direction[][] DIAGONALS = new Direction[][]{
            new Direction[]{Direction.NORTH, Direction.EAST},
            new Direction[]{Direction.SOUTH, Direction.EAST},
            new Direction[]{Direction.SOUTH, Direction.WEST},
            new Direction[]{Direction.NORTH, Direction.WEST},
            new Direction[]{Direction.UP, Direction.NORTH},
            new Direction[]{Direction.UP, Direction.EAST},
            new Direction[]{Direction.UP, Direction.SOUTH},
            new Direction[]{Direction.UP, Direction.WEST},
            new Direction[]{Direction.DOWN, Direction.NORTH},
            new Direction[]{Direction.DOWN, Direction.EAST},
            new Direction[]{Direction.DOWN, Direction.SOUTH},
            new Direction[]{Direction.DOWN, Direction.WEST}
    };

    int DELAY = 1;
    int COOLDOWN = 20;

    default void toggle(BlockState state, World world, BlockPos pos) {
        boolean opaque = !state.get(OPAQUE);
        if (state.get(INTERACTION_COOLDOWN)) {
            world.setBlockState(pos, state.with(INTERACTION_COOLDOWN, false));
            return;
        } else {
            world.playSound(null, pos, WatheSounds.BLOCK_PRIVACY_PANEL_TOGGLE, SoundCategory.BLOCKS, 0.1f, opaque ? 1.0f : 1.2f);
        }

        world.setBlockState(pos, state.with(OPAQUE, opaque).with(INTERACTION_COOLDOWN, true));
        world.scheduleBlockTick(pos, state.getBlock(), COOLDOWN);
        Set<Direction> changedDirections = EnumSet.noneOf(Direction.class);
        for (Direction direction : Direction.values()) {
            BlockPos sidePos = pos.offset(direction);
            BlockState sideState = world.getBlockState(sidePos);
            if (this.canToggle(sideState) && sideState.get(OPAQUE) != opaque) {
                changedDirections.add(direction);
                world.scheduleBlockTick(sidePos, sideState.getBlock(), DELAY);
            }
        }
        for (Direction[] diagonal : DIAGONALS) {
            if (diagonalHasAdjacentBlock(diagonal, changedDirections)) continue;
            BlockPos diagonalPos = this.offsetDiagonal(pos, diagonal);
            BlockState diagonalState = world.getBlockState(diagonalPos);
            if (this.canToggle(diagonalState) && diagonalState.get(OPAQUE) != opaque) {
                world.scheduleBlockTick(diagonalPos, diagonalState.getBlock(), DELAY);
            }
        }
    }

    default boolean diagonalHasAdjacentBlock(Direction[] diagonal, Set<Direction> changedDirections) {
        return changedDirections.contains(diagonal[0]) || changedDirections.contains(diagonal[1]);
    }

    default BlockPos offsetDiagonal(BlockPos pos, Direction[] diagonal) {
        return pos.offset(diagonal[0]).offset(diagonal[1]);
    }

    default boolean canInteract(BlockState state, BlockPos pos, World world, PlayerEntity player, Hand hand) {
        if (state.get(INTERACTION_COOLDOWN)) return false;
        if (player.getStackInHand(hand).getItem() instanceof SpyglassItem) return false;
        for (Direction direction : Direction.values()) {
            BlockState sideState = world.getBlockState(pos.offset(direction));
            if (sideState.contains(INTERACTION_COOLDOWN) && sideState.get(INTERACTION_COOLDOWN)) return false;
        }
        for (Direction[] diagonal : DIAGONALS) {
            BlockPos diagonalPos = this.offsetDiagonal(pos, diagonal);
            BlockState diagonalState = world.getBlockState(diagonalPos);
            if (diagonalState.contains(INTERACTION_COOLDOWN) && diagonalState.get(INTERACTION_COOLDOWN)) return false;
        }
        return true;
    }

    default boolean canToggle(BlockState state) {
        return state.getBlock() instanceof PrivacyBlock;
    }


}
