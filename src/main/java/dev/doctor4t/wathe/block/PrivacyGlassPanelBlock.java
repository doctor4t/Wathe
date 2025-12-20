package dev.doctor4t.wathe.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class PrivacyGlassPanelBlock extends GlassPanelBlock implements PrivacyBlock {

    public PrivacyGlassPanelBlock(Settings settings) {
        super(settings);
        this.setDefaultState(super.getDefaultState()
                .with(OPAQUE, false)
                .with(INTERACTION_COOLDOWN, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!player.shouldCancelInteraction() && !player.getMainHandStack().isOf(this.asItem()) && this.canInteract(state, pos, world, player, Hand.MAIN_HAND)) {
            this.toggle(state, world, pos);

            return ActionResult.success(world.isClient);
        }

        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.toggle(state, world, pos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(OPAQUE, INTERACTION_COOLDOWN);
        super.appendProperties(builder);
    }
}
