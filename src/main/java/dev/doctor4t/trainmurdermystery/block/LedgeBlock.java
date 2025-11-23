package dev.doctor4t.trainmurdermystery.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class LedgeBlock extends HorizontalFacingBlock {
    public static final MapCodec<LedgeBlock> CODEC = createCodec(LedgeBlock::new);

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0, 14, 0, 16, 16, 8);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(8, 14, 0, 16, 16, 16);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0, 14, 8, 16, 16, 16);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0, 14, 0, 8, 16, 16);

    protected static final VoxelShape NORTH_SHAPE_SMALL = Block.createCuboidShape(0, 14, 0, 16, 16, 2);
    protected static final VoxelShape EAST_SHAPE_SMALL = Block.createCuboidShape(14, 14, 0, 16, 16, 16);
    protected static final VoxelShape SOUTH_SHAPE_SMALL = Block.createCuboidShape(0, 14, 14, 16, 16, 16);
    protected static final VoxelShape WEST_SHAPE_SMALL = Block.createCuboidShape(0, 14, 0, 2, 16, 16);

    public LedgeBlock(Settings settings) {
        super(settings);
        this.setDefaultState(super.getDefaultState()
                .with(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> NORTH_SHAPE_SMALL;
            case SOUTH -> SOUTH_SHAPE_SMALL;
            case WEST -> WEST_SHAPE_SMALL;
            case EAST -> EAST_SHAPE_SMALL;
            default -> null;
        };
    }

    public VoxelShape getCollisionShapeBig(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> null;
        };
    }

    public VoxelShape getCollisionShapeSmall(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> NORTH_SHAPE_SMALL;
            case SOUTH -> SOUTH_SHAPE_SMALL;
            case WEST -> WEST_SHAPE_SMALL;
            case EAST -> EAST_SHAPE_SMALL;
            default -> null;
        };
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (!context.isAbove(state.getOutlineShape(world, pos), pos, true)) {
            return getCollisionShapeSmall(state, world, pos, context);
        }
        return getCollisionShapeBig(state, world, pos, context);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
