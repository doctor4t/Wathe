package dev.doctor4t.wathe.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TrimmedLanternBlock extends ToggleableFacingLightBlock {
    protected static final VoxelShape FLOOR_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(3, 0, 3, 13, 10, 13),
            Block.createCuboidShape(2, 4, 2, 14, 6, 14)
    );
    protected static final VoxelShape CEILING_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(3, 6, 3, 13, 16, 13),
            Block.createCuboidShape(2, 10, 2, 14, 12, 14)
    );
    protected static final VoxelShape NORTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(3, 3, 10, 13, 13, 14),
            Block.createCuboidShape(2, 2, 14, 14, 14, 16)
    );
    protected static final VoxelShape EAST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(2, 3, 3, 6, 13, 13),
            Block.createCuboidShape(0, 2, 2, 2, 14, 14)
    );
    protected static final VoxelShape SOUTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(3, 3, 2, 13, 13, 6),
            Block.createCuboidShape(2, 2, 0, 14, 14, 2)
    );
    protected static final VoxelShape WEST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(10, 3, 3, 14, 13, 13),
            Block.createCuboidShape(14, 2, 2, 16, 14, 14)
    );

    public TrimmedLanternBlock(Settings settings) {
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

    @Override
    protected boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        super.onSyncedBlockEvent(state, world, pos, type, data);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity != null && blockEntity.onSyncedBlockEvent(type, data);
    }

    @Nullable
    @Override
    protected NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory) blockEntity : null;
    }
}
