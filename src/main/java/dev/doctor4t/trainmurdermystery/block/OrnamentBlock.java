package dev.doctor4t.trainmurdermystery.block;

import com.mojang.serialization.MapCodec;
import dev.doctor4t.trainmurdermystery.block.property.OrnamentShape;
import dev.doctor4t.trainmurdermystery.index.TMMProperties;
import dev.doctor4t.trainmurdermystery.mixin.AbstractBlockInvoker;
import dev.doctor4t.trainmurdermystery.util.BlockUtils;
import net.fabricmc.fabric.mixin.object.builder.AbstractBlockAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class OrnamentBlock extends FacingBlock {

    public static final EnumProperty<OrnamentShape> SHAPE = TMMProperties.ORNAMENT_SHAPE;

    public OrnamentBlock(Settings settings) {
        super(settings);
        this.setDefaultState(super.getDefaultState()
                .with(FACING, Direction.NORTH)
                .with(SHAPE, OrnamentShape.CENTER));
    }

    @Override
    protected MapCodec<? extends FacingBlock> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        Direction side = ctx.getSide();
        World world = ctx.getWorld();
        BlockState state = world.getBlockState(pos);
        Vec2f hit = BlockUtils.get2DHit(ctx.getHitPos(), pos, side);
        boolean topRight = hit.x + hit.y > 1;
        boolean bottomRight = hit.x - hit.y > 0;
        boolean center = ctx.shouldCancelInteraction();
        OrnamentShape shape = center ? OrnamentShape.CENTER :
                topRight && bottomRight ? OrnamentShape.RIGHT :
                        topRight ? OrnamentShape.TOP :
                                !bottomRight ? OrnamentShape.LEFT : OrnamentShape.BOTTOM;
        if (state.isOf(this)) {
            OrnamentShape originalShape = state.get(SHAPE);
            OrnamentShape newShape = originalShape.with(shape);
            if (originalShape == newShape) return null;
            return state.with(SHAPE, newShape);
        }
        return this.getDefaultState()
                .with(FACING, side)
                .with(SHAPE, shape);
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        return (context.getStack().isOf(this.asItem())) || super.canReplace(state, context);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> GlassPanelBlock.NORTH_COLLISION_SHAPE;
            case EAST -> GlassPanelBlock.EAST_COLLISION_SHAPE;
            case SOUTH -> GlassPanelBlock.SOUTH_COLLISION_SHAPE;
            case WEST -> GlassPanelBlock.WEST_COLLISION_SHAPE;
            case UP -> GlassPanelBlock.UP_COLLISION_SHAPE;
            case DOWN -> GlassPanelBlock.DOWN_COLLISION_SHAPE;
        };
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!player.shouldCancelInteraction() && !player.getMainHandStack().isOf(this.asItem())) {
            Direction dir = state.get(FACING);
            BlockPos behindBlockPos = pos.subtract(new Vec3i(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ()));
            BlockState blockBehindOrnament = world.getBlockState(behindBlockPos);
            return ((AbstractBlockInvoker) blockBehindOrnament.getBlock()).tmm$invokeOnUseWithItem(stack, blockBehindOrnament, world, behindBlockPos, player, hand, hit.withBlockPos(behindBlockPos));
        }
        return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!player.shouldCancelInteraction() && !player.getMainHandStack().isOf(this.asItem())) {
            Direction dir = state.get(FACING);
            BlockPos behindBlockPos = pos.subtract(new Vec3i(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ()));
            BlockState blockBehindOrnament = world.getBlockState(behindBlockPos);
                return ((AbstractBlockInvoker) blockBehindOrnament.getBlock()).tmm$invokeOnUse(blockBehindOrnament, world, behindBlockPos, player, hit.withBlockPos(behindBlockPos));
        }
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, SHAPE);
    }
}
