package dev.doctor4t.wathe.block;

import dev.doctor4t.wathe.index.WatheProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class BarBlock extends PillarBlock {

    public static final BooleanProperty TOP = WatheProperties.TOP;
    public static final BooleanProperty BOTTOM = Properties.BOTTOM;

    protected static final VoxelShape X_SHAPE = Block.createCuboidShape(0, 6, 6, 16, 10, 10);
    protected static final VoxelShape Y_SHAPE = Block.createCuboidShape(6, 0, 6, 10, 16, 10);
    protected static final VoxelShape Z_SHAPE = Block.createCuboidShape(6, 6, 0, 10, 10, 16);

    public BarBlock(Settings settings) {
        super(settings);
        this.setDefaultState(super.getDefaultState()
                .with(AXIS, Direction.Axis.Y)
                .with(TOP, true)
                .with(BOTTOM, true));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        Direction.Axis axis = state.get(AXIS);
        if (direction.getAxis() == axis) {
            return state.with(
                    direction == this.getTopDirection(axis) ? TOP : BOTTOM,
                    !this.isConnectedBar(neighborState, axis)
            );
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        Direction.Axis axis = ctx.getSide().getAxis();
        Direction topDirection = this.getTopDirection(axis);
        return this.getDefaultState().with(AXIS, ctx.getSide().getAxis())
                .with(TOP, !this.isConnectedBar(world.getBlockState(pos.offset(topDirection)), axis))
                .with(BOTTOM, !this.isConnectedBar(world.getBlockState(pos.offset(topDirection.getOpposite())), axis));
    }

    private boolean isConnectedBar(BlockState state, Direction.Axis axis) {
        return state.isOf(this) && state.get(AXIS) == axis;
    }

    private Direction getTopDirection(Direction.Axis axis) {
        return switch (axis) {
            case X -> Direction.EAST;
            case Y -> Direction.UP;
            case Z -> Direction.SOUTH;
        };
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(AXIS)) {
            case X -> X_SHAPE;
            case Y -> Y_SHAPE;
            case Z -> Z_SHAPE;
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS, TOP, BOTTOM);
    }
}
