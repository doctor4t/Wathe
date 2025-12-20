package dev.doctor4t.trainmurdermystery.item;

import dev.doctor4t.trainmurdermystery.block.SmallDoorBlock;
import dev.doctor4t.trainmurdermystery.block_entity.SmallDoorBlockEntity;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import dev.doctor4t.trainmurdermystery.util.AdventureUsable;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class LockpickItem extends Item implements AdventureUsable {
    public LockpickItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof SmallDoorBlock) {
            BlockPos lowerPos = state.get(SmallDoorBlock.HALF) == DoubleBlockHalf.LOWER ? pos : pos.down();
            if (world.getBlockEntity(lowerPos) instanceof SmallDoorBlockEntity) {
                if (player.isSneaking()) {
                    for (Vec3i neighborOffset : new Vec3i[]{
                            new Vec3i(0, 0, 0),
                            new Vec3i(1, 0, 0),
                            new Vec3i(-1, 0, 0),
                            new Vec3i(0, 0, 1),
                            new Vec3i(0, 0, -1)
                    })
                        if (world.getBlockEntity(lowerPos.add(neighborOffset)) instanceof SmallDoorBlockEntity neighborBlock)
                            neighborBlock.jam();
                    if (!player.isCreative())
                        player.getItemCooldownManager().set(this, GameConstants.ITEM_COOLDOWNS.get(this));
                    if (!world.isClient)
                        world.playSound(null, lowerPos.getX() + .5f, lowerPos.getY() + 1, lowerPos.getZ() + .5f, TMMSounds.ITEM_LOCKPICK_DOOR, SoundCategory.BLOCKS, 1f, 1f);
                    return ActionResult.SUCCESS;
                }
            }

            return ActionResult.PASS;
        }

        return super.useOnBlock(context);
    }

}
