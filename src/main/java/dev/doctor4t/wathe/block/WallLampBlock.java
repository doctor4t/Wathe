package dev.doctor4t.wathe.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class WallLampBlock extends ToggleableFacingLightBlock {

    public static final VoxelShape FLOOR_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(6, 0, 6, 10, 10, 10),
            Block.createCuboidShape(5, 1, 5, 11, 9, 11)
    );
    public static final VoxelShape CEILING_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(6, 6, 6, 10, 16, 10),
            Block.createCuboidShape(5, 7, 5, 11, 15, 11)
    );
    public static final VoxelShape NORTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(6, 3, 11, 10, 13, 15),
            Block.createCuboidShape(5, 4, 10, 11, 12, 16)
    );
    public static final VoxelShape EAST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(1, 3, 6, 5, 13, 10),
            Block.createCuboidShape(0, 4, 5, 6, 12, 11)
    );
    public static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(6, 3, 1, 10, 13, 5),
            Block.createCuboidShape(5, 4, 0, 11, 12, 6)
    );
    public static final VoxelShape WEST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(11, 3, 6, 15, 13, 10),
            Block.createCuboidShape(10, 4, 5, 16, 12, 11)
    );

    public WallLampBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends FacingBlock> getCodec() {
        return null;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case UP -> FLOOR_SHAPE;
            case DOWN -> CEILING_SHAPE;
        };
    }
}
