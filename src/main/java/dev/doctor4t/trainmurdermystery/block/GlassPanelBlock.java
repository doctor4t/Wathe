package dev.doctor4t.trainmurdermystery.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class GlassPanelBlock extends FacingBlock {

    public static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0, 0, 12, 16, 16, 16);
    public static final VoxelShape NORTH_COLLISION_SHAPE = Block.createCuboidShape(0, 0, 15, 16, 16, 16);
    public static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0, 0, 0, 4, 16, 16);
    public static final VoxelShape EAST_COLLISION_SHAPE = Block.createCuboidShape(0, 0, 0, 1, 16, 16);
    public static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 16, 4);
    public static final VoxelShape SOUTH_COLLISION_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 16, 1);
    public static final VoxelShape WEST_SHAPE = Block.createCuboidShape(12, 0, 0, 16, 16, 16);
    public static final VoxelShape WEST_COLLISION_SHAPE = Block.createCuboidShape(15, 0, 0, 16, 16, 16);
    public static final VoxelShape UP_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 4, 16);
    public static final VoxelShape UP_COLLISION_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 1, 16);
    public static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(0, 12, 0, 16, 16, 16);
    public static final VoxelShape DOWN_COLLISION_SHAPE = Block.createCuboidShape(0, 15, 0, 16, 16, 16);

    public GlassPanelBlock(Settings settings) {
        super(settings);
        this.setDefaultState(super.getDefaultState().with(FACING, Direction.SOUTH));
    }

    @Override
    protected MapCodec<? extends FacingBlock> getCodec() {
        return null;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (context.isHolding(this.asItem())) {
            return switch (state.get(FACING)) {
                case NORTH -> NORTH_SHAPE;
                case EAST -> EAST_SHAPE;
                case SOUTH -> SOUTH_SHAPE;
                case WEST -> WEST_SHAPE;
                case UP -> UP_SHAPE;
                case DOWN -> DOWN_SHAPE;
            };
        }
        return this.getCollisionShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> NORTH_COLLISION_SHAPE;
            case EAST -> EAST_COLLISION_SHAPE;
            case SOUTH -> SOUTH_COLLISION_SHAPE;
            case WEST -> WEST_COLLISION_SHAPE;
            case UP -> UP_COLLISION_SHAPE;
            case DOWN -> DOWN_COLLISION_SHAPE;
        };
    }

    @Override
    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        WorldAccess world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        Direction facing = ctx.getSide();
        BlockState neighborState = world.getBlockState(pos.offset(facing.getOpposite()));
        if (!ctx.shouldCancelInteraction() && neighborState.isOf(this)) {
            Direction neighborFacing = neighborState.get(FACING);
            if (!neighborFacing.getAxis().equals(facing.getAxis())) facing = neighborFacing;
        }
        return this.getDefaultState().with(FACING, facing);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        Direction facing = state.get(FACING);
        if (stateFrom.isOf(this)) {
            Direction fromFacing = stateFrom.get(FACING);
            if (fromFacing.equals(direction)) return facing.equals(direction.getOpposite());
            else if (fromFacing.equals(direction.getOpposite())) return facing.equals(direction);
            else if (fromFacing.equals(facing)) return true;
        }
        return super.isSideInvisible(state, stateFrom, direction);
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }
}
