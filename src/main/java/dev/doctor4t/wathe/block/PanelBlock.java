package dev.doctor4t.wathe.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.LichenGrower;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Arrays;

public class PanelBlock extends MultifaceGrowthBlock {

    public PanelBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends MultifaceGrowthBlock> getCodec() {
        return null;
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return true;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return state;
    }

    @Override
    public boolean canGrowWithDirection(BlockView world, BlockState state, BlockPos pos, Direction direction) {
        return this.canHaveDirection(direction) && (!state.isOf(this) || !hasDirection(state, direction));
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return context.getStack().isOf(this.asItem())
                && Arrays.stream(DIRECTIONS).anyMatch(direction -> !hasDirection(state, direction))
                && !context.shouldCancelInteraction();
    }

    @Override
    public LichenGrower getGrower() {
        return null;
    }

}
