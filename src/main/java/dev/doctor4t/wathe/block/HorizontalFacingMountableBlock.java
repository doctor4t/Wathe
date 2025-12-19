package dev.doctor4t.wathe.block;

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
import org.jetbrains.annotations.Nullable;

public abstract class HorizontalFacingMountableBlock extends MountableBlock {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public HorizontalFacingMountableBlock(Settings settings) {
        super(settings);
        this.setDefaultState(super.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    public Vec3d getSitPos(World world, BlockState state, BlockPos pos) {
        Vec3d sitPos = this.getNorthFacingSitPos(world, state, pos);
        return switch (state.get(FACING)) {
            case EAST -> new Vec3d(sitPos.z, sitPos.y, 1 - sitPos.x);
            case SOUTH -> new Vec3d(1 - sitPos.x, sitPos.y, 1 - sitPos.z);
            case WEST -> new Vec3d(1 - sitPos.z, sitPos.y, sitPos.x);
            default -> sitPos;
        };
    }

    public abstract Vec3d getNorthFacingSitPos(World world, BlockState state, BlockPos pos);

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
