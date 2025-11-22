package dev.doctor4t.trainmurdermystery.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class HorizontalFacingMountableBlock extends MountableBlock {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public HorizontalFacingMountableBlock(Settings settings) {
        super(settings);
        this.setDefaultState(super.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public Vec3d getSitOffset(World world, BlockState state, BlockPos pos) {
        Vec3d offset = this.getNorthFacingSitOffset(world, state, pos);
        return switch (state.get(FACING)) {
            case EAST -> new Vec3d(offset.z, offset.y, 1 - offset.x);
            case SOUTH -> new Vec3d(1 - offset.x, offset.y, 1 - offset.z);
            case WEST -> new Vec3d(1 - offset.z, offset.y, offset.x);
            default -> offset;
        };
    }

    public abstract Vec3d getNorthFacingSitOffset(World world, BlockState state, BlockPos pos);

    @NotNull
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
