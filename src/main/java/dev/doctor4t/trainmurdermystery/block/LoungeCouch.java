package dev.doctor4t.trainmurdermystery.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LoungeCouch extends CouchBlock {
    private static final Vec3d SIT_OFFSET = new Vec3d(0.5f, -0.5f, 0.6f);

    public LoungeCouch(Settings settings) {
        super(settings);
    }

    @Override
    public Vec3d getNorthFacingSitOffset(World world, BlockState state, BlockPos pos) {
        return SIT_OFFSET;
    }
}
