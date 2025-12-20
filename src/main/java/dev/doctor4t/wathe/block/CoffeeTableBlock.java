package dev.doctor4t.wathe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class CoffeeTableBlock extends Block {

    private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 11, 16);

    public CoffeeTableBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
