package dev.doctor4t.wathe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BarTableBlock extends Block {

    private static final VoxelShape SHAPE = VoxelShapes.union(
            Block.createCuboidShape(4, 0, 4, 12, 1, 12),
            Block.createCuboidShape(5, 1, 5, 11, 14, 11),
            Block.createCuboidShape(0, 14, 0, 16, 16, 16)
    );

    public BarTableBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
