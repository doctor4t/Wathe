package dev.doctor4t.wathe.block;

import net.minecraft.block.BarrierBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class LightBarrierBlock extends BarrierBlock {
    public LightBarrierBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected int getOpacity(BlockState state, BlockView world, BlockPos pos) {
        return 15;
    }
}