package dev.doctor4t.wathe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class SmallButtonBlock extends WatheButtonBlock {

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(6, 6, 14, 10, 10, 16);
    protected static final VoxelShape NORTH_PRESSED_SHAPE = Block.createCuboidShape(6, 6, 15, 10, 10, 16);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0, 6, 6, 2, 10, 10);
    protected static final VoxelShape EAST_PRESSED_SHAPE = Block.createCuboidShape(0, 6, 6, 1, 10, 10);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(6, 6, 0, 10, 10, 2);
    protected static final VoxelShape SOUTH_PRESSED_SHAPE = Block.createCuboidShape(6, 6, 0, 10, 10, 1);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(14, 6, 6, 16, 10, 10);
    protected static final VoxelShape WEST_PRESSED_SHAPE = Block.createCuboidShape(15, 6, 6, 16, 10, 10);
    protected static final VoxelShape FLOOR_X_SHAPE = Block.createCuboidShape(6, 0, 6, 10, 2, 10);
    protected static final VoxelShape FLOOR_X_PRESSED_SHAPE = Block.createCuboidShape(6, 0, 6, 10, 1, 10);
    protected static final VoxelShape FLOOR_Z_SHAPE = Block.createCuboidShape(6, 0, 6, 10, 2, 10);
    protected static final VoxelShape FLOOR_Z_PRESSED_SHAPE = Block.createCuboidShape(6, 0, 6, 10, 1, 10);
    protected static final VoxelShape CEILING_X_SHAPE = Block.createCuboidShape(6, 14, 6, 10, 16, 10);
    protected static final VoxelShape CEILING_X_PRESSED_SHAPE = Block.createCuboidShape(6, 15, 6, 10, 16, 10);
    protected static final VoxelShape CEILING_Z_SHAPE = Block.createCuboidShape(6, 14, 6, 10, 16, 10);
    protected static final VoxelShape CEILING_Z_PRESSED_SHAPE = Block.createCuboidShape(6, 15, 6, 10, 16, 10);

    public SmallButtonBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        boolean pressed = state.get(POWERED);
        Direction facing = state.get(FACING);
        BlockFace face = state.get(FACE);
        boolean xAxis = facing.getAxis() == Direction.Axis.X;
        return switch (face) {
            case CEILING ->
                    xAxis ? (pressed ? CEILING_X_PRESSED_SHAPE : CEILING_X_SHAPE) : (pressed ? CEILING_Z_PRESSED_SHAPE : CEILING_Z_SHAPE);
            case WALL -> switch (facing) {
                case NORTH -> pressed ? NORTH_PRESSED_SHAPE : NORTH_SHAPE;
                case EAST -> pressed ? EAST_PRESSED_SHAPE : EAST_SHAPE;
                case SOUTH -> pressed ? SOUTH_PRESSED_SHAPE : SOUTH_SHAPE;
                default -> pressed ? WEST_PRESSED_SHAPE : WEST_SHAPE;
            };
            case FLOOR ->
                    xAxis ? (pressed ? FLOOR_X_PRESSED_SHAPE : FLOOR_X_SHAPE) : (pressed ? FLOOR_Z_PRESSED_SHAPE : FLOOR_Z_SHAPE);
        };
    }
}
