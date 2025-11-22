package dev.doctor4t.trainmurdermystery.command;

import com.mojang.brigadier.CommandDispatcher;
import dev.doctor4t.trainmurdermystery.block.SmallDoorBlock;
import dev.doctor4t.trainmurdermystery.block_entity.SmallDoorBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

public class UpdateDoorsCommand {
    public static void register(@NotNull CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("tmm:updateDoors").requires(source -> source.hasPermissionLevel(2)).executes(context -> {
            ServerCommandSource source = context.getSource();

            BlockPos playerPos = source.getPlayer().getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            for (int x = -250; x <= 250; x++) {
                for (int y = -10; y <= 10; y++) {
                    for (int z = -250; z <= 250; z++) {
                        mutable.set(playerPos.getX() + x, playerPos.getY() + y, playerPos.getZ() + z);
                        ServerWorld world = source.getWorld();
                        BlockState blockState = world.getBlockState(mutable);
                        if (blockState.getBlock() instanceof SmallDoorBlock && blockState.get(SmallDoorBlock.HALF).equals(DoubleBlockHalf.LOWER)) {
                            if (world.getBlockEntity(mutable) instanceof SmallDoorBlockEntity entity) {
                                SmallDoorBlock.toggleDoor(blockState, world, entity, mutable);
                            }
                        }
                    }
                }
            }

            return 1;
        }));
    }
}
