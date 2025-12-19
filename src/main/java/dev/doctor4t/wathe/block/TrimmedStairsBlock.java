package dev.doctor4t.wathe.block;

import com.mojang.serialization.MapCodec;
import dev.doctor4t.wathe.index.WatheProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class TrimmedStairsBlock extends HorizontalFacingBlock {

    public static final BooleanProperty SUPPORT = WatheProperties.SUPPORT;
    public static final BooleanProperty LEFT = WatheProperties.LEFT;
    public static final BooleanProperty RIGHT = WatheProperties.RIGHT;

    public static final VoxelShape BOTTOM = Block.createCuboidShape(0, 0, 0, 16, 6, 16);
    public static final VoxelShape NORTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0, 6, -2, 16, 8, 8),
            Block.createCuboidShape(0, 14, 6, 16, 16, 16)
    );
    public static final VoxelShape NORTH_SUPPORTED_SHAPE = VoxelShapes.union(
            NORTH_SHAPE,
            BOTTOM,
            Block.createCuboidShape(0, 6, 8, 16, 14, 16)
    );
    public static final VoxelShape EAST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(8, 6, 0, 18, 8, 16),
            Block.createCuboidShape(0, 14, 0, 10, 16, 16)
    );
    public static final VoxelShape EAST_SUPPORTED_SHAPE = VoxelShapes.union(
            EAST_SHAPE,
            BOTTOM,
            Block.createCuboidShape(0, 6, 0, 8, 14, 16)
    );
    public static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0, 6, 8, 16, 8, 18),
            Block.createCuboidShape(0, 14, 0, 16, 16, 10)
    );
    public static final VoxelShape SOUTH_SUPPORTED_SHAPE = VoxelShapes.union(
            SOUTH_SHAPE,
            BOTTOM,
            Block.createCuboidShape(0, 6, 0, 16, 14, 8)
    );
    public static final VoxelShape WEST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(-2, 6, 0, 8, 8, 16),
            Block.createCuboidShape(6, 14, 0, 16, 16, 16)
    );
    public static final VoxelShape WEST_SUPPORTED_SHAPE = VoxelShapes.union(
            WEST_SHAPE,
            BOTTOM,
            Block.createCuboidShape(8, 6, 0, 16, 14, 16)
    );

    public TrimmedStairsBlock(Settings settings) {
        super(settings);
        this.setDefaultState(super.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(SUPPORT, false)
                .with(LEFT, true)
                .with(RIGHT, true));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return null;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        Direction facing = state.get(FACING);
        if (direction == facing.rotateYClockwise() && state.get(LEFT)) {
            return state.with(LEFT, !neighborState.isOf(this) || neighborState.get(FACING) != facing);
        } else if (direction == facing.rotateYCounterclockwise() && state.get(RIGHT)) {
            return state.with(RIGHT, !neighborState.isOf(this) || neighborState.get(FACING) != facing);
        }
        return state;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        Direction facing = ctx.getHorizontalPlayerFacing().getOpposite();
        BlockState leftState = world.getBlockState(pos.offset(facing.rotateYClockwise()));
        BlockState rightState = world.getBlockState(pos.offset(facing.rotateYCounterclockwise()));
        return this.getDefaultState()
                .with(SUPPORT, ctx.shouldCancelInteraction())
                .with(FACING, facing)
                .with(LEFT, !leftState.isOf(this) || leftState.get(FACING) != facing)
                .with(RIGHT, !rightState.isOf(this) || rightState.get(FACING) != facing);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        boolean support = state.get(SUPPORT);
        return switch (state.get(FACING)) {
            case NORTH -> support ? NORTH_SUPPORTED_SHAPE : NORTH_SHAPE;
            case EAST -> support ? EAST_SUPPORTED_SHAPE : EAST_SHAPE;
            case SOUTH -> support ? SOUTH_SUPPORTED_SHAPE : SOUTH_SHAPE;
            default -> support ? WEST_SUPPORTED_SHAPE : WEST_SHAPE;
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, SUPPORT, LEFT, RIGHT);
    }
}
