package dev.doctor4t.trainmurdermystery.block;

import dev.doctor4t.trainmurdermystery.cca.WorldBlackoutComponent;
import dev.doctor4t.trainmurdermystery.game.GameConstants;
import dev.doctor4t.trainmurdermystery.index.TMMSounds;
import dev.doctor4t.trainmurdermystery.util.BlackoutBlockFunctions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class ToggleableFacingLightBlock extends FacingLightBlock implements BlackoutBlock {
    public static final BooleanProperty LIT = Properties.LIT;

    public ToggleableFacingLightBlock(Settings settings) {
        super(settings);
        this.setDefaultState(super.getDefaultState()
                .with(LIT, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!player.shouldCancelInteraction()) {
            boolean lit = state.get(LIT);
            world.setBlockState(pos, state.with(LIT, !lit), Block.NOTIFY_ALL);
            world.playSound(null, pos, TMMSounds.BLOCK_LIGHT_TOGGLE, SoundCategory.BLOCKS, 0.5f, lit ? 1f : 1.2f);
            if (!state.get(ACTIVE)) {
                world.playSound(player, pos, TMMSounds.BLOCK_BUTTON_TOGGLE_NO_POWER, SoundCategory.BLOCKS, 0.1f, 1f);
            }
            return ActionResult.success(world.isClient);
        }
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(LIT);
        super.appendProperties(builder);
    }

    @Override
    public int getDuration(Random random) {
        return GameConstants.getRandomBlackoutDuration(random);
    }

    @Override
    public void init(@NotNull World world, WorldBlackoutComponent.BlackoutDetails detail) {
        BlackoutBlockFunctions.Lights.init(world, detail);
    }

    @Override
    public void end(@NotNull World world, WorldBlackoutComponent.BlackoutDetails detail) {
        BlackoutBlockFunctions.Lights.end(world, detail);
    }

    @Override
    public void tick(World world, WorldBlackoutComponent.BlackoutDetails detail) {
        BlackoutBlockFunctions.Lights.tick(world, detail);
    }
}
