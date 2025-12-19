package dev.doctor4t.wathe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class ElevatorButtonBlock extends WatheButtonBlock {

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(5, 5, 14, 11, 11, 16);
    protected static final VoxelShape NORTH_PRESSED_SHAPE = Block.createCuboidShape(5, 5, 15, 11, 11, 16);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0, 5, 5, 2, 11, 11);
    protected static final VoxelShape EAST_PRESSED_SHAPE = Block.createCuboidShape(0, 5, 5, 1, 11, 11);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(5, 5, 0, 11, 11, 2);
    protected static final VoxelShape SOUTH_PRESSED_SHAPE = Block.createCuboidShape(5, 5, 0, 11, 11, 1);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(14, 5, 5, 16, 11, 11);
    protected static final VoxelShape WEST_PRESSED_SHAPE = Block.createCuboidShape(15, 5, 5, 16, 11, 11);
    protected static final VoxelShape FLOOR_SHAPE = Block.createCuboidShape(5, 0, 5, 11, 2, 11);
    protected static final VoxelShape FLOOR_PRESSED_SHAPE = Block.createCuboidShape(5, 0, 5, 11, 1, 11);
    protected static final VoxelShape CEILING_SHAPE = Block.createCuboidShape(5, 14, 5, 11, 16, 11);
    protected static final VoxelShape CEILING_PRESSED_SHAPE = Block.createCuboidShape(5, 15, 5, 11, 16, 11);

    public ElevatorButtonBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        boolean pressed = state.get(POWERED);
        Direction facing = state.get(FACING);
        BlockFace face = state.get(FACE);
        return switch (face) {
            case CEILING -> pressed ? CEILING_PRESSED_SHAPE : CEILING_SHAPE;
            case WALL -> switch (facing) {
                case NORTH -> pressed ? NORTH_PRESSED_SHAPE : NORTH_SHAPE;
                case EAST -> pressed ? EAST_PRESSED_SHAPE : EAST_SHAPE;
                case SOUTH -> pressed ? SOUTH_PRESSED_SHAPE : SOUTH_SHAPE;
                default -> pressed ? WEST_PRESSED_SHAPE : WEST_SHAPE;
            };
            case FLOOR -> pressed ? FLOOR_PRESSED_SHAPE : FLOOR_SHAPE;
        };
    }
}
