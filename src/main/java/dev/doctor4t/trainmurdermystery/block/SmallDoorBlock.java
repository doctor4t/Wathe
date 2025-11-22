package dev.doctor4t.trainmurdermystery.block;

import dev.doctor4t.trainmurdermystery.block_entity.DoorBlockEntity;
import dev.doctor4t.trainmurdermystery.block_entity.SmallDoorBlockEntity;
import dev.doctor4t.trainmurdermystery.event.AllowPlayerOpenLockedDoor;
import dev.doctor4t.trainmurdermystery.index.TMMItems;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class SmallDoorBlock extends DoorPartBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    protected static final VoxelShape X_SHAPE = Block.createCuboidShape(7, 0, 0, 9, 16, 16);
    protected static final VoxelShape Z_SHAPE = Block.createCuboidShape(0, 0, 7, 16, 16, 9);
    private static final VoxelShape[] SHAPES = createShapes();
    private final Supplier<BlockEntityType<SmallDoorBlockEntity>> typeSupplier;

    public SmallDoorBlock(Supplier<BlockEntityType<SmallDoorBlockEntity>> typeSupplier, Settings settings) {
        super(settings);
        this.setDefaultState(super.getDefaultState().with(HALF, DoubleBlockHalf.LOWER));
        this.typeSupplier = typeSupplier;
    }

    private static VoxelShape[] createShapes() {
        VoxelShape[] shapes = new VoxelShape[16];
        VoxelShape lowerXShape = Block.createCuboidShape(7, 0, 0, 9, 32, 16);
        VoxelShape lowerZShape = Block.createCuboidShape(0, 0, 7, 16, 32, 9);
        VoxelShape upperXShape = Block.createCuboidShape(7, -16, 0, 9, 16, 16);
        VoxelShape upperZShape = Block.createCuboidShape(0, -16, 7, 16, 16, 9);
        for (Direction direction : Direction.Type.HORIZONTAL) {
            int id = direction.getHorizontal();
            boolean xAxis = direction.getAxis() == Direction.Axis.X;
            shapes[id] = xAxis ? lowerXShape : lowerZShape;
            shapes[id + 4] = xAxis ? upperXShape : upperZShape;
            Vector3f offset = direction.rotateYClockwise().getUnitVector().mul(7);
            Box box = new Box(7, 0, 7, 9, 32, 9).offset(offset);
            shapes[id + 8] = Block.createCuboidShape(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
            shapes[id + 12] = Block.createCuboidShape(box.minX, box.minY - 16, box.minZ, box.maxX, box.maxY - 16, box.maxZ);
        }
        return shapes;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), state.with(HALF, DoubleBlockHalf.UPPER));
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        DoubleBlockHalf half = state.get(HALF);
        if (direction == half.getOppositeDirection() &&
                (!neighborState.isOf(this)
                        || neighborState.get(FACING) != state.get(FACING)
                        || neighborState.get(HALF) != half.getOtherHalf())) {
            return Blocks.AIR.getDefaultState();
        }
        return state;
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState placementState = super.getPlacementState(ctx);
        if (placementState == null) {
            return null;
        }
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();
        return pos.getY() < world.getTopY() - 1 && world.getBlockState(pos.up()).canReplace(ctx) ? placementState : null;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (context.equals(ShapeContext.absent())) {
            return this.getShape(state);
        }
        boolean lower = state.get(HALF) == DoubleBlockHalf.LOWER;
        boolean open = state.get(OPEN);
        return SHAPES[state.get(FACING).getHorizontal() + (lower ? 0 : 4) + (open ? 8 : 0)];
    }

    @Override
    protected VoxelShape getShape(BlockState state) {
        return state.get(FACING).getAxis() == Direction.Axis.X ? X_SHAPE : Z_SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HALF);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return state.get(HALF) == DoubleBlockHalf.LOWER ? this.typeSupplier.get().instantiate(pos, state) : null;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return state.get(HALF) == DoubleBlockHalf.LOWER ? super.getTicker(world, state, type) : null;
    }

    @Override
    protected BlockEntityType<? extends DoorBlockEntity> getBlockEntityType() {
        return this.typeSupplier.get();
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        BlockPos lowerPos = state.get(HALF) == DoubleBlockHalf.LOWER ? pos : pos.down();
        if (world.getBlockEntity(lowerPos) instanceof SmallDoorBlockEntity entity) {
            if (entity.isBlasted()) {
                return ActionResult.PASS;
            }

            if (player.isCreative() || AllowPlayerOpenLockedDoor.EVENT.invoker().allowOpen(player)) {
                return open(state, world, entity, lowerPos);
            } else {
                boolean requiresKey = !entity.getKeyName().isEmpty();
                boolean hasLockpick = player.getMainHandStack().isOf(TMMItems.LOCKPICK);
                boolean jammed = entity.isJammed();

                if (entity.isOpen()) {
                    return open(state, world, entity, lowerPos);
                } else if (requiresKey && !jammed) {
                    if (player.getMainHandStack().isOf(TMMItems.CROWBAR)) return ActionResult.FAIL;
                    if (player.getMainHandStack().isOf(TMMItems.KEY) || hasLockpick) {
                        LoreComponent lore = player.getMainHandStack().get(DataComponentTypes.LORE);
                        boolean isRightKey = lore != null && !lore.lines().isEmpty() && lore.lines().getFirst().getString().equals(entity.getKeyName());
                        if (isRightKey || hasLockpick) {
                            if (isRightKey)
                                world.playSound(null, lowerPos.getX() + .5f, lowerPos.getY() + 1, lowerPos.getZ() + .5f, TMMSounds.ITEM_KEY_DOOR, SoundCategory.BLOCKS, 1f, 1f);
                            if (hasLockpick)
                                world.playSound(null, lowerPos.getX() + .5f, lowerPos.getY() + 1, lowerPos.getZ() + .5f, TMMSounds.ITEM_LOCKPICK_DOOR, SoundCategory.BLOCKS, 1f, 1f);
                            return open(state, world, entity, lowerPos);
                        } else {
                            if (!world.isClient) {
                                world.playSound(null, lowerPos.getX() + .5f, lowerPos.getY() + 1, lowerPos.getZ() + .5f, TMMSounds.BLOCK_DOOR_LOCKED, SoundCategory.BLOCKS, 1f, 1f);
                                player.sendMessage(Text.translatable("tip.door.requires_different_key"), true);
                            }
                            return ActionResult.FAIL;
                        }
                    }

                    if (!world.isClient) {
                        world.playSound(null, lowerPos.getX() + .5f, lowerPos.getY() + 1, lowerPos.getZ() + .5f, TMMSounds.BLOCK_DOOR_LOCKED, SoundCategory.BLOCKS, 1f, 1f);
                        player.sendMessage(Text.translatable("tip.door.requires_key"), true);
                    }
                    return ActionResult.FAIL;
                } else {
                    if (jammed) {
                        if (!world.isClient) {
                            world.playSound(null, lowerPos.getX() + .5f, lowerPos.getY() + 1, lowerPos.getZ() + .5f, TMMSounds.BLOCK_DOOR_LOCKED, SoundCategory.BLOCKS, 1f, 1f);
                            player.sendMessage(Text.translatable("tip.door.jammed"), true);
                        }
                    } else {
                        // open the door freely
                        return open(state, world, entity, lowerPos);
                    }
                }
            }
        }

        return ActionResult.PASS;
    }

    static @NotNull ActionResult open(BlockState state, World world, SmallDoorBlockEntity entity, BlockPos lowerPos) {
        if (world.isClient) return ActionResult.SUCCESS;
        toggleDoor(state, world, entity, lowerPos);
        return ActionResult.CONSUME;
    }

    public static void toggleDoor(BlockState state, World world, SmallDoorBlockEntity entity, BlockPos lowerPos) {
        entity.toggle(false);
        Direction facing = state.get(FACING);
        BlockPos neighborPos = lowerPos.offset(facing.rotateYCounterclockwise());
        BlockState neighborState = world.getBlockState(neighborPos);
        if (neighborState.isOf(state.getBlock())
                && neighborState.get(FACING).getOpposite() == facing
                && world.getBlockEntity(neighborPos) instanceof SmallDoorBlockEntity neighborEntity) {
            neighborEntity.toggle(true);
        }
    }


}
