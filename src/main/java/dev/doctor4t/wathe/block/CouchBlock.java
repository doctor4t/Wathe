package dev.doctor4t.wathe.block;

import dev.doctor4t.wathe.block.property.CouchArms;
import dev.doctor4t.wathe.index.WatheProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class CouchBlock extends HorizontalFacingMountableBlock {
    public static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 8, 16);
    public static final EnumProperty<CouchArms> ARMS = WatheProperties.COUCH_ARMS;

    public CouchBlock(Settings settings) {
        super(settings);
        this.setDefaultState(super.getDefaultState()
                .with(ARMS, CouchArms.SINGLE)
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ARMS, Properties.HORIZONTAL_FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public Vec3d getNorthFacingSitPos(World world, BlockState state, BlockPos pos) {
        return new Vec3d(0.5f, 0.5f, 0.375f);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx).with(ARMS, CouchArms.NO_ARMS);
        BlockState clickedBlockState = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(ctx.getSide().getOpposite()));

        if (ctx.shouldCancelInteraction()) {
            state = state.with(ARMS, CouchArms.SINGLE);
        } else if (clickedBlockState.getBlock() instanceof CouchBlock && clickedBlockState.get(Properties.HORIZONTAL_FACING).equals(state.get(Properties.HORIZONTAL_FACING))) {
            if (clickedBlockState.get(Properties.HORIZONTAL_FACING).rotateYClockwise().equals(ctx.getSide())) {
                state = state.with(ARMS, CouchArms.RIGHT);
            } else if (clickedBlockState.get(Properties.HORIZONTAL_FACING).rotateYCounterclockwise().equals(ctx.getSide())) {
                state = state.with(ARMS, CouchArms.LEFT);
            }
        }

        return state;
    }
}
