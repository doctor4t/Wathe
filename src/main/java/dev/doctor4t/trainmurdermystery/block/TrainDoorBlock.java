package dev.doctor4t.trainmurdermystery.block;

import dev.doctor4t.trainmurdermystery.block_entity.SmallDoorBlockEntity;
import dev.doctor4t.trainmurdermystery.cca.TrainWorldComponent;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TrainDoorBlock extends SmallDoorBlock {
    public TrainDoorBlock(BlockEntityType<SmallDoorBlockEntity> type, Settings settings) {
        super(type, settings);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        BlockPos lowerPos = state.get(HALF) == DoubleBlockHalf.LOWER ? pos : pos.down();
        if (!(world.getBlockEntity(lowerPos) instanceof SmallDoorBlockEntity entity)) {
            return ActionResult.PASS;
        }

        if (entity.isBlasted()) {
            return ActionResult.PASS;
        }

        if (player.isCreative() || TrainWorldComponent.KEY.get(world).getSpeed() == 0) {
            return open(state, world, entity, lowerPos);
        }

        if (entity.isOpen()) {
            return open(state, world, entity, lowerPos);
        }

        boolean hasLockpick = player.getMainHandStack().isOf(TMMItems.LOCKPICK);
        if (hasLockpick) {
            world.playSound(null, lowerPos.getX() + .5f, lowerPos.getY() + 1, lowerPos.getZ() + .5f, TMMSounds.ITEM_LOCKPICK_DOOR, SoundCategory.BLOCKS, 1f, 1f);
            return open(state, world, entity, lowerPos);
        }

        if (!world.isClient) {
            world.playSound(null, lowerPos.getX() + .5f, lowerPos.getY() + 1, lowerPos.getZ() + .5f, TMMSounds.BLOCK_DOOR_LOCKED, SoundCategory.BLOCKS, 1f, 1f);
            player.sendMessage(Text.translatable("tip.door.locked"), true);
        }
        return ActionResult.FAIL;
    }
}
