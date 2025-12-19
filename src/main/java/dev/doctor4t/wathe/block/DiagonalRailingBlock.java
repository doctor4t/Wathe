package dev.doctor4t.wathe.block;

import com.mojang.serialization.MapCodec;
import dev.doctor4t.wathe.block.property.RailingShape;
import dev.doctor4t.wathe.index.WatheProperties;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.StairShape;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class DiagonalRailingBlock extends AbstractRailingBlock {

    public static final BooleanProperty LEFT = WatheProperties.LEFT;
    public static final EnumProperty<RailingShape> SHAPE = WatheProperties.RAILING_SHAPE;
    protected static final VoxelShape NORTH_LEFT_SHAPE = createShape(16, 8, 2, 0, 0, 8, 0);
    protected static final VoxelShape NORTH_RIGHT_SHAPE = createShape(16, 8, 2, 8, 0, 0, 0);
    protected static final VoxelShape EAST_LEFT_SHAPE = createShape(16, 2, 8, 14, 0, 14, 8);
    protected static final VoxelShape EAST_RIGHT_SHAPE = createShape(16, 2, 8, 14, 8, 14, 0);
    protected static final VoxelShape SOUTH_LEFT_SHAPE = createShape(16, 8, 2, 8, 14, 0, 14);
    protected static final VoxelShape SOUTH_RIGHT_SHAPE = createShape(16, 8, 2, 0, 14, 8, 14);
    protected static final VoxelShape WEST_LEFT_SHAPE = createShape(16, 2, 8, 0, 8, 0, 0);
    protected static final VoxelShape WEST_RIGHT_SHAPE = createShape(16, 2, 8, 0, 0, 0, 8);

    public DiagonalRailingBlock(Settings settings) {
        super(settings);
        this.setDefaultState(super.getDefaultState().with(LEFT, false).with(SHAPE, RailingShape.MIDDLE));
    }

    protected static VoxelShape createShape(int height, int sizeX, int sizeZ, int x1, int z1, int x2, int z2) {
        return VoxelShapes.union(
                Block.createCuboidShape(x1, 0, z1, x1 + sizeX, height, z1 + sizeZ),
                Block.createCuboidShape(x2, -8, z2, x2 + sizeX, height - 8, z2 + sizeZ)
        );
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return null;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (state == null) {
            return null;
        }
        World world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();
        Direction facing = state.get(FACING);
        BlockState stateForStairsBelow = this.getPlacementState(state, world.getBlockState(pos.down()));
        if (stateForStairsBelow != null) {
            boolean left = stateForStairsBelow.get(LEFT);
            BlockPos neighborPos = pos.offset(left ? facing.rotateYCounterclockwise() : facing.rotateYClockwise());
            return this.tryConnecting(stateForStairsBelow, world.getBlockState(neighborPos));
        }
        Direction left = facing.rotateYCounterclockwise();
        state = state.with(SHAPE, RailingShape.BOTTOM);
        BlockState stateForStairsLeft = this.getPlacementState(state, world.getBlockState(pos.offset(left)));
        if (stateForStairsLeft != null) {
            return stateForStairsLeft;
        }
        Direction right = facing.rotateYClockwise();
        return this.getPlacementState(state, world.getBlockState(pos.offset(right)));
    }

    @Nullable
    private BlockState getPlacementState(BlockState state, BlockState stairsState) {
        Direction facing = state.get(FACING);
        if (stairsState.getBlock() instanceof StairsBlock) {
            if (stairsState.get(StairsBlock.HALF) == BlockHalf.TOP) {
                return null;
            }

            Direction stairsFacing = stairsState.get(StairsBlock.FACING);
            StairShape stairShape = stairsState.get(StairsBlock.SHAPE);

            if (stairsFacing.rotateYClockwise() == facing) {
                if (stairShape != StairShape.INNER_RIGHT && stairShape != StairShape.OUTER_LEFT) {
                    return state.with(LEFT, true);
                }
            } else if (stairsFacing.rotateYCounterclockwise() == facing) {
                if (stairShape != StairShape.INNER_LEFT && stairShape != StairShape.OUTER_RIGHT) {
                    return state.with(LEFT, false);
                }
            } else if (stairsFacing == facing) {
                if (stairShape == StairShape.OUTER_LEFT) {
                    return state.with(LEFT, true);
                } else if (stairShape == StairShape.OUTER_RIGHT) {
                    return state.with(LEFT, false);
                }
            } else if (stairsFacing.getOpposite() == facing) {
                if (stairShape == StairShape.INNER_LEFT) {
                    return state.with(LEFT, false);
                } else if (stairShape == StairShape.INNER_RIGHT) {
                    return state.with(LEFT, true);
                }
            }

        } else if (stairsState.getBlock() instanceof TrimmedStairsBlock) {
            Direction stairsFacing = stairsState.get(TrimmedStairsBlock.FACING);
            if (stairsFacing.rotateYClockwise() == facing) {
                return state.with(LEFT, false);
            } else if (stairsFacing.rotateYCounterclockwise() == facing) {
                return state.with(LEFT, true);
            }
        }
        return null;
    }

    private BlockState tryConnecting(BlockState state, BlockState neighborState) {
        if (state.get(SHAPE) != RailingShape.MIDDLE) {
            return state;
        }
        Direction facing = state.get(FACING);
        boolean left = state.get(LEFT);
        if (neighborState.getBlock() instanceof RailingBlock && neighborState.get(FACING) == facing) {
            return state.with(SHAPE, RailingShape.TOP);
        } else if (neighborState.getBlock() instanceof RailingPostBlock && neighborState.get(FACING) == (left ? facing.rotateYClockwise() : facing)) {
            return state.with(SHAPE, RailingShape.TOP);
        }
        return state;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        BlockState blockState = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        if (blockState == null) {
            return null;
        } else if (state.get(SHAPE) == RailingShape.MIDDLE) {
            Direction facing = state.get(FACING);
            boolean left = state.get(LEFT);
            if (neighborPos.equals(pos.offset(left ? facing.rotateYCounterclockwise() : facing.rotateYClockwise()))) {
                return this.tryConnecting(state, neighborState);
            }
        }
        return blockState;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(SHAPE) == RailingShape.BOTTOM) {
            return switch (state.get(FACING)) {
                case NORTH -> RailingBlock.NORTH_SHAPE;
                case EAST -> RailingBlock.EAST_SHAPE;
                case SOUTH -> RailingBlock.SOUTH_SHAPE;
                default -> RailingBlock.WEST_SHAPE;
            };
        } else if (state.get(LEFT)) {
            return switch (state.get(FACING)) {
                case NORTH -> NORTH_LEFT_SHAPE;
                case EAST -> EAST_LEFT_SHAPE;
                case SOUTH -> SOUTH_LEFT_SHAPE;
                default -> WEST_LEFT_SHAPE;
            };
        }
        return switch (state.get(FACING)) {
            case NORTH -> NORTH_RIGHT_SHAPE;
            case EAST -> EAST_RIGHT_SHAPE;
            case SOUTH -> SOUTH_RIGHT_SHAPE;
            default -> WEST_RIGHT_SHAPE;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getOutlineShape(state, world, pos, context);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LEFT, SHAPE);
        super.appendProperties(builder);
    }
}
