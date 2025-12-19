package dev.doctor4t.wathe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class WalkwayBlock extends Block {

    public static final EnumProperty<BlockHalf> HALF = Properties.BLOCK_HALF;
    protected static final VoxelShape TOP_SHAPE = Block.createCuboidShape(0, 13, 0, 16, 16, 16);
    protected static final VoxelShape BOTTOM_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 3, 16);

    public WalkwayBlock(Settings settings) {
        super(settings);
        this.setDefaultState(super.getDefaultState().with(HALF, BlockHalf.TOP));
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide();
        BlockHalf half = direction == Direction.UP ? BlockHalf.BOTTOM : BlockHalf.TOP;
        if (!ctx.canReplaceExisting() && direction.getAxis().isHorizontal()) {
            half = ctx.getHitPos().y - (double) ctx.getBlockPos().getY() > 0.5 ? BlockHalf.TOP : BlockHalf.BOTTOM;
        }
        return this.getDefaultState().with(HALF, half);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(HALF) == BlockHalf.TOP ? TOP_SHAPE : BOTTOM_SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HALF);
    }
}
