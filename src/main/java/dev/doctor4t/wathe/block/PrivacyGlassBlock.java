package dev.doctor4t.wathe.block;

import dev.doctor4t.wathe.cca.WorldBlackoutComponent;
import dev.doctor4t.wathe.util.BlackoutBlockFunctions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TransparentBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class PrivacyGlassBlock extends TransparentBlock implements PrivacyBlock, BlackoutBlock {

    public PrivacyGlassBlock(Settings settings) {
        super(settings);
        this.setDefaultState(super.getDefaultState()
                .with(ACTIVE, true)
                .with(OPAQUE, false)
                .with(INTERACTION_COOLDOWN, false));
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
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
        builder.add(OPAQUE, INTERACTION_COOLDOWN, ACTIVE);
    }

    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    @Override
    public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
        return world.getMaxLightLevel();
    }

    @Override
    public int getDuration(Random random) {
        return BlackoutBlockFunctions.PrivacyGlasses.getDuration(random);
    }

    @Override
    public void init(@NotNull World world, WorldBlackoutComponent.BlackoutDetails detail) {
        BlackoutBlockFunctions.PrivacyGlasses.init(world, detail);
    }

    @Override
    public void end(@NotNull World world, WorldBlackoutComponent.BlackoutDetails detail) {
        BlackoutBlockFunctions.PrivacyGlasses.end(world, detail);
    }

    @Override
    public void tick(World world, WorldBlackoutComponent.BlackoutDetails detail) {
        BlackoutBlockFunctions.PrivacyGlasses.tick(world, detail);
    }
}
