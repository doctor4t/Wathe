package dev.doctor4t.trainmurdermystery.block;

import dev.doctor4t.trainmurdermystery.block_entity.TrimmedBedBlockEntity;
import dev.doctor4t.trainmurdermystery.index.TMMBlockEntities;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TrimmedBedBlock extends BedBlock {
    public static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 8, 16);

    public TrimmedBedBlock(Settings settings) {
        super(DyeColor.WHITE, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(PART, BedPart.FOOT).with(OCCUPIED, false));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.CONSUME;
        }

        if (!player.isCreative() && player.getStackInHand(Hand.MAIN_HAND).isOf(TMMItems.SCORPION)) {
            TrimmedBedBlockEntity blockEntity = null;

            if (world.getBlockEntity(pos) instanceof TrimmedBedBlockEntity firstBlockEntity) {
                if (world.getBlockState(pos).get(PART) == BedPart.HEAD) {
                    blockEntity = firstBlockEntity;
                } else {
                    BlockPos headPos = pos.offset(world.getBlockState(pos).get(FACING));
                    if (world.getBlockEntity(headPos) instanceof TrimmedBedBlockEntity foundBlockEntity) {
                        blockEntity = foundBlockEntity;
                    }
                }
            }

            if (blockEntity != null) {
                if (!blockEntity.hasScorpion()) {
                    blockEntity.setHasScorpion(true, player.getUuid());
                    player.getStackInHand(Hand.MAIN_HAND).decrement(1);

                    return ActionResult.SUCCESS;
                }
            }
        }

        if (state.get(PART) != BedPart.HEAD) {
            pos = pos.offset(state.get(FACING));
            state = world.getBlockState(pos);
            if (!state.isOf(this)) {
                return ActionResult.CONSUME;
            }
        }

        if (state.get(OCCUPIED)) {
            if (!this.wakePlayers(world, pos)) {
                player.sendMessage(Text.translatable("block.minecraft.bed.occupied"), true);
            }
        } else {
            player.trySleep(pos).ifLeft(reason -> {
                if (reason.getMessage() != null) {
                    player.sendMessage(reason.getMessage(), true);
                }
            });
        }
        return ActionResult.SUCCESS;
    }

    private boolean wakePlayers(World world, BlockPos pos) {
        List<PlayerEntity> list = world.getEntitiesByClass(PlayerEntity.class, new Box(pos), LivingEntity::isSleeping);
        if (list.isEmpty()) {
            return false;
        }

        list.getFirst().wakeUp();
        return true;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (!world.isClient || !type.equals(TMMBlockEntities.TRIMMED_BED)) {
            return null;
        }
        return TrimmedBedBlockEntity::clientTick;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TrimmedBedBlockEntity(TMMBlockEntities.TRIMMED_BED, pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
