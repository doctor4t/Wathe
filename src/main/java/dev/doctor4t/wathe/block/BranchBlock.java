package dev.doctor4t.wathe.block;

import com.mojang.serialization.MapCodec;
import dev.doctor4t.wathe.index.tag.WatheBlockTags;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * @author EightSidedSquare
 */

public class BranchBlock extends ConnectingBlock {

    public static final Map<Block, Block> STRIPPED_BRANCHES = new Object2ObjectOpenHashMap<>();

    public BranchBlock(Settings settings) {
        super(0.25f, settings);
        this.setDefaultState(super.getDefaultState()
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false)
                .with(UP, false)
                .with(DOWN, false));
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        Direction side = ctx.getSide();
        if (state != null) return this.connectState(state
                .with(FACING_PROPERTIES.get(side), ctx.shouldCancelInteraction())
                .with(FACING_PROPERTIES.get(side.getOpposite()), true), pos, world);
        return null;
    }

    public BlockState connectState(BlockState state, BlockPos pos, WorldAccess world) {
        BlockState blockState = state;
        for (Direction direction : Direction.values())
            blockState = this.connectState(blockState, pos, world, direction);
        return blockState;
    }

    public BlockState connectState(BlockState state, BlockPos pos, WorldAccess world, Direction direction) {
        BlockState sideState = world.getBlockState(pos.offset(direction));
        if (!sideState.isIn(WatheBlockTags.BRANCHES)) return state;
        BooleanProperty sideProperty = FACING_PROPERTIES.get(direction.getOpposite());
        if (sideState.contains(sideProperty) && sideState.get(sideProperty)) {
            return state.with(FACING_PROPERTIES.get(direction), true);
        }
        return state;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return this.connectState(state, pos, world, direction);
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!stack.isOf(Items.SHEARS)) {
            return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
        }
        boolean success = false;
        Vec3d hitPos = hit.getPos().subtract(pos.getX(), pos.getY(), pos.getZ());
        Direction direction = null;
        if (hitPos.x < 0.25) direction = Direction.WEST;
        else if (hitPos.x > 0.75) direction = Direction.EAST;
        else if (hitPos.y < 0.25) direction = Direction.DOWN;
        else if (hitPos.y > 0.75) direction = Direction.UP;
        else if (hitPos.z < 0.25) direction = Direction.NORTH;
        else if (hitPos.z > 0.75) direction = Direction.SOUTH;
        if (direction != null) {
            BooleanProperty property = FACING_PROPERTIES.get(direction);
            if (state.get(property)) {
                world.setBlockState(pos, state.with(property, false), Block.NOTIFY_ALL);
                BlockPos sidePos = pos.offset(direction);
                BlockState sideState = world.getBlockState(sidePos);
                BooleanProperty oppositeProperty = FACING_PROPERTIES.get(direction.getOpposite());
                if (sideState.isIn(WatheBlockTags.BRANCHES) && sideState.contains(oppositeProperty) && sideState.get(oppositeProperty)) {
                    world.setBlockState(sidePos, sideState.with(oppositeProperty, false), Block.NOTIFY_ALL);
                }
                success = true;
            }
        }
        Direction side = hit.getSide();
        BooleanProperty property = FACING_PROPERTIES.get(side);
        if (!success && !state.get(property)) {
            world.setBlockState(pos, state.with(property, true), Block.NOTIFY_ALL);
            success = true;
        }
        if (success) {
            world.playSound(player, pos, SoundEvents.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1f, 1f);
            return ItemActionResult.SUCCESS;
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    protected MapCodec<? extends ConnectingBlock> getCodec() {
        return null;
    }
}