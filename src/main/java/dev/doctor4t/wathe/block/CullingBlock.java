package dev.doctor4t.wathe.block;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class CullingBlock extends MushroomBlock {

    public CullingBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        if (stateFrom.getBlock() instanceof GlassPanelBlock && stateFrom.get(GlassPanelBlock.FACING) == direction) {
            return true;
        }

        return stateFrom.getBlock() instanceof CullingBlock || stateFrom.isIn(ConventionalBlockTags.GLASS_BLOCKS);
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
        return world.getMaxLightLevel();
    }
}
