package dev.doctor4t.wathe.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class CullingGlassBlock extends GlassPanelBlock {

    public CullingGlassBlock(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return switch (state.get(FACING)) {
            case NORTH -> NORTH_COLLISION_SHAPE;
            case EAST -> EAST_COLLISION_SHAPE;
            case SOUTH -> SOUTH_COLLISION_SHAPE;
            case WEST -> WEST_COLLISION_SHAPE;
            case UP -> UP_COLLISION_SHAPE;
            case DOWN -> DOWN_COLLISION_SHAPE;
        };
    }
}
