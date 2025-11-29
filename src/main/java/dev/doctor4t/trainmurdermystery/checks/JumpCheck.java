package dev.doctor4t.trainmurdermystery.checks;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class JumpCheck {

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (player.isCreative() || player.isSpectator()) {return;}
                if (!isTouching(player, Blocks.LADDER) &&
                        !isTouching(player, Blocks.VINE) &&
                        !isFluid(player)) {
                    double velocity = player.getVelocity().y;

                    if (velocity > 0.3 && !player.isOnGround()) {
                        System.out.println("jumped");
                        player.setVelocity(-player.getVelocity().x, -player.getVelocity().y, -player.getVelocity().z);
                        player.velocityModified = true; // make sure Minecraft updates the velocity
                    }
                }
            }
        });
    }

    public static BlockPos[] getPositions(BlockPos playerPos) {
        BlockPos[] positions = new BlockPos[] {
                playerPos,
                playerPos.add(0,1,0),
                playerPos.add(0,-1,0),
                playerPos.add(1,0,0),
                playerPos.add(-1,0,0),
                playerPos.add(0,0,1),
                playerPos.add(0,0,-1)
        };
        return positions;
    }

    public static boolean isTouching(ServerPlayerEntity player, Block block) {
        BlockPos playerPos = player.getBlockPos();

        for (BlockPos pos : getPositions(playerPos)) {
            if (player.getWorld().getBlockState(pos).isOf(block)) {
                return true;
            }
        }
        return  false;
    }

    public static boolean isFluid(ServerPlayerEntity player) {
        World world = player.getWorld();
        BlockPos playerPos = player.getBlockPos();

        for (BlockPos pos : getPositions(playerPos)) {
            if (world.getFluidState(pos).getBlockState().isOf(Blocks.WATER) || world.getFluidState(pos).getBlockState().isOf(Blocks.LAVA)) {
                return true;
            }
        }

        return false;
    }
}
