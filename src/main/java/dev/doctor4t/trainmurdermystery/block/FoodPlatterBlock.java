package dev.doctor4t.trainmurdermystery.block;

import com.mojang.serialization.MapCodec;
import dev.doctor4t.trainmurdermystery.block_entity.PlateBlockEntity;
import dev.doctor4t.trainmurdermystery.index.TMMBlockEntities;
import dev.doctor4t.trainmurdermystery.index.TMMDataComponentTypes;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FoodPlatterBlock extends BlockWithEntity {
    public static final MapCodec<FoodPlatterBlock> CODEC = createCodec(FoodPlatterBlock::new);
    public FoodPlatterBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PlateBlockEntity(TMMBlockEntities.PLATE, pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return this.getShape(state);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShape(state);
    }

    protected VoxelShape getShape(BlockState state) {
        return Block.createCuboidShape(0, 0, 0, 16, 2, 16);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        if (!(world.getBlockEntity(pos) instanceof PlateBlockEntity blockEntity)) {
            return ActionResult.PASS;
        }

        if (player.isCreative()) {
            ItemStack heldItem = player.getStackInHand(Hand.MAIN_HAND);

            if (!heldItem.isEmpty()) {
                blockEntity.addItem(heldItem);
                return ActionResult.SUCCESS;
            }
        } else if (player.getStackInHand(Hand.MAIN_HAND).isEmpty()) {
            List<ItemStack> platter = blockEntity.getStoredItems();
            if (platter.isEmpty()) return ActionResult.SUCCESS;

            boolean hasPlatterItem = false;
            for (ItemStack platterItem : platter) {
                for (int i = 0; i < player.getInventory().size(); i++) {
                    ItemStack invItem = player.getInventory().getStack(i);

                    if (invItem.getItem() == platterItem.getItem()) {
                        hasPlatterItem = true;
                        break;
                    }
                }
                if (hasPlatterItem) break;
            }

            if (!hasPlatterItem) {
                ItemStack randomItem = platter.get(world.random.nextInt(platter.size())).copy();
                if (blockEntity.getPoisonedItemsCount() > 0) {
                    randomItem.set(TMMDataComponentTypes.POISONED, true);
                    blockEntity.setPoisonedItemsCount(blockEntity.getPoisonedItemsCount() - 1);
                }

                player.playSoundToPlayer(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 1f, 1f);
                player.setStackInHand(Hand.MAIN_HAND, randomItem);
            }
        } if (player.getStackInHand(Hand.MAIN_HAND).isOf(TMMItems.POISON_VIAL)) {
            blockEntity.setPoisonedItemsCount(blockEntity.getPoisonedItemsCount() + 1);
            player.getStackInHand(Hand.MAIN_HAND).decrement(1);
            player.playSoundToPlayer(SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.BLOCKS, 0.5f, 1f);
        }

        return ActionResult.PASS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (!world.isClient || !type.equals(TMMBlockEntities.PLATE)) {
            return null;
        }
        return PlateBlockEntity::clientTick;
    }
}
