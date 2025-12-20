package dev.doctor4t.wathe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BarStoolBlock extends MountableBlock {
    private static final Vec3d SIT_POS = new Vec3d(0.5f, -0.2f, 0.5f);

    private static final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(6, 0, 6, 10, 1, 10),
            Block.createCuboidShape(7, 1, 7, 9, 9, 9),
            Block.createCuboidShape(4, 4, 4, 12, 5, 12),
            Block.createCuboidShape(3, 9, 3, 13, 12, 13)
    );

    public BarStoolBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public Vec3d getSitPos(World world, BlockState state, BlockPos pos) {
        return SIT_POS;
    }
}
