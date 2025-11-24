package dev.doctor4t.trainmurdermystery.util;

import dev.doctor4t.trainmurdermystery.block_entity.TrimmedBedBlockEntity;
import dev.doctor4t.trainmurdermystery.cca.PlayerPoisonComponent;
import dev.doctor4t.trainmurdermystery.networking.PoisonOverlayS2CPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PoisonUtils {
    public static float getFovMultiplier(float tickDelta, PlayerPoisonComponent poisonComponent) {
        if (!poisonComponent.pulsing) return 1f;

        poisonComponent.pulseProgress += tickDelta * 0.1f;

        if (poisonComponent.pulseProgress >= 1f) {
            poisonComponent.pulsing = false;
            poisonComponent.pulseProgress = 0f;
            return 1f;
        }

        final float maxAmplitude = 0.1f;
        final float minAmplitude = 0.025f;

        return getResult(poisonComponent, minAmplitude, maxAmplitude);
    }

    private static float getResult(PlayerPoisonComponent poisonComponent, float minAmplitude, float maxAmplitude) {
        float amplitude = minAmplitude + (maxAmplitude - minAmplitude) * (1f - ((float) poisonComponent.poisonTicks / 1200f));

        if (poisonComponent.pulseProgress < 0.25f) {
            return 1f - amplitude * (float) Math.sin(Math.PI * (poisonComponent.pulseProgress / 0.25f));
        }

        if (poisonComponent.pulseProgress < 0.5f) {
            return 1f - amplitude * (float) Math.sin(Math.PI * ((poisonComponent.pulseProgress - 0.25f) / 0.25f));
        }

        return 1f;
    }

    public static void bedPoison(ServerPlayerEntity player) {
        World world = player.getEntityWorld();
        BlockPos bedPos = player.getBlockPos();

        TrimmedBedBlockEntity blockEntity = findHeadInBoxWithObstacles(world, bedPos);
        if (blockEntity == null) {
            return;
        }

        if (world.isClient) {
            return;
        }

        UUID poisoner = blockEntity.getPoisoner();

        blockEntity.setHasScorpion(false, null);

        updatePoisonTicks(player, poisoner, world.getRandom());

        ServerPlayNetworking.send(
                player, new PoisonOverlayS2CPayload("game.player.stung")
        );
    }

    public static void updatePoisonTicks(PlayerEntity player, @Nullable UUID poisoner, Random random) {
        int poisonTicks = PlayerPoisonComponent.KEY.get(player).poisonTicks;

        int updated;
        if (poisonTicks == -1) {
            updated = random.nextBetween(PlayerPoisonComponent.TIME_BOUNDS.getLeft(), PlayerPoisonComponent.TIME_BOUNDS.getRight());
        } else {
            updated = MathHelper.clamp(poisonTicks - random.nextBetween(100, 300), 0, PlayerPoisonComponent.TIME_BOUNDS.getRight());
        }

        PlayerPoisonComponent.KEY.get(player).setPoisonTicks(updated, poisoner);
    }

    private static TrimmedBedBlockEntity findHeadInBoxWithObstacles(World world, BlockPos centerPos) {
        int radius = 2;
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos pos = centerPos.add(dx, dy, dz);
                    TrimmedBedBlockEntity entity = resolveHead(world, pos);
                    if (entity != null && entity.hasScorpion()) {
                        if (isLineClear(world, centerPos, pos)) {
                            return entity;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static boolean isLineClear(World world, BlockPos start, BlockPos end) {
        // Use simple 3D Bresenham line algorithm
        int x0 = start.getX(), y0 = start.getY(), z0 = start.getZ();
        int x1 = end.getX(), y1 = end.getY(), z1 = end.getZ();

        int dx = Math.abs(x1 - x0), dy = Math.abs(y1 - y0), dz = Math.abs(z1 - z0);
        int sx = x0 < x1 ? 1 : -1, sy = y0 < y1 ? 1 : -1, sz = z0 < z1 ? 1 : -1;
        int err1, err2;

        int ax = 2 * dx, ay = 2 * dy, az = 2 * dz;

        if (dx >= dy && dx >= dz) {
            err1 = ay - dx;
            err2 = az - dx;
            while (x0 != x1) {
                x0 += sx;
                if (err1 > 0) {
                    y0 += sy;
                    err1 -= 2 * dx;
                }
                if (err2 > 0) {
                    z0 += sz;
                    err2 -= 2 * dx;
                }
                err1 += ay;
                err2 += az;

                if (isBlocking(world, new BlockPos(x0, y0, z0))) return false;
            }
        } else if (dy >= dx && dy >= dz) {
            err1 = ax - dy;
            err2 = az - dy;
            while (y0 != y1) {
                y0 += sy;
                if (err1 > 0) {
                    x0 += sx;
                    err1 -= 2 * dy;
                }
                if (err2 > 0) {
                    z0 += sz;
                    err2 -= 2 * dy;
                }
                err1 += ax;
                err2 += az;

                if (isBlocking(world, new BlockPos(x0, y0, z0))) return false;
            }
        } else {
            err1 = ay - dz;
            err2 = ax - dz;
            while (z0 != z1) {
                z0 += sz;
                if (err1 > 0) {
                    y0 += sy;
                    err1 -= 2 * dz;
                }
                if (err2 > 0) {
                    x0 += sx;
                    err2 -= 2 * dz;
                }
                err1 += ay;
                err2 += ax;

                if (isBlocking(world, new BlockPos(x0, y0, z0))) return false;
            }
        }

        return true;
    }

    private static boolean isBlocking(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return !(state.getBlock() instanceof BedBlock);
    }

    /**
     * Resolve a bed block (head or foot) into its head entity.
     */
    private static TrimmedBedBlockEntity resolveHead(World world, BlockPos pos) {
        if (!(world.getBlockEntity(pos) instanceof TrimmedBedBlockEntity entity)) return null;

        BedPart part = world.getBlockState(pos).get(BedBlock.PART);
        Direction facing = world.getBlockState(pos).get(HorizontalFacingBlock.FACING);

        if (part == BedPart.HEAD) return entity;

        if (part == BedPart.FOOT) {
            BlockPos headPos = pos.offset(facing);
            if (world.getBlockEntity(headPos) instanceof TrimmedBedBlockEntity headEntity &&
                    world.getBlockState(headPos).get(BedBlock.PART) == BedPart.HEAD) return headEntity;
        }

        return null;
    }
}