package dev.doctor4t.wathe.block;

import dev.doctor4t.wathe.block_entity.TrimmedBedBlockEntity;
import dev.doctor4t.wathe.index.WatheBlockEntities;
import dev.doctor4t.wathe.index.WatheItems;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.Dismounting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class TrimmedBedBlock extends BedBlock {
    public static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 8, 16);

    public TrimmedBedBlock(Settings settings) {
        super(DyeColor.WHITE, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(PART, BedPart.FOOT).with(OCCUPIED, false));
    }

    @Nullable
    public static Direction getDirection(BlockView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.getBlock() instanceof TrimmedBedBlock ? blockState.get(FACING) : null;
    }

    private static Direction getDirectionTowardsOtherPart(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

    private static boolean isBedBelow(BlockView world, BlockPos pos) {
        return world.getBlockState(pos.down()).getBlock() instanceof TrimmedBedBlock;
    }

    public static Optional<Vec3d> findWakeUpPosition(EntityType<?> type, CollisionView world, BlockPos pos, Direction bedDirection, float spawnAngle) {
        Direction direction = bedDirection.rotateYClockwise();
        Direction direction2 = direction.pointsTo(spawnAngle) ? direction.getOpposite() : direction;
        if (TrimmedBedBlock.isBedBelow(world, pos)) {
            return TrimmedBedBlock.findWakeUpPosition(type, world, pos, bedDirection, direction2);
        }
        int[][] is = TrimmedBedBlock.getAroundAndOnBedOffsets(bedDirection, direction2);
        Optional<Vec3d> optional = TrimmedBedBlock.findWakeUpPosition(type, world, pos, is, true);
        if (optional.isPresent()) {
            return optional;
        }
        return TrimmedBedBlock.findWakeUpPosition(type, world, pos, is, false);
    }

    private static Optional<Vec3d> findWakeUpPosition(EntityType<?> type, CollisionView world, BlockPos pos, Direction bedDirection, Direction respawnDirection) {
        int[][] is = TrimmedBedBlock.getAroundBedOffsets(bedDirection, respawnDirection);
        Optional<Vec3d> optional = TrimmedBedBlock.findWakeUpPosition(type, world, pos, is, true);
        if (optional.isPresent()) {
            return optional;
        }
        BlockPos blockPos = pos.down();
        Optional<Vec3d> optional2 = TrimmedBedBlock.findWakeUpPosition(type, world, blockPos, is, true);
        if (optional2.isPresent()) {
            return optional2;
        }
        int[][] js = TrimmedBedBlock.getOnBedOffsets(bedDirection);
        Optional<Vec3d> optional3 = TrimmedBedBlock.findWakeUpPosition(type, world, pos, js, true);
        if (optional3.isPresent()) {
            return optional3;
        }
        Optional<Vec3d> optional4 = TrimmedBedBlock.findWakeUpPosition(type, world, pos, is, false);
        if (optional4.isPresent()) {
            return optional4;
        }
        Optional<Vec3d> optional5 = TrimmedBedBlock.findWakeUpPosition(type, world, blockPos, is, false);
        if (optional5.isPresent()) {
            return optional5;
        }
        return TrimmedBedBlock.findWakeUpPosition(type, world, pos, js, false);
    }

    private static Optional<Vec3d> findWakeUpPosition(EntityType<?> type, CollisionView world, BlockPos pos, int[][] possibleOffsets, boolean ignoreInvalidPos) {
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int[] is : possibleOffsets) {
            mutable.set(pos.getX() + is[0], pos.getY(), pos.getZ() + is[1]);
            Vec3d vec3d = Dismounting.findRespawnPos(type, world, mutable, ignoreInvalidPos);
            if (vec3d == null) continue;
            return Optional.of(vec3d);
        }
        return Optional.empty();
    }

    private static int[][] getAroundAndOnBedOffsets(Direction bedDirection, Direction respawnDirection) {
        return ArrayUtils.addAll(TrimmedBedBlock.getAroundBedOffsets(bedDirection, respawnDirection), TrimmedBedBlock.getOnBedOffsets(bedDirection));
    }

    private static int[][] getAroundBedOffsets(Direction bedDirection, Direction respawnDirection) {
        return new int[][]{{respawnDirection.getOffsetX(), respawnDirection.getOffsetZ()}, {respawnDirection.getOffsetX() - bedDirection.getOffsetX(), respawnDirection.getOffsetZ() - bedDirection.getOffsetZ()}, {respawnDirection.getOffsetX() - bedDirection.getOffsetX() * 2, respawnDirection.getOffsetZ() - bedDirection.getOffsetZ() * 2}, {-bedDirection.getOffsetX() * 2, -bedDirection.getOffsetZ() * 2}, {-respawnDirection.getOffsetX() - bedDirection.getOffsetX() * 2, -respawnDirection.getOffsetZ() - bedDirection.getOffsetZ() * 2}, {-respawnDirection.getOffsetX() - bedDirection.getOffsetX(), -respawnDirection.getOffsetZ() - bedDirection.getOffsetZ()}, {-respawnDirection.getOffsetX(), -respawnDirection.getOffsetZ()}, {-respawnDirection.getOffsetX() + bedDirection.getOffsetX(), -respawnDirection.getOffsetZ() + bedDirection.getOffsetZ()}, {bedDirection.getOffsetX(), bedDirection.getOffsetZ()}, {respawnDirection.getOffsetX() + bedDirection.getOffsetX(), respawnDirection.getOffsetZ() + bedDirection.getOffsetZ()}};
    }

    private static int[][] getOnBedOffsets(Direction bedDirection) {
        return new int[][]{{0, 0}, {-bedDirection.getOffsetX(), -bedDirection.getOffsetZ()}};
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.CONSUME;
        } else {
            if (!player.isCreative() && player.getStackInHand(Hand.MAIN_HAND).isOf(WatheItems.SCORPION)) {
                TrimmedBedBlockEntity blockEntity = null;

                if (world.getBlockEntity(pos) instanceof TrimmedBedBlockEntity firstBlockEntity) {
                    if (world.getBlockState(pos).get(PART) == BedPart.HEAD)
                        blockEntity = firstBlockEntity;
                    else {
                        BlockPos headPos = pos.offset(world.getBlockState(pos).get(FACING));
                        if (world.getBlockEntity(headPos) instanceof TrimmedBedBlockEntity foundBlockEntity)
                            blockEntity = foundBlockEntity;
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

                return ActionResult.SUCCESS;
            } else {
                player.trySleep(pos).ifLeft(reason -> {
                    if (reason.getMessage() != null) {
                        player.sendMessage(reason.getMessage(), true);
                    }
                });
                return ActionResult.SUCCESS;
            }
        }
    }

    private boolean wakePlayers(World world, BlockPos pos) {
        List<PlayerEntity> list = world.getEntitiesByClass(PlayerEntity.class, new Box(pos), LivingEntity::isSleeping);
        if (list.isEmpty()) {
            return false;
        } else {
            (list.get(0)).wakeUp();
            return true;
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (!world.isClient || !type.equals(WatheBlockEntities.TRIMMED_BED)) {
            return null;
        }
        return TrimmedBedBlockEntity::clientTick;
    }

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        super.onLandedUpon(world, state, pos, entity, fallDistance * 0.5f);
    }

    @Override
    public void onEntityLand(BlockView world, Entity entity) {
        if (entity.bypassesLandingEffects()) {
            super.onEntityLand(world, entity);
        } else {
            this.bounceEntity(entity);
        }
    }

    private void bounceEntity(Entity entity) {
        Vec3d vec3d = entity.getVelocity();
        if (vec3d.y < 0.0) {
            double d = entity instanceof LivingEntity ? 1.0 : 0.8;
            entity.setVelocity(vec3d.x, -vec3d.y * (double) 0.66f * d, vec3d.z);
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (direction == TrimmedBedBlock.getDirectionTowardsOtherPart(state.get(PART), state.get(FACING))) {
            if (neighborState.isOf(this) && neighborState.get(PART) != state.get(PART)) {
                return state.with(OCCUPIED, neighborState.get(OCCUPIED));
            }
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            BedPart bedPart = state.get(PART);
            if (bedPart == BedPart.FOOT) {
                BlockPos blockPos = pos.offset(getDirectionTowardsOtherPart(bedPart, state.get(FACING)));
                BlockState blockState = world.getBlockState(blockPos);
                if (blockState.isOf(this) && blockState.get(PART) == BedPart.HEAD) {
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
                    world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(blockState));
                }
            }
        }

        return super.onBreak(world, pos, state, player);
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getHorizontalPlayerFacing();
        BlockPos blockPos = ctx.getBlockPos();
        BlockPos blockPos2 = blockPos.offset(direction);
        World world = ctx.getWorld();
        if (world.getBlockState(blockPos2).canReplace(ctx) && world.getWorldBorder().contains(blockPos2)) {
            return this.getDefaultState().with(FACING, direction);
        }
        return null;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, OCCUPIED);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient) {
            BlockPos blockPos = pos.offset(state.get(FACING));
            world.setBlockState(blockPos, state.with(PART, BedPart.HEAD), Block.NOTIFY_ALL);
            world.updateNeighbors(pos, Blocks.AIR);
            state.updateNeighbors(world, pos, Block.NOTIFY_ALL);
        }
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TrimmedBedBlockEntity(WatheBlockEntities.TRIMMED_BED, pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
