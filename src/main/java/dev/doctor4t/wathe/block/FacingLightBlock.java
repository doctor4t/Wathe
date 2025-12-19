package dev.doctor4t.wathe.block;

import dev.doctor4t.wathe.index.WatheProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public abstract class FacingLightBlock extends FacingBlock {

    public static final BooleanProperty ACTIVE = WatheProperties.ACTIVE;

    public FacingLightBlock(Settings settings) {
        super(settings);
        this.setDefaultState(super.getDefaultState()
                .with(FACING, Direction.DOWN)
                .with(ACTIVE, true));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getSide())
                .with(ACTIVE, true);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }
}
