package dev.doctor4t.wathe.block_entity;

import dev.doctor4t.wathe.index.WatheBlockEntities;
import dev.doctor4t.wathe.index.WatheParticles;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChimneyBlockEntity extends SyncingBlockEntity {

    public ChimneyBlockEntity(BlockPos pos, BlockState state) {
        super(WatheBlockEntities.CHIMNEY, pos, state);
    }

    public static <T extends BlockEntity> void clientTick(World world, BlockPos pos, BlockState state, T t) {
        world.addParticle(WatheParticles.BLACK_SMOKE, pos.getX() + .5f, pos.getY(), pos.getZ() + .5f, 0, 0, 0);
    }
}
