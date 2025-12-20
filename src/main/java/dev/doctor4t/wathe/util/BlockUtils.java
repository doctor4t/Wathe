package dev.doctor4t.wathe.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class BlockUtils {

    public static Vec2f get2DHit(Vec3d hitPos, BlockPos blockPos, Direction side) {
        Vec3d pos = hitPos.subtract(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        float x = (float) pos.x;
        float y = (float) pos.y;
        float z = (float) pos.z;
        return switch (side) {
            case NORTH -> new Vec2f(1 - x, y);
            case EAST -> new Vec2f(1 - z, y);
            case SOUTH -> new Vec2f(x, y);
            case WEST -> new Vec2f(z, y);
            case UP -> new Vec2f(1 - x, z);
            case DOWN -> new Vec2f(1 - x, 1 - z);
        };
    }

}
