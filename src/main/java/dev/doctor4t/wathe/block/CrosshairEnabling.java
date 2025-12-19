package dev.doctor4t.wathe.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public interface CrosshairEnabling {

    boolean shouldShowCrosshair(World world, BlockState state, BlockHitResult hit);

}
