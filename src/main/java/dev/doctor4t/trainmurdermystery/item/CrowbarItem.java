package dev.doctor4t.trainmurdermystery.item;

import dev.doctor4t.trainmurdermystery.block_entity.DoorBlockEntity;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.index.TMMBlocks;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import dev.doctor4t.trainmurdermystery.util.AdventureUsable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class CrowbarItem extends Item implements AdventureUsable {

    public Set<Block> glassBlocks = Set.of(
            TMMBlocks.CULLING_GLASS,
            TMMBlocks.HULL_GLASS,
            TMMBlocks.RHOMBUS_GLASS,
            TMMBlocks.GOLDEN_GLASS_PANEL,
            TMMBlocks.PRIVACY_GLASS_PANEL,
            TMMBlocks.RHOMBUS_HULL_GLASS
    );

    public CrowbarItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        boolean success = false;
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockEntity entity = world.getBlockEntity(blockPos);
        PlayerEntity player = context.getPlayer();
        BlockState targetBlockState = world.getBlockState(blockPos);

        if(player != null) {
            if (!(entity instanceof DoorBlockEntity)) entity = world.getBlockEntity(context.getBlockPos().down());
            if (entity instanceof DoorBlockEntity door && !door.isBlasted()) {
                world.playSound(null, context.getBlockPos(), TMMSounds.ITEM_CROWBAR_PRY, SoundCategory.BLOCKS, 2.5f, 1f);
                door.blast();
                success = true;
            } else if(targetBlockState != null) {
                Set<BlockPos> checked = new HashSet<>();
                Deque<BlockPos> toCheck = new ArrayDeque<>();
                toCheck.push(blockPos);

                while(!toCheck.isEmpty()) {
                    BlockPos currentBlock = toCheck.pop();

                    if(checked.contains(currentBlock)) continue;
                    checked.add(currentBlock);

                    if(!glassBlocks.contains(world.getBlockState(currentBlock).getBlock())) continue;
                    world.playSound(null, currentBlock, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    success = true;

                    world.breakBlock(currentBlock, false);

                    for(Direction direction : Direction.values()) {
                        BlockPos nextBlockPos = currentBlock.offset(direction);
                        if(!checked.contains(nextBlockPos)) {
                            BlockState nextBlockState = world.getBlockState(nextBlockPos);
                            if(glassBlocks.contains(nextBlockState.getBlock())) {
                                toCheck.push(nextBlockPos);
                            }
                        }
                    }
                }
            }
            if(success) {
                player.swingHand(Hand.MAIN_HAND, true);
                if (!player.isCreative()) player.getItemCooldownManager().set(this, GameConstants.ITEM_COOLDOWNS.get(this));
            }
        }
        return super.useOnBlock(context);
    }
}