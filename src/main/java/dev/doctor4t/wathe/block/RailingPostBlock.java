package dev.doctor4t.wathe.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class RailingPostBlock extends AbstractRailingBlock {

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0, 0, 0, 2, 16, 2);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(14, 0, 0, 16, 16, 2);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(14, 0, 14, 16, 16, 16);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0, 0, 14, 2, 16, 16);
    protected static final VoxelShape NORTH_COLLISION_SHAPE = Block.createCuboidShape(0, 0, 0, 2, 24, 2);
    protected static final VoxelShape EAST_COLLISION_SHAPE = Block.createCuboidShape(14, 0, 0, 16, 24, 2);
    protected static final VoxelShape SOUTH_COLLISION_SHAPE = Block.createCuboidShape(14, 0, 14, 16, 24, 16);
    protected static final VoxelShape WEST_COLLISION_SHAPE = Block.createCuboidShape(0, 0, 14, 2, 24, 16);

    public RailingPostBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return null;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            default -> WEST_SHAPE;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> NORTH_COLLISION_SHAPE;
            case EAST -> EAST_COLLISION_SHAPE;
            case SOUTH -> SOUTH_COLLISION_SHAPE;
            default -> WEST_COLLISION_SHAPE;
        };
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (state == null) return null;
        return state.with(FACING, Direction.fromRotation(ctx.getPlayerYaw() + 45d));
    }
}
