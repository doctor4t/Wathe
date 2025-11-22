package dev.doctor4t.trainmurdermystery.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class RailingBlock extends AbstractRailingBlock {

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 16, 2);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(14, 0, 0, 16, 16, 16);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0, 0, 14, 16, 16, 16);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0, 0, 0, 2, 16, 16);
    private static final MapCodec<RailingBlock> CODEC;

    private final Block diagonalRailingBlock;
    private final Block postBlock;

    public RailingBlock(Block diagonalRailingBlock, Block postBlock, Settings settings) {
        super(settings);
        this.diagonalRailingBlock = diagonalRailingBlock;
        this.postBlock = postBlock;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return switch (state.get(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            default -> WEST_SHAPE;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.getOutlineShape(state, world, pos, context);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);

        if (state == null) {
            return null;
        }
        if (ctx.shouldCancelInteraction()) {
            return this.postBlock.getPlacementState(ctx);
        }

        BlockState diagonalState = this.diagonalRailingBlock.getPlacementState(ctx);
        return diagonalState == null ? state : diagonalState;
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    static {
        Codec<Block> blockCodec = Registries.BLOCK.getCodec();
        CODEC = RecordCodecBuilder.mapCodec(
                (instance) ->
                        instance.group(
                                blockCodec.fieldOf("diagonalRailingBlock").forGetter(railing -> railing.diagonalRailingBlock),
                                blockCodec.fieldOf("postBlock").forGetter(railing -> railing.postBlock),
                                createSettingsCodec()
                ).apply(instance, RailingBlock::new));
    }
}
