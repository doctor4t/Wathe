package dev.doctor4t.wathe.block;

import com.mojang.serialization.MapCodec;
import dev.doctor4t.wathe.block_entity.SprinklerBlockEntity;
import dev.doctor4t.wathe.index.WatheBlockEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class SprinklerBlock extends WallMountedBlock implements BlockEntityProvider {
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final MapCodec<SprinklerBlock> CODEC = createCodec(SprinklerBlock::new);
    protected static final VoxelShape UP_SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 3.0, 13.0);
    protected static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(3.0, 13.0, 3.0, 13.0, 16.0, 13.0);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 3.0, 3.0, 3.0, 13.0, 13.0);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(13.0, 3.0, 3.0, 16.0, 13.0, 13.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(3.0, 3.0, 0.0, 13.0, 13.0, 3.0);
    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(3.0, 3.0, 13.0, 13.0, 13.0, 16.0);

    public SprinklerBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(POWERED, false).with(FACING, Direction.NORTH).with(FACE, BlockFace.WALL));
    }

    public static Direction getDirection(BlockState state) {
        return switch (state.get(FACE)) {
            case CEILING -> Direction.DOWN;
            case FLOOR -> Direction.UP;
            default -> state.get(FACING);
        };
    }

    @Override
    protected MapCodec<? extends WallMountedBlock> getCodec() {
        return CODEC;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (getDirection(state)) {
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            case EAST -> EAST_SHAPE;
            case UP -> UP_SHAPE;
            case DOWN -> DOWN_SHAPE;
            default -> NORTH_SHAPE;
        };
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return true;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (state != null) {
            return state.with(POWERED, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
        }
        return null;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!world.isClient) {
            if (world.isReceivingRedstonePower(pos) || world.isReceivingRedstonePower(fromPos)) {
                world.scheduleBlockTick(pos, this, 4);
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockState cycle = state.cycle(POWERED);
        world.setBlockState(pos, cycle, Block.NOTIFY_LISTENERS);
        world.getBlockEntity(pos, WatheBlockEntities.SPRINKLER).ifPresent(entity -> {
            entity.setPowered(cycle.get(POWERED));
            entity.sync();
        });
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED, FACING, FACE);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SprinklerBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (!world.isClient || !type.equals(WatheBlockEntities.SPRINKLER)) {
            return null;
        }
        return SprinklerBlockEntity::clientTick;
    }
}
