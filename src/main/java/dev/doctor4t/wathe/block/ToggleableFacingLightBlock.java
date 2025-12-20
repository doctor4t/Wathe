package dev.doctor4t.wathe.block;

import dev.doctor4t.wathe.index.WatheSounds;
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
import net.minecraft.world.World;

public abstract class ToggleableFacingLightBlock extends FacingLightBlock {
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
            world.playSound(null, pos, WatheSounds.BLOCK_LIGHT_TOGGLE, SoundCategory.BLOCKS, 0.5f, lit ? 1f : 1.2f);
            if (!state.get(ACTIVE)) {
                world.playSound(player, pos, WatheSounds.BLOCK_BUTTON_TOGGLE_NO_POWER, SoundCategory.BLOCKS, 0.1f, 1f);
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
}
